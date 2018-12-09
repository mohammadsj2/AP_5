package Controller;

import Model.Map.Map;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import Model.WareHouse;
import Model.Well;
import Model.WorkShop;
import Exception.NotEnoughMoneyException;

import java.util.ArrayList;

public class Controller {
    static int money,turn;
    static ArrayList<WorkShop> workShops=new ArrayList<>();
    static ArrayList<WareHouse> wareHouses=new ArrayList<>();
    static Map map;
    static Well well;
    static Level level;
    static Helicopter helicopter;
    static Truck truck;

    public static int getTurn() {
        return turn;
    }
    public void subtractMoney(int money) throws NotEnoughMoneyException
    {
        if(this.money<money)
            throw new NotEnoughMoneyException();
        this.money-=money;
    }
    public void increaseMoney(int money){this.money+=money;}
    public void plant(int x,int y)
    {
        if(well.getWaterRemaining()==0);
        boolean didPlant=false;

    }
    public void createWorkshops()
    {

    }
    public void nextTurn(int x)
    {

    }
    public void preProcess()
    {

    }
}
