package Controller;

import Constant.Constant;
import Model.Entity.Item;
import com.gilecode.yagson.YaGson;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

import Exception.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InputReader extends Application {
    public static Stage primaryStage;
    static Controller currentController = null;
    static ArrayList<Controller> loadedLevelsControllers = new ArrayList<>();
    static ArrayList<Integer> indexOfLevel = new ArrayList<>();


    public static void main(String[] args) throws StartBusyTransporter, IOException {
        launch(args);
        Scanner scanner = new Scanner(System.in);
        String[] input;
        while (true) {
            input = scanner.nextLine().split(" ");
            try {
                switch (input[0]) {
                    case "save":
                        if (input[1].equals("game")) {
                            save(input[2]);
                        }
                        break;
                    case "load":
                        if (input[1].equals("game")) {
                            load(input[2]);
                        } else {
                            loadLevel(new Integer(input[1]));
                        }
                        break;
                    case "buy":
                        buy(input[1]);
                        break;
                    case "pickup":
                        pickup(new Integer(input[1]), new Integer(input[2]));
                        break;
                    case "cage":
                        cage(new Integer(input[1]), new Integer(input[2]));
                        break;
                    case "plant":
                        plant(new Integer(input[1]), new Integer(input[2]));
                        break;
                    case "well":
                        fillWell();
                        break;
                    case "start":
                        startWorkshop(new Integer(input[1]));
                        break;
                    case "upgrade":
                        upgrade(input[1]);
                        break;
                    case "run":
                        runByLevelNumber(new Integer(input[1]));
                        break;
                    case "turn":
                        nextTurn(new Integer(input[1]));
                        break;
                    case "truck":
                        if (input[1].equals("go")) {
                            currentController.startTruck();
                        } else if (input[1].equals("clear")) {
                            currentController.clearTruck();
                        } else {
                            Item item = Constant.getItemByType(input[2]);
                            for (int i = 0; i < new Integer(input[3]); i++) {
                                try {
                                    currentController.addItemToTruck(item);
                                } catch (NoTransporterSpaceException e) {
                                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                                } catch (NoSuchItemInWarehouseException e) {
                                    System.out.println(Constant.NO_SUCH_ITEM_MESSAGE);
                                }
                            }
                        }
                        break;
                    case "helicopter":
                        if (input[1].equals("go")) {
                            try {
                                currentController.startHelicopter();
                            } catch (NotEnoughMoneyException e) {
                                System.out.println(Constant.NOT_ENOUGH_MONEY_MASSEGE);
                            }
                        } else if (input[1].equals("clear")) {
                            currentController.clearHelicopter();
                        } else {
                            Item item = Constant.getItemByType(input[2]);
                            for (int i = 0; i < new Integer(input[3]); i++) {
                                try {
                                    currentController.addItemToHelicopter(item);
                                } catch (NoTransporterSpaceException e) {
                                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                                }
                            }
                        }
                        break;
                    case "print":
                        System.out.println(currentController.getInfo(input[1]));
                        break;
                    case "cheat":
                        currentController.increaseMoney(1000);
                        break;
                    default:
                        System.out.println(Constant.BAD_INPUT_FORMAT_MASSEGE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void save(String saveName) {
        YaGson yaGson = new YaGson();
        try {
            OutputStream outputStream = new FileOutputStream(("./ResourcesRoot/Save/" + saveName + ".json"));
            Formatter formatter = new Formatter(outputStream);
            formatter.format(yaGson.toJson(currentController));

            formatter.flush();
            formatter.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String saveName) {
        YaGson yaGson = new YaGson();
        try {
            currentController = yaGson.fromJson(new FileReader(("./ResourcesRoot/Save/" + saveName + ".json")), Controller.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadLevel(int levelNumber) {
        YaGson yaGson = new YaGson();
        try {
            loadedLevelsControllers.add(yaGson.fromJson(
                    new FileReader(("./ResourcesRoot/Levels/level" + levelNumber + ".json")), Controller.class));
            indexOfLevel.add(levelNumber);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void buy(String type) {
        try {
            currentController.addAnimal(type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotEnoughMoneyException e) {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MASSEGE);
        } catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }
    }

    public static void pickup(int x, int y) {
        try {
            currentController.pickup(x, y);
        } catch (CellDoesNotExistException e) {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MASSEGE);
        }catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }
    }

    public static void cage(int x, int y) {
        try {
            currentController.cage(x, y);
        } catch (CellDoesNotExistException e) {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MASSEGE);
        }catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }
    }

    public static void plant(int x, int y) {
        try {
            currentController.plant(x, y);
        } catch (NoWaterException e) {
            System.out.println(Constant.NOT_ENOUGH_WATER_MASSEGE);
        } catch (CellDoesNotExistException e) {
            System.out.println(Constant.CELL_DOES_NOT_EXIST_MASSEGE);
        }catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }

    }

    public static void fillWell() {
        try {
            currentController.fillWell();
        } catch (NotEnoughMoneyException e) {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MASSEGE);
        }catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }
    }

    public static void startWorkshop(int index) {
        try {
            currentController.startAWorkShop(index);
        } catch (WorkshopDoesntExistException e) {
            System.out.println(Constant.WORKSHOP_DOESNT_EXIST_MASSEGE);
        } catch (StartBusyProducerException e) {
            System.out.println(Constant.START_BUSY_WORKSPACE_EXCEPTION_MASSEGE);
        } catch (WorkShopNotUsedException e) {
            System.out.println(Constant.WORK_SHOP_NOT_USED_EXCEPTION_MASSEGE);
        }catch (NullPointerException e){
            System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
        }
    }

    public static void upgrade(String type) {
        try {
            currentController.upgrade(type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CantUpgradeException e) {
            System.out.println(Constant.CANT_UPGRADE_MASSEGE);
        } catch (NotEnoughMoneyException e) {
            System.out.println(Constant.NOT_ENOUGH_MONEY_MASSEGE);
        }catch (NullPointerException e){
            if(currentController==null)
                System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
            else
                e.printStackTrace();
        }
    }

    public static void runByLevelNumber(int id) {
        for (int i = 0; i < loadedLevelsControllers.size(); i++) {
            Controller controller = loadedLevelsControllers.get(i);
            int index = indexOfLevel.get(i);
            if (index == id) {
                currentController = controller;
                return;
            }
        }
        System.out.println(Constant.THIS_LEVEL_NOT_LOADED_MASSEGE);
    }

    public static void nextTurn(int count) {
        for (int i = 0; i < count; i++) {
            try {
                currentController.nextTurn();
            } catch (WinningMessage e) {
                System.out.println(Constant.WIN_LEVEL_MASSEGE);
            }catch (NullPointerException e){
                if(currentController==null) {
                    System.out.println(Constant.CURRENT_CONTROLLER_NOT_EXIST_MASSEGE);
                }else{
                    e.printStackTrace();
                }
            }
        }
    }


    public static Controller getCurrentController() {
        return currentController;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Hello World");
        Group root=new Group();
        Scene scene=new Scene(root,1000,800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
