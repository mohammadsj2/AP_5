package Controller;

import Model.Entity.Animal.Cat;
import Model.Entity.Animal.Dog;
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

public class Controller {
    private int money,turn;
    private ArrayList<WorkShop> workShops=new ArrayList<>();
    private Map map;
    private Well well;
    private WareHouse wareHouse;
    private Level level;
    private Helicopter helicopter;
    private Truck truck;
    private boolean hasDog,hasCat;
    private Dog dog;
    private Cat cat;

    public Controller()
    {
        money=turn=0;
        map=new Map();
        well=new Well();
        level=new Level();
        helicopter=new Helicopter();
        truck=new Truck();
        hasDog=hasCat=false;
    }

    public int getTurn() {
        return turn;
    }
    public void subtractMoney(int money) throws NotEnoughMoneyException
    {
        if(this.money<money)
            throw new NotEnoughMoneyException();
        this.money-=money;
    }
    public void increaseMoney(int money){this.money+=money;}
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

    public void upgrade(Object object) throws IOException, CantUpgrade, NotEnoughMoneyException {
        if(!(object instanceof Upgradable))
        {
            throw new IOException();
        }
        subtractMoney(((Upgradable) object).upgradeCost());
        ((Upgradable) object).upgrade();
    }
    public void addAnimal(String type)
    {

    }
    public void pickup(int x,int y)
    {

    }
    public void startAWorkShop(String name)
    {

    }
    public void fillWell()
    {

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
