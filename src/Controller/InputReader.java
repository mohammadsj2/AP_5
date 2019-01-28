package Controller;

import Exception.*;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Network.Client.Client;
import Network.Server.Server;
import View.Scene.UsernameGetterScene;
import YaGson.*;
import Model.Well;
import Model.WorkShop;
import View.Scene.HelicopterScene;
import View.Scene.MenuScene;
import View.Scene.TruckScene;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

import Constant.Constant;

public class InputReader extends Application
{

    static Controller currentController = null;
    static int indexOfLevel;
    public static Stage primaryStage;
    static YaGson yaGson=new YaGsonBuilder().serializeSpecialFloatingPointValues().setExclusionStrategies(new YaGsonExclusionStrategy()).create();
    private static Client client;
    private static Server server;



    public static void main(String[] args) throws StartBusyTransporter, IOException
    {
        createAllLevels();

        launch(args);
    }

    private static void createAllLevels() {
        createLevel1();
        createLevel2();
        createLevel3();
    }

    private static void createLevel1()
    {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                goalEntities.add("egg");
            }
            WorkShop workShop = getWorkShop("EggPowderPlant");
            workShop.setLocation(0);
            ArrayList<Item> helicopterItems = new ArrayList<>();
            //nmshe Constant.getItemByType ro seda krd chon creatingTurn null mishe
            Controller controller = new Controller(200, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);

            controller.setMoney(120);
            indexOfLevel = 1;
            createLevel(1,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createLevel2()
    {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                goalEntities.add("cake");
            }
            WorkShop workShop = getWorkShop("EggPowderPlant");
            WorkShop workShop2 = getWorkShop("CookieBakery");
            workShop.setLocation(0);
            workShop2.setLocation(1);
            ArrayList<Item> helicopterItems = new ArrayList<>();
            //nmshe Constant.getItemByType ro seda krd chon creatingTurn null mishe
            helicopterItems.add(Constant.getItemByType("egg"));
            Controller controller = new Controller(500, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);

            controller.setMoney(120);
            indexOfLevel = 2;
            createLevel(2,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createLevel3() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                goalEntities.add("flourycake");
            }
            WorkShop workShop = getWorkShop("EggPowderPlant");
            WorkShop workShop2 = getWorkShop("CookieBakery");
            WorkShop workShop3 = getWorkShop("CakeBakery");
            WorkShop workShop4 = getWorkShop("Spinnery");
            WorkShop workShop5 = getWorkShop("WeavingFactory");
            WorkShop workShop6 = getWorkShop("SewingFactory");
            workShop.setLocation(0);
            workShop2.setLocation(1);
            workShop3.setLocation(2);
            workShop4.setLocation(3);
            workShop5.setLocation(4);
            workShop6.setLocation(5);
            ArrayList<Item> helicopterItems = new ArrayList<>();
            //nmshe Constant.getItemByType ro seda krd chon creatingTurn null mishe
            helicopterItems.add(Constant.getItemByType("egg"));
            helicopterItems.add(Constant.getItemByType("flour"));
            helicopterItems.add(Constant.getItemByType("cake"));
            helicopterItems.add(Constant.getItemByType("flourycake"));
            helicopterItems.add(Constant.getItemByType("sewing"));
            helicopterItems.add(Constant.getItemByType("fabric"));
            helicopterItems.add(Constant.getItemByType("adornment"));
            Controller controller = new Controller(35000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 3;
            createLevel(3,controller);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static WorkShop getWorkShop(String s) throws FileNotFoundException {
        return yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/"+s+".json"), WorkShop.class);
    }

    static void createLevel(int indexOfLevel, Controller controller){
        try {
            OutputStream outputStream = new FileOutputStream(("./ResourcesRoot/Levels/level" + indexOfLevel + ".json"));
            Formatter formatter = new Formatter(outputStream);
            formatter.format(yaGson.toJson(controller));
            formatter.flush();
            formatter.close();
            outputStream.close();

            System.out.println("level " + indexOfLevel + " Created!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void startHelicopter() throws NotEnoughMoneyException, StartBusyTransporter, StartEmptyTransporter
    {
        currentController.startHelicopter();
    }

    public static void clearHelicopter()
    {
        currentController.clearHelicopter();
    }

    public static void addItemToHelicopter(Item item) throws NoTransporterSpaceException
    {
        currentController.addItemToHelicopter(item);
    }

    public static void clearTruck()
    {
        currentController.clearTruck();
    }

    public static void startTruck() throws StartBusyTransporter, StartEmptyTransporter
    {
        currentController.startTruck();
    }

    public static void save()
    {
        try
        {
            System.out.println("to Save....");
            OutputStream outputStream = new FileOutputStream(("./ResourcesRoot/Save/save" + indexOfLevel + ".json"));
            Formatter formatter = new Formatter(outputStream);
            formatter.format(yaGson.toJson(currentController));
            formatter.flush();
            formatter.close();
            outputStream.close();
            System.out.println("saved!");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public static void loadLevel(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        currentController = yaGson.fromJson(new FileReader(("./ResourcesRoot/Levels/level" + indexOfLevel + ".json")),
                Controller.class);

    }


    public static void loadSave(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        currentController = yaGson.fromJson(new FileReader(("./ResourcesRoot/Save/save" + indexOfLevel + ".json")),
                Controller.class);
    }


    public static void buy(String type)
    {
        try
        {
            currentController.addAnimal(type);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (NotEnoughMoneyException e)
        {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
        }
    }

    public static void pickup(int x, int y)
    {
        try
        {
            currentController.pickup(x, y);
        } catch (CellDoesNotExistException e)
        {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MESSAGE);
        }
    }

    public static void cage(int x, int y)
    {
        try
        {
            currentController.cage(x, y);
        } catch (CellDoesNotExistException e)
        {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MESSAGE);
        }
    }

    public static void plant(int x, int y)
    {
        try
        {
            currentController.plant(x, y);
        } catch (NoWaterException e)
        {
            System.out.println(Constant.NOT_ENOUGH_WATER_MESSAGE);
        } catch (CellDoesNotExistException e)
        {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MESSAGE);
        }

    }

    public static void fillWell()
    {
        try
        {
            currentController.fillWell();
        } catch (NotEnoughMoneyException e)
        {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
        }
    }

    public static void startWorkshop(int index)
    {
        try
        {
            currentController.startAWorkShop(index);
        } catch (WorkshopDoesntExistException e)
        {
            System.out.println(Constant.WORKSHOP_DOESNT_EXIST_MESSAGE);
        } catch (StartBusyProducerException e)
        {
            System.out.println(Constant.START_BUSY_WORKSPACE_MESSAGE);
        } catch (WorkShopNotUsedException e)
        {
            System.out.println(Constant.WORK_SHOP_NOT_USED_EXCEPTION_MESSAGE);
        } catch (NotEnoughItemException e)
        {
            System.out.println(Constant.NOT_ENOUGH_ITEM_MESSAGE);
        }
    }

    public static void upgrade(String type)
    {
        try
        {
            currentController.upgrade(type);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (CantUpgradeException e)
        {
            System.out.println(Constant.CANT_UPGRADE_MESSAGE);
        } catch (NotEnoughMoneyException e)
        {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
        }
    }

    public static void nextTurn(int id)
    {
        for (int i = 0; i < id; i++)
        {
            try
            {
                currentController.nextTurn();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void addItemToTruck(Item item) throws NoTransporterSpaceException, NoSuchItemInWarehouseException
    {
        currentController.addItemToTruck(item);
    }


    public static Controller getCurrentController()
    {
        return currentController;
    }

    public static void setServer(Server server)
    {
        InputReader.server=server;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        InputReader.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setX(300);
        primaryStage.setY(100);
        primaryStage.show();
        UsernameGetterScene.init();
        setScene(UsernameGetterScene.getScene());
    }

    public static void setScene(Scene scene)
    {
        primaryStage.setScene(scene);
    }

    public static void setClient(Client client) {
        InputReader.client = client;
    }

    public static YaGson getYaGson()
    {
        return yaGson;
    }
}
