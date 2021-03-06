package Controller;

import Constant.Constant;
import Exception.*;
import Model.Entity.Animal.Cat;
import Model.Entity.Animal.Dog;
import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Cow;
import Model.Entity.Animal.Pet.Sheep;
import Model.Entity.Animal.Wild.Bear;
import Model.Entity.Animal.Wild.Lion;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import Model.Upgradable;
import Model.WareHouse;
import Model.Well;
import Model.WorkShop;
import Network.Client.Client;
import View.Scene.GameScene;
import com.gilecode.yagson.YaGson;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller
{
    private int money, turn;
    private ArrayList<WorkShop> workShops = new ArrayList<>();
    private Map map;
    private Well well;
    private WareHouse wareHouse;
    private Level level;
    private Helicopter helicopter;
    private Truck truck;
    private int catLevel = 1;
    private boolean isGameFinished=false;
    private int levelNumber=-1;
    private boolean onlineLevelChecker=false;

    Controller(int goalMoney, ArrayList<String> goalEntities, ArrayList<Item> helicopterItems)
    {
        money = turn = 0;
        map = new Map();
        well = new Well();
        level = new Level(goalMoney, goalEntities);
        helicopter = new Helicopter(helicopterItems);
        truck = new Truck();
        wareHouse = new WareHouse();
    }
    public Controller(int initialMoney,int goalMoney, ArrayList<String> goalEntities)
    {
        //For Online 2player games only
        this(goalMoney,goalEntities,new ArrayList<>());
        onlineLevelChecker=true;
        money=initialMoney;
    }
    Controller(int levelNumber,int goalMoney, ArrayList<String> goalEntities, ArrayList<Item> helicopterItems)
    {
        this(goalMoney,goalEntities,helicopterItems);
        this.levelNumber=levelNumber;
    }

    public int getMoney()
    {
        return money;
    }

    public int getTurn()
    {
        return turn;
    }

    public void subtractMoney(int money2) throws NotEnoughMoneyException
    {
        if (money < money2)
            throw new NotEnoughMoneyException();
        money -= money2;
        GameScene.updateMoney();
        if(InputReader.getClient()!=null) {
            InputReader.getClient().setMoney(getMoney());
            if (InputReader.getClient().isOnline()) InputReader.getClient().updateClient();
        }
    }

    void setMoney(int money)
    {
        this.money = money;
        GameScene.updateMoney();
        if(InputReader.getClient()!=null) {
            InputReader.getClient().setMoney(getMoney());
            if (InputReader.getClient().isOnline()) InputReader.getClient().updateClient();
        }
    }

    public void increaseMoney(int money2)
    {
        money += money2;
        GameScene.updateMoney();
        if(InputReader.getClient()!=null) {
            InputReader.getClient().setMoney(getMoney());
            if (InputReader.getClient().isOnline()) InputReader.getClient().updateClient();
        }
    }

    public void addItemToWareHouse(Item item) throws NoWarehouseSpaceException
    {
        wareHouse.addItem(item);
        item.setInWareHouse(true);
        /*
        try
        {
            if (item.getCell() != null)
            {
                item.destroyFromMap();
            }
        } catch (CellDoesNotExistException e)
        {
            e.printStackTrace();
        }
        */
    }

    void plant(int x, int y) throws NoWaterException, CellDoesNotExistException
    {
        if (well.getWaterRemaining() == 0)
        {
            throw new NoWaterException();
        }
        if (map.plant(x, y)) well.liftWater();
    }

    private String getInfoOfObject(Object object)
    {
        if (object instanceof Map)
        {
            return ((Map) object).printInfo();
        }
        YaGson yaGson = new YaGson();
        return yaGson.toJson(object);
    }

    String getInfo(String string) throws IOException
    {
        if (string.equals("info"))
        {
            StringBuilder stringBuilder = new StringBuilder("");

            stringBuilder.append("Money:");
            stringBuilder.append(getMoney());
            stringBuilder.append("\n");

            stringBuilder.append("Turn:");
            stringBuilder.append(getTurn());
            stringBuilder.append("\n");

            stringBuilder.append(getInfoOfObject(level));
            return stringBuilder.toString();
        }
        if (string.equals("workshops"))
        {
            return getInfoOfObject(workShops);
        }
        return getInfoOfObject(getObject(string));
    }


    public void startAWorkShop(int index) throws WorkshopDoesntExistException, StartBusyProducerException,
            WorkShopNotUsedException, NotEnoughItemException
    {
        if (index >= workShops.size()) throw new WorkshopDoesntExistException();
        WorkShop workShop = workShops.get(index);
        int usedLevel = workShop.maxLevelCanDoWithItems(wareHouse.getItems());
        if (usedLevel == -1) throw new NotEnoughItemException();
        workShop.startByLevel(usedLevel);
        ArrayList<Item> neededItems = workShop.getInputItemsByUsedLevel();
        for (Item item : neededItems)
        {
            try
            {
                item.moveToWorkshopFromWareHouse(index);
                wareHouse.eraseItem(item);
            } catch (NoSuchItemInWarehouseException e)
            {
                System.out.println("This Shouldn't Happen!");
                e.printStackTrace();
            }
        }
    }

    public void createWorkshops() throws IOException
    {
        for (int workshopNumber = 0; workshopNumber < 6; workshopNumber++)
        {
            YaGson yaGson = new YaGson();
            WorkShop workshop = yaGson.fromJson(new FileReader("../../workshop" + workshopNumber + ".json"), WorkShop.class);
            workShops.add(workshop);
        }
    }

    public void addWorkshop(WorkShop workShop)
    {
        workShops.add(workShop);
    }

    void nextTurn()
    {
        turn++;
        map.nextTurn();
        if (helicopter.isTransportationEnds())
        {
            distributeItems(helicopter.getItems());
            helicopter.endTransportation();
            helicopter.clear();
        }
        if (truck.isTransportationEnds())
        {
            if(truck.getValue()!=0)increaseMoney(truck.getValue());
            truck.endTransportation();
            truck.clear();
        }

        for (WorkShop workShop : workShops)
        {
            if (workShop.haveProduct())
            {
                try
                {
                    distributeItems(workShop.getOutputItemsByUsedLevel(), workShop.getLocation());
                    workShop.endProduction();
                } catch (WorkShopNotUsedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (!onlineLevelChecker && level.checkLevel())
        {
            win();
        }

        Client client=InputReader.getClient();
        if(onlineLevelChecker && client!=null && getTurn()%15==((client.getName().hashCode()%15+15)%15)){
            client.checkLevel();
        }
    }

    public void win() {
        InputReader.getClient().setMoney(0);
        if(levelNumber!=-1){
            InputReader.getClient().setLevel(levelNumber);
        }
        if(InputReader.getClient().isOnline())InputReader.getClient().updateClient();
        GameScene.setWinningMessage();
        Media media=new Media(new File("Textures/Sound/WinSound.wav").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
    }


    public Truck getTruck()
    {
        return truck;
    }

    public Helicopter getHelicopter()
    {
        return helicopter;
    }

    private void distributeItems(ArrayList<Item> items)
    {
        for (Item item : items)
        {
            Cell randomCell = map.getRandomCell();
            item.setCell(randomCell);
            getMap().addEntity(randomCell, item);
        }
    }
    private void distributeItems(ArrayList<Item> items, int location)
    {
        for (Item item : items)
        {
            Cell randomCell = map.getRandomCell();

        //    Timeline timeline = item.moveToCellFromWorkshop(location, randomCell);

            item.setCell(randomCell);
            getMap().addEntity(randomCell, item);


        }
    }


    private void distributeItems(HashMap<Item,Integer> items)
    {

        for (Item item : items.keySet())
        {
            for(int i=0;i<items.get(item);i++)
            {
                Item newItem=Constant.getItemByType(item.getName());
                Cell randomCell = map.getRandomCell();
                newItem.setCell(randomCell);
                getMap().addEntity(randomCell, newItem);
            }
        }
    }

    private Object getObject(String type) throws IOException
    {
        // if(type.equals("cat"))return cat;
        // if(type.equals("dog"))return dog;
        if (type.equals("well")) return well;
        if (type.equals("helicopter")) return helicopter;
        if (type.equals("truck")) return truck;
        if (type.equals("warehouse")) return wareHouse;
        if (type.equals("map")) return map;
        if (type.equals("level")) return level;

        if (type.startsWith("workshop"))
        {
            int workshopNumber = type.charAt(type.length() - 1) - '0';
            return workShops.get(workshopNumber);
        }
        throw new IOException();
    }

    public int getCatLevel()
    {
        return catLevel;
    }

    void upgrade(String type) throws IOException, CantUpgradeException, NotEnoughMoneyException
    {
        if (type.equals("cat"))
        {
            if (catLevel == 1)
                throw new CantUpgradeException();
            subtractMoney(Constant.CAT_UPGRADE_COST);
            catLevel = 1;
            return;
        }
        Object object = getObject(type);
        if (!(object instanceof Upgradable))
        {
            throw new IOException();
        }
        Upgradable upgradable = (Upgradable) object;
        if (!upgradable.canUpgrade())
        {
            throw new CantUpgradeException();
        }
        subtractMoney(upgradable.upgradeCost());
        try
        {
            upgradable.upgrade();
        } catch (CantUpgradeException e)
        {
            increaseMoney(upgradable.upgradeCost());
        }
    }

    void addAnimal(String type) throws IOException, NotEnoughMoneyException
    {
        type = type.toLowerCase();
        switch (type)
        {
            case Constant.DOG_NAME:
                subtractMoney(Constant.DOG_ADD_COST);
                new Dog(map.getRandomCell());
                break;
            case Constant.CAT_NAME:
                subtractMoney(Constant.CAT_ADD_COST);
                new Cat(map.getRandomCell());
                break;
            case Constant.COW_NAME:
                subtractMoney(Constant.COW_ADD_COST);
                new Cow(map.getRandomCell());
                break;
            case Constant.SHEEP_NAME:
                subtractMoney(Constant.SHEEP_ADD_COST);
                new Sheep(map.getRandomCell());
                break;
            case Constant.CHICKEN_NAME:
                subtractMoney(Constant.CHICKEN_ADD_COST);
                new Chicken(map.getRandomCell());
                break;
            case Constant.BEAR_NAME:
                new Bear(map.getRandomCell());
                break;
            case Constant.LION_NAME:
                new Lion(map.getRandomCell());
                break;
            default:
                throw new IOException();
        }

    }

    void pickup(int x, int y) throws CellDoesNotExistException
    {
        ArrayList<Item> items = map.getItems(x, y);
        for (Item item : items)
        {
            try
            {
                wareHouse.addItem(item);
            //    map.destroyItem(x, y, item);
            } catch (NoWarehouseSpaceException ignored)
            {

            }
        }
    }

    void fillWell() throws NotEnoughMoneyException
    {
        if (well.getWaterRemaining() == well.getMaxWater()) return;
        subtractMoney(Constant.WELL_FILL_COST + well.getLevel() * Constant.WELL_FILL_COST_PER_LEVEL);
        well.fill();
    }

    void cage(int x, int y) throws CellDoesNotExistException
    {
        map.cage(x, y);
    }

    void clearTruck() {

        HashMap<Item,Integer> items=truck.getItems();
        for (Item item : items.keySet())
        {
            for(int i=0;i<items.get(item);i++)
            {
                Item newItem=Constant.getItemByType(item.getName());
                try
                {
                    wareHouse.addItem(newItem);
                } catch (NoWarehouseSpaceException e)
                {
                    e.printStackTrace();
                }
            }
        }
        truck.clear();
    }

    void clearHelicopter()
    {
        helicopter.clear();
    }

    void addItemToHelicopter(Item item) throws NoTransporterSpaceException
    {
        helicopter.addItem(item);
    }

    void addItemToTruck(Item item) throws NoTransporterSpaceException, NoSuchItemInWarehouseException
    {
        if (!truck.canAddItem(item))
            throw new NoTransporterSpaceException();
        wareHouse.eraseItem(item);
        truck.addItem(item);
    }

    void startTruck() throws StartBusyTransporter, StartEmptyTransporter
    {
        if(InputReader.getClient().isOnline())
        {
            InputReader.getClient().addMarketItems(truck.getItems());
        }
        truck.startTransportation();
    }

    void startHelicopter() throws NotEnoughMoneyException, StartBusyTransporter, StartEmptyTransporter
    {
        if(InputReader.getClient().isOnline())
        {
            try
            {
                InputReader.getClient().removeItems(helicopter.getItems());
            } catch (NotEnoughItemException e)
            {
                helicopter.clear();
                System.out.println(Constant.NOT_ENOUGH_ITEM_MESSAGE);
                return ;
            }
        }
        subtractMoney(helicopter.getValue());
        helicopter.startTransportation();
    }

    public ArrayList<WorkShop> getWorkShops()
    {
        return workShops;
    }

    public Map getMap()
    {
        return map;
    }

    public Well getWell()
    {
        return well;
    }

    public WareHouse getWareHouse()
    {
        return wareHouse;
    }

    public Level getLevel()
    {
        return level;
    }

    public void finishGame()
    {
        isGameFinished=true;
    }

    public boolean isGameFinished()
    {
        return isGameFinished;
    }

    public void initLoad() {
        ArrayList<Entity> entities=getMap().getEntities();
        for(Entity entity:entities){
            entity.initLoad();
        }
    }
}
