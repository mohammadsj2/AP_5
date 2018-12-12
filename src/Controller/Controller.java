package Controller;

import Constant.Constant;
import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Cat;
import Model.Entity.Animal.Dog;
import Model.Entity.Animal.Pet.Chiken;
import Model.Entity.Animal.Pet.Cow;
import Model.Entity.Animal.Pet.Pet;
import Model.Entity.Animal.Pet.Sheep;
import Model.Entity.Item;
import Model.Map.Map;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import Model.Upgradable;
import Model.WareHouse;
import Model.Well;
import Model.WorkShop;
import Exception.NotEnoughMoneyException;
import Exception.NoWaterException;
import Exception.CellDoesNotExist;
import Exception.CantUpgrade;

import java.io.IOException;
import java.util.ArrayList;

public enum Controller {
    C;
    private int money,turn;
    private ArrayList<WorkShop> workShops=new ArrayList<>();
    private Map map;
    private Well well;
    private WareHouse wareHouse;
    private Level level;
    private Helicopter helicopter;
    private Truck truck;
    private ArrayList<Dog> dogs=new ArrayList<>();
    private ArrayList<Cat> cats=new ArrayList<>();
    private ArrayList<Pet> pets=new ArrayList<>();

    private Controller()
    {
        money=turn=0;
        map=new Map();
        well=new Well();
        level=new Level();
        helicopter=new Helicopter();
        truck=new Truck();
    }

    public int getMoney()
    {
        return money;
    }

    public int getTurn() {
        return turn;
    }
    private void subtractMoney(int money) throws NotEnoughMoneyException
    {
        if(this.money<money)
            throw new NotEnoughMoneyException();
        this.money-=money;
    }
    private void increaseMoney(int money){this.money+=money;}
    public void plant(int x,int y) throws NoWaterException, CellDoesNotExist
    {
        if(well.getWaterRemaining()==0)
        {
            throw new NoWaterException();
        }
        if(map.plant(x,y))well.liftWater();
    }
    public void createWorkshops()
    {

    }
    public void nextTurn()
    {
        map.nextTurn();
        if(helicopter.isTransportationEnds())
        {
            ArrayList<Item> items=helicopter.getItems();
            for(Item item:items)
            {
                wareHouse.addItem(item);
            }
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
    }

    public Object getObject(String type) throws IOException
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

    public void upgrade(Object object) throws IOException, CantUpgrade, NotEnoughMoneyException {
        if(!(object instanceof Upgradable))
        {
            throw new IOException();
        }
        subtractMoney(((Upgradable) object).upgradeCost());
        ((Upgradable) object).upgrade();
    }
    public void addAnimal(Object object) throws IOException, NotEnoughMoneyException {
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
    public void pickup(int x,int y) throws CellDoesNotExist
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
    public void startAWorkShop(String name)
    {

    }
    public void fillWell() throws NotEnoughMoneyException
    {
        subtractMoney(Constant.WELL_FILL_COST+well.getLevel()*Constant.WELL_FILL_COST_PER_LEVEL);
        well.fill();
    }
    public void printInfo(String type)
    {

    }
    public void cage(int x,int y)
    {

    }
    public void clearTruck()
    {

    }
    public void clearHelicopter()
    {

    }
    public void addItemToHelicopter(String type,int count)
    {

    }
    public void addItemToTruck(String type,int count)
    {

    }
}
