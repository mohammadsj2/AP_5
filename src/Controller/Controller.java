package Controller;

import Constant.Constant;
import Model.Entity.Animal.Cat;
import Model.Entity.Animal.Dog;
import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Cow;
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
import Exception.StartBusyTransporter;
import com.gilecode.yagson.YaGson;
import Exception.WinningMessage;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Exception.WorkshopDoesntExistException;

public class Controller {
    private int money, turn;
    private ArrayList<WorkShop> workShops = new ArrayList<>();
    private Map map;
    private Well well;
    private WareHouse wareHouse;
    private Level level;
    private Helicopter helicopter;
    private Truck truck;


    Controller(int goalMoney, ArrayList<Entity> earnedEnitities) {
        money = turn = 0;
        map = new Map();
        well = new Well();
        level = new Level(goalMoney, earnedEnitities);
        helicopter = new Helicopter();
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
    }

    void setMoney(int money) {
        this.money = money;
    }

    public void increaseMoney(int money2) {
        money += money2;
    }

    public void plant(int x, int y) throws NoWaterException, CellDoesNotExistException {
        if (well.getWaterRemaining() == 0) {
            throw new NoWaterException();
        }
        if (map.plant(x, y)) well.liftWater();
    }

    private String getInfoOfObject(Object object) {
        YaGson yaGson=new YaGson();
        return yaGson.toJson(object);
    }

    public String getInfo(String string)throws IOException{
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
        if(string.equals("workShops")){
            return getInfoOfObject(workShops);
        }
        return getInfoOfObject(getObject(string));
    }


    public void startAWorkShop(int index) throws WorkshopDoesntExistException, StartBusyProducerException, WorkShopNotUsedException {
        if (index >= workShops.size()) throw new WorkshopDoesntExistException();
        WorkShop workShop = workShops.get(index);
        int usedLevel = workShop.maxLevelCanDoWithItems(wareHouse.getItems());
        workShop.startByLevel(usedLevel);
        ArrayList<Item> neededItems = workShop.getInputItemsByUsedLevel();
        for (Item item : neededItems) {
            wareHouse.eraseItem(item);
        }
    }

    public void createWorkshops() throws IOException {
        for (int workshopNumber = 0; workshopNumber < 6; workshopNumber++) {
            YaGson yaGson = new YaGson();
            WorkShop workshop = yaGson.fromJson(new FileReader("../../workshop" + workshopNumber + ".json"), WorkShop.class);
            workShops.add(workshop);
        }
    }

    //TODO chera bayad nextTurn exception bede ??!!!
    public void nextTurn() throws CellDoesNotExistException, WorkShopNotUsedException, WinningMessage {
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
                distributeItems(workShop.getOutputItemsByUsedLevel());
                workShop.endProduction();
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
            randomCell.addEntity(item);
        }
    }

    public Object getObject(String type) throws IOException {
        //  if(type.equals("cat"))return cat;
        //  if(type.equals("dog"))return dog;
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

    public void upgrade(Object object) throws IOException, CantUpgradeException, NotEnoughMoneyException {
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

    public void addAnimal(String type) throws IOException, NotEnoughMoneyException {
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
            default:
                throw new IOException();
        }

    }

    public void pickup(int x, int y) throws CellDoesNotExistException {
        ArrayList<Item> items = map.getItems(x, y);
        for (Item item : items) {
            if (wareHouse.addItem(item)) {
                map.destroyEntity(x, y, item);
            }
        }
    }

    public void fillWell() throws NotEnoughMoneyException {
        subtractMoney(Constant.WELL_FILL_COST + well.getLevel() * Constant.WELL_FILL_COST_PER_LEVEL);
        well.fill();
    }

    public void cage(int x, int y) throws CellDoesNotExistException {
        map.cage(x, y);
    }

    public void clearTruck() {
        truck.clear();
    }

    public void clearHelicopter() {
        helicopter.clear();
    }

    public void addItemToHelicopter(Item item) {
        helicopter.addItem(item);
    }

    public void addItemToTruck(Item item) {
        if (truck.addItem(item)) {
            wareHouse.eraseItem(item);
        }
    }

    public void startTruck() throws StartBusyTransporter {
        truck.startTransportation();
    }

    public void startHelicopter() throws NotEnoughMoneyException, StartBusyTransporter {
        subtractMoney(helicopter.getValue());
        helicopter.startTransportation();
    }

    public Map getMap() {
        return map;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }
}
