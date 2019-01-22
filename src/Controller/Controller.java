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
import View.Scene.GameScene;
import com.gilecode.yagson.YaGson;
import Exception.WinningMessage;
import Exception.NoTransporterSpaceException;
import Exception.NoSuchItemInWarehouseException;
import Exception.NoWarehouseSpaceException;
import Exception.NotEnoughItemException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private int money, turn;
    private ArrayList<WorkShop> workShops = new ArrayList<>();
    private Map map;
    private Well well;
    private WareHouse wareHouse;
    private Level level;
    private Helicopter helicopter;
    private Truck truck;
    private int catLevel=1;


    Controller(int goalMoney, ArrayList<Entity> earnedEnitities,ArrayList<Item> helicopterItems) {
        money = turn = 0;
        map = new Map();
        well = new Well();
        level = new Level(goalMoney, earnedEnitities);
        helicopter = new Helicopter(helicopterItems);
        truck = new Truck();
        wareHouse = new WareHouse();
    }

    public int getMoney() {
        return money;
    }

    public int getTurn() {
        return turn;
    }

    void subtractMoney(int money2) throws NotEnoughMoneyException {
        if (money < money2)
            throw new NotEnoughMoneyException();
        money -= money2;
        GameScene.updateMoney();
    }

    void setMoney(int money) {
        this.money = money;
        GameScene.updateMoney();
    }

    void increaseMoney(int money2) {
        money += money2;
        GameScene.updateMoney();
    }

    public void addItemToWareHouse(Item item) throws NoWarehouseSpaceException{
        //TODO level.entityEarned(item);
        wareHouse.addItem(item);
        item.setInWareHouse(true);
        try {
            if(item.getCell()!=null){
                item.destroyFromMap();
            }
        } catch (CellDoesNotExistException e) {
            e.printStackTrace();
        }
    }
    void plant(int x, int y) throws NoWaterException, CellDoesNotExistException {
        if (well.getWaterRemaining() == 0) {
            throw new NoWaterException();
        }
        if (map.plant(x, y)) well.liftWater();
    }

    private String getInfoOfObject(Object object) {
        if(object instanceof Map){
            return ((Map)object).printInfo();
        }
        YaGson yaGson=new YaGson();
        return yaGson.toJson(object);
    }

    String getInfo(String string)throws IOException{
        if(string.equals("info")){
            StringBuilder stringBuilder=new StringBuilder("");

            stringBuilder.append("Money:");
            stringBuilder.append(getMoney());
            stringBuilder.append("\n");

            stringBuilder.append("Turn:");
            stringBuilder.append(getTurn());
            stringBuilder.append("\n");

            stringBuilder.append(getInfoOfObject(level));
            return stringBuilder.toString();
        }
        if(string.equals("workshops")){
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
        if(usedLevel==-1)throw new NotEnoughItemException();
        workShop.startByLevel(usedLevel);
        ArrayList<Item> neededItems = workShop.getInputItemsByUsedLevel();
        for (Item item : neededItems) {
            try {
                wareHouse.eraseItem(item);
            } catch (NoSuchItemInWarehouseException e) {
                System.out.println("This Shouldn't Happen!");
                e.printStackTrace();
            }
        }
    }

    public void createWorkshops() throws IOException {
        for (int workshopNumber = 0; workshopNumber < 6; workshopNumber++) {
            YaGson yaGson = new YaGson();
            WorkShop workshop = yaGson.fromJson(new FileReader("../../workshop" + workshopNumber + ".json"), WorkShop.class);
            workShops.add(workshop);
        }
    }
    public void addWorkshop(WorkShop workShop){
        workShops.add(workShop);
    }

    void nextTurn() throws WinningMessage {
        turn++;
        map.nextTurn();
        if (helicopter.isTransportationEnds()) {
            distributeItems(helicopter.getItems());
            helicopter.endTransportation();
            helicopter.clear();
        }
        if (truck.isTransportationEnds()) {
            increaseMoney(truck.getValue());
            truck.endTransportation();
            truck.clear();
        }

        for (WorkShop workShop : workShops) {
            if (workShop.haveProduct()) {
                try {
                    distributeItems(workShop.getOutputItemsByUsedLevel());
                    workShop.endProduction();
                } catch (WorkShopNotUsedException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList <Entity> everyThing=new ArrayList<>();
        everyThing.addAll(wareHouse.getItems());
        everyThing.addAll(map.getEntities());
        for(Entity entity:level.getGoalEntities())
            if(everyThing.contains(entity))
                level.entityEarned(entity);
        if(level.checkLevel())
        {
            throw new WinningMessage();
        }
    }

    public Truck getTruck() {
        return truck;
    }

    public Helicopter getHelicopter() {
        return helicopter;
    }

    private void distributeItems(ArrayList<Item> items) {
        for (Item item : items) {
            Cell randomCell = map.getRandomCell();
            item.setCell(randomCell);
            getMap().addEntity(randomCell,item);
        }
    }

    private Object getObject(String type) throws IOException {
         // if(type.equals("cat"))return cat;
         // if(type.equals("dog"))return dog;
        if (type.equals("well")) return well;
        if (type.equals("helicopter")) return helicopter;
        if (type.equals("truck")) return truck;
        if (type.equals("warehouse")) return wareHouse;
        if(type.equals("map"))return map;
        if(type.equals("level"))return level;

        if (type.startsWith("workshop")) {
            int workshopNumber = type.charAt(type.length() - 1) - '0';
            return workShops.get(workshopNumber);
        }
        throw new IOException();
    }

    public int getCatLevel() {
        return catLevel;
    }

    void upgrade(String type) throws IOException, CantUpgradeException, NotEnoughMoneyException {
        if(type.equals("cat")){
            if(catLevel==1)
                throw new CantUpgradeException();
            subtractMoney(Constant.CAT_UPGRADE_COST);
            catLevel=1;
            return ;
        }
        Object object=getObject(type);
        if (!(object instanceof Upgradable)) {
            throw new IOException();
        }
        Upgradable upgradable=(Upgradable)object;
        if(!upgradable.canUpgrade()){
            throw new CantUpgradeException();
        }
        subtractMoney(upgradable.upgradeCost());
        upgradable.upgrade();
    }

    void addAnimal(String type) throws IOException, NotEnoughMoneyException {
        type = type.toLowerCase();
        switch (type) {
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

    void pickup(int x, int y) throws CellDoesNotExistException{
        ArrayList<Item> items = map.getItems(x, y);
        for (Item item : items) {
            try{
                wareHouse.addItem(item);
                map.destroyEntity(x, y, item);
            }catch(NoWarehouseSpaceException ignored) {

            }
        }
    }

    void fillWell() throws NotEnoughMoneyException {
        if(well.getWaterRemaining()==well.getMaxWater())return ;
        subtractMoney(Constant.WELL_FILL_COST + well.getLevel() * Constant.WELL_FILL_COST_PER_LEVEL);
        well.fill();
    }

    void cage(int x, int y) throws CellDoesNotExistException {
        map.cage(x, y);
    }

    void clearTruck() {
        //TODO bayad berizi chiz mizasho too anbar
        truck.clear();
    }

    void clearHelicopter() {
        helicopter.clear();
    }

    void addItemToHelicopter(Item item) throws NoTransporterSpaceException {
        helicopter.addItem(item);
    }

    void addItemToTruck(Item item) throws NoTransporterSpaceException, NoSuchItemInWarehouseException {
        if(!truck.canAddItem(item))
            throw new NoTransporterSpaceException();
        wareHouse.eraseItem(item);
        truck.addItem(item);
    }

    void startTruck() throws StartBusyTransporter, StartEmptyTransporter {
        truck.startTransportation();
    }

    void startHelicopter() throws NotEnoughMoneyException, StartBusyTransporter, StartEmptyTransporter {
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
}
