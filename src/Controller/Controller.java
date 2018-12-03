package Controller;

import Model.Map.Map;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import Model.WareHouse;
import Model.Well;
import Model.WorkShop;

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
}
