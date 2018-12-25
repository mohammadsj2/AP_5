package Controller;

import Constant.Constant;
import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Cat;
import Model.Entity.Animal.Dog;
import Model.Entity.Animal.Pet.Chiken;
import Model.Entity.Animal.Pet.Cow;
import Model.Entity.Animal.Pet.Pet;
import Model.Entity.Animal.Pet.Sheep;
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
import Exception.NotEnoughMoneyException;
import Exception.NoWaterException;
import Exception.CellDoesNotExistException;
import Exception.CantUpgradeException;
import Exception.StartBusyProducerException;
import Exception.WorkShopNotUsedException;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Exception.WorkshopDoesntExistException;

public class Controller {
    private static int money,turn;
    private static ArrayList<WorkShop> workShops=new ArrayList<>();
    private static Map map;
    private static Well well;
    private static WareHouse wareHouse;
    private static Level level;
    private static Helicopter helicopter;
    private static Truck truck;
    private static ArrayList<Dog> dogs=new ArrayList<>();
    private static ArrayList<Cat> cats=new ArrayList<>();
    private static ArrayList<Pet> pets=new ArrayList<>();

    private Controller(int goalMoney,ArrayList<Entity> earnedEnitities)
    {
        money=turn=0;
        map=new Map();
        well=new Well();
        level=new Level(goalMoney,earnedEnitities);
        helicopter=new Helicopter();
        truck=new Truck();
        wareHouse=new WareHouse();
    }

    public static int getMoney()
    {
        return money;
    }

    public static int getTurn() {
        return turn;
    }
    public static void subtractMoney(int money2) throws NotEnoughMoneyException
    {
        if(money<money2)
            throw new NotEnoughMoneyException();
        money-=money2;
    }
    private static void increaseMoney(int money2){money+=money2;}
    public static void plant(int x,int y) throws NoWaterException, CellDoesNotExistException
    {
        if(well.getWaterRemaining()==0)
        {
            throw new NoWaterException();
        }
        if(map.plant(x,y))well.liftWater();
    }

    public static void printInfo(Object object)
    {

    }

    public static void startAWorkShop(int index) throws WorkshopDoesntExistException, StartBusyProducerException, WorkShopNotUsedException {
        if(index>=workShops.size())throw new WorkshopDoesntExistException();
        WorkShop workShop=workShops.get(index);
        int usedLevel=workShop.maxLevelCanDoWithItems(wareHouse.getItems());
        workShop.startByLevel(usedLevel);
        ArrayList<Item> neededItems=workShop.getInputItemsByUsedLevel();
        for(Item item:neededItems)
        {
            wareHouse.eraseItem(item);
        }
    }
    public static void createWorkshops() throws IOException
    {
        for(int workshopNumber=0;workshopNumber<6;workshopNumber++)
        {
            Gson gson=new Gson();
            WorkShop workshop=gson.fromJson(new FileReader("../../workshop"+workshopNumber+".json"),WorkShop.class);
            workShops.add(workshop);
        }
    }
    public static void nextTurn() throws CellDoesNotExistException, WorkShopNotUsedException {
        turn++;
        map.nextTurn();
        if(helicopter.isTransportationEnds())
        {
            distributeItems(helicopter.getItems());
            helicopter.endTransportation();
            helicopter.clear();
        }
        if(truck.isTransportationEnds())
        {
            increaseMoney(truck.getMoney());
            truck.endTransportation();
            truck.clear();
        }
        // nextTurn WorkShop
        for(WorkShop workShop:workShops)
        {
            if(workShop.haveProduct())
            {
                distributeItems(workShop.getOutputItemsByUsedLevel());
                workShop.endProduction();
            }
        }

    }

    private static void distributeItems(ArrayList<Item> items)
    {
        for(Item item:items)
        {
            Cell randomCell=map.getRandomCell();
            randomCell.addEntity(item);
        }
    }

    public static Object getObject(String type) throws IOException
    {
        //  if(type.equals("cat"))return cat;
        //  if(type.equals("dog"))return dog;
        if(type.equals("well"))return well;
        if(type.equals("helicopter"))return helicopter;
        if(type.equals("truck"))return truck;
        if(type.equals("warehouse"))return wareHouse;
        if(type.startsWith("workshop"))
        {
            int workshopNumber=type.charAt(type.length() - 1)-'0';
            return workShops.get(workshopNumber);
        }
        throw new IOException();
    }

    public static void upgrade(Object object) throws IOException, CantUpgradeException, NotEnoughMoneyException {
        if(!(object instanceof Upgradable))
        {
            throw new IOException();
        }
        subtractMoney(((Upgradable) object).upgradeCost());
        ((Upgradable) object).upgrade();
    }
    public static void addAnimal(Object object) throws IOException, NotEnoughMoneyException {
        if(!(object instanceof Animal))
        {
            throw new IOException();
        }
        if(object instanceof Dog)
        {
            subtractMoney(Constant.DOG_ADD_COST);
            dogs.add(new Dog(map.getRandomCell()));
        }
        if(object instanceof Cat)
        {
            subtractMoney(Constant.CAT_ADD_COST);
            cats.add(new Cat(map.getRandomCell()));
        }
        if(object instanceof Cow)
        {
            subtractMoney(Constant.COW_ADD_COST);
            pets.add(new Cow(map.getRandomCell()));
        }
        if(object instanceof Sheep)
        {
            subtractMoney(Constant.SHEEP_ADD_COST);
            pets.add(new Sheep(map.getRandomCell()));
        }
        if(object instanceof Chiken)
        {
            subtractMoney(Constant.CHICKEN_ADD_COST);
            pets.add(new Chiken(map.getRandomCell()));
        }

    }
    public static void pickup(int x,int y) throws CellDoesNotExistException
    {
        ArrayList<Item> items=map.getItems(x,y);
        for(Item item:items)
        {
            if(wareHouse.addItem(item))
            {
                map.destroyEntity(x,y,item);
            }
        }
    }
    public static void fillWell() throws NotEnoughMoneyException
    {
        subtractMoney(Constant.WELL_FILL_COST+well.getLevel()*Constant.WELL_FILL_COST_PER_LEVEL);
        well.fill();
    }
    public static void cage(int x,int y) throws CellDoesNotExistException {
        map.cage(x,y);
    }
    public static void clearTruck()
    {
        truck.clear();
    }
    public static void clearHelicopter()
    {
        helicopter.clear();
    }
    public static void addItemToHelicopter(Item item)
    {
        helicopter.addItem(item);
    }
    public static void addItemToTruck(Item item)
    {
        if(truck.addItem(item))
        {
            wareHouse.eraseItem(item);
        }
    }

    public static void startTruck()
    {
        truck.startTransportation();
    }

    public static void startHelicopter() throws NotEnoughMoneyException {
        subtractMoney(helicopter.getMoney());
        helicopter.startTransportation();
    }

    public static Map getMap() {
        return map;
    }

    public static WareHouse getWareHouse() {
        return wareHouse;
    }
}
