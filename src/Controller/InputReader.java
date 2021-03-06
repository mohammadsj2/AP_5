package Controller;

import Exception.*;
import Model.Entity.Item;
import Network.Client.Client;

import Network.Server.Server;
import View.Scene.GameScene;
import View.Scene.MultiPlayerScene.ChatroomScene;
import View.Scene.UsernameGetterScene;
import YaGson.*;
import Model.WorkShop;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;

import Constant.Constant;

public class InputReader extends Application
{

    static Controller currentController = null;
    public static int indexOfLevel;
    public static Stage primaryStage;
    private static YaGson yaGson=new YaGsonBuilder().serializeSpecialFloatingPointValues().setExclusionStrategies(new YaGsonExclusionStrategy()).create();
    private static Client client;
    private static Server server;
    private static MediaView mediaView;



    public static void main(String[] args) throws StartBusyTransporter, IOException
    {
        createAllLevels();
        System.out.println(getIp());
        System.out.println();
        startSound();

        launch(args);
    }

    public static MediaView getMediaView() {
        return mediaView;
    }

    private static void startSound() {

        Media media = new Media(new File("Textures/Sound/sound2.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        mediaView=new MediaView(mediaPlayer);
        System.out.println("ok sounds");
    }



    public static WorkShop getWorkShop(String s) throws FileNotFoundException {
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
    public static void runAController(Controller controller,boolean force){
        setCurrentController(controller);
        GameScene.init();
        if(force){
            InputReader.setScene(GameScene.getScene());
        }else{
            Platform.runLater(() -> InputReader.setScene(GameScene.getScene()));
        }
    }
    public static void setCurrentController(Controller controller){
        currentController=controller;
        if(InputReader.getClient()!=null) {
            InputReader.getClient().setMoney(currentController.getMoney());
            if (InputReader.getClient().isOnline()) InputReader.getClient().updateClient();
        }
    }

    public static void loadLevel(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        setCurrentController(yaGson.fromJson(new FileReader(("./ResourcesRoot/Levels/level" + indexOfLevel + ".json")),
                Controller.class));
    }


    public static void loadSave(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        setCurrentController(yaGson.fromJson(new FileReader(("./ResourcesRoot/Save/save" + indexOfLevel + ".json")),
                Controller.class));
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

    public static Scene getScene() {
        return primaryStage.getScene();
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("FarmFrenzy");
        InputReader.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setX(300);
        primaryStage.setY(100);
        primaryStage.setOnCloseRequest(event -> {
            if(client!=null && client.isOnline()){
                client.disconnect();
                System.exit(0);
            }
            Media media=new Media(new File("Textures/Sound/khodahafez.wav").toURI().toString());
            MediaPlayer mediaPlayer=new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                InputReader.primaryStage.close();
                System.exit(0);
            });
            event.consume();
        });
        primaryStage.show();

        ChatroomScene.CHATROOM_SCENE.init();
        UsernameGetterScene.init();
        setScene(UsernameGetterScene.getScene());
    }

    public static void setScene(Scene scene)
    {
        primaryStage.setScene(scene);
    }

    public static Client getClient() {
        return client;
    }


    public static void setClient(Client client) {

        InputReader.client = client;
    }

    public static boolean isServer()
    {
        return server!=null;
    }

    public static Server getServer(){return server;}

    public static String getIp() {
        try {
//            System.out.println("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements();)
            {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements();)
                {
                    InetAddress addr = a.nextElement();
                    String address = addr.getHostAddress();
                    if (address.startsWith("192.168.1")) {
                        return address;
                    }
//                    System.out.println("  " + addr.getHostAddress());
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Exception";
        }
    }
    private static void createAllLevels() {
        createLevel1();
        createLevel2();
        createLevel3();
        createLevel4();
        createLevel5();
        createLevel6();
        createLevel7();
        createLevel8();
        createLevel9();
        createLevel10();
        System.out.println("");
    }

    private static void createLevel1() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                goalEntities.add("egg");
            }
            WorkShop workShop = getWorkShop("EggPowderPlant");
            workShop.setLocation(0);
            ArrayList<Item> helicopterItems = new ArrayList<>();
            //nmshe Constant.getItemByType ro seda krd chon creatingTurn null mishe
            Controller controller = new Controller(1,200, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);

            controller.setMoney(120);
            indexOfLevel = 1;
            createLevel(1,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel2() {
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
            Controller controller = new Controller(2,500, goalEntities, helicopterItems);
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
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
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
    private static void createLevel4() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                goalEntities.add("milk");
                goalEntities.add("egg");
                goalEntities.add("fabric");
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

            helicopterItems.add(Constant.getItemByType("egg"));
            helicopterItems.add(Constant.getItemByType("flour"));
            helicopterItems.add(Constant.getItemByType("cake"));
            helicopterItems.add(Constant.getItemByType("fabric"));
            helicopterItems.add(Constant.getItemByType("adornment"));
            Controller controller = new Controller(3,2000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 4;
            createLevel(4,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel5() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 5;
            createLevel(5,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel6() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 6;
            createLevel(6,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel7() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 7;
            createLevel(7,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel8() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 8;
            createLevel(8,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel9() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 9;
            createLevel(9,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createLevel10() {
        try {
            ArrayList<String> goalEntities = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
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
            Controller controller = new Controller(3,30000, goalEntities, helicopterItems);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);

            controller.setMoney(99999);
            indexOfLevel = 10;
            createLevel(10,controller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
