package Controller;

import Exception.*;
import View.GameScene.GameScene;
import com.gilecode.yagson.YaGson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;

public class InputReader extends Application
{

    public static final String NOT_ENOUGH_MONEY_MESSAGE = "**** Error: Not Enough Money! ****";
    public static final String CELL_DOES_NOT_EXIST_MESSAGE = "**** Error: Cell Does Not Exist! ****";
    public static final String BAD_INPUT_FORMAT_MESSAGE = "**** Error: Bad input Format! ****";
    public static final String NOT_ENOUGH_WATER_MESSAGE = "**** Error: Not enough water! ****";
    public static final String WORKSHOP_DOESNT_EXIST_MESSAGE = "**** Error: Invalid workshop index! ****";
    public static final String START_BUSY_WORKSPACE_EXCEPTION_MESSAGE = "**** Error: workshop is busy! ****";
    public static final String WORK_SHOP_NOT_USED_EXCEPTION_MESSAGE = "**** Error: WorkShopNotUsedException! ****";
    public static final String CANT_UPGRADE_MESSAGE = "**** Error: this objec cant upgrade! ****";
    public static final String THIS_LEVEL_NOT_LOADED_MESSAGE = "**** Error: this level not loaded yet! ****";
    public static final String NO_SUCH_ITEM_MESSAGE = "**** Error: You don't have that item! ****";
    public static final String NOT_ENOUGH_SPACE_MESSAGE = "**** Error: Not enough space! ****";
    static Controller currentController = null;
    static ArrayList<Controller> loadedLevelsControllers = new ArrayList<>();
    static ArrayList<Integer> indexOfLevel = new ArrayList<>();
    static Stage primaryStage;



    public static void main(String[] args) throws StartBusyTransporter, IOException {
        /*
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner scanner = new Scanner(System.in);
                    String[] input;
                    while (true) {
                        input = scanner.nextLine().split(" ");
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
                                    loadLevel(new Integer(input[2]));
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
                                            System.out.println(NOT_ENOUGH_SPACE_MESSAGE);
                                        } catch (NoSuchItemInWarehouseException e) {
                                            System.out.println(NO_SUCH_ITEM_MESSAGE);
                                        }
                                    }
                                }
                                break;
                            case "helicopter":
                                if (input[1].equals("go")) {
                                    try {
                                        currentController.startHelicopter();
                                    } catch (NotEnoughMoneyException e) {
                                        System.out.println(NOT_ENOUGH_MONEY_MESSAGE);
                                    }
                                } else if (input[1].equals("clear")) {
                                    currentController.clearHelicopter();
                                } else {
                                    Item item = Constant.getItemByType(input[2]);
                                    for (int i = 0; i < new Integer(input[3]); i++) {
                                        try {
                                            currentController.addItemToHelicopter(item);
                                        } catch (NoTransporterSpaceException e) {
                                            System.out.println(NOT_ENOUGH_SPACE_MESSAGE);
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
                                System.out.println(BAD_INPUT_FORMAT_MESSAGE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        */
        loadLevel(1);
        runByLevelNumber(1);
        launch(args);

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
            System.out.println(NOT_ENOUGH_MONEY_MESSAGE);
        }
    }

    public static void pickup(int x, int y) {
        try {
            currentController.pickup(x, y);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MESSAGE);
        }
    }

    public static void cage(int x, int y) {
        try {
            currentController.cage(x, y);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MESSAGE);
        }
    }

    public static void plant(int x, int y) {
        try {
            currentController.plant(x, y);
        } catch (NoWaterException e) {
            System.out.println(NOT_ENOUGH_WATER_MESSAGE);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MESSAGE);
        }

    }

    public static void fillWell() {
        try {
            currentController.fillWell();
        } catch (NotEnoughMoneyException e) {
            System.out.println(NOT_ENOUGH_MONEY_MESSAGE);
        }
    }

    public static void startWorkshop(int index) {
        try {
            currentController.startAWorkShop(index);
        } catch (WorkshopDoesntExistException e) {
            System.out.println(WORKSHOP_DOESNT_EXIST_MESSAGE);
        } catch (StartBusyProducerException e) {
            System.out.println(START_BUSY_WORKSPACE_EXCEPTION_MESSAGE);
        } catch (WorkShopNotUsedException e) {
            System.out.println(WORK_SHOP_NOT_USED_EXCEPTION_MESSAGE);
        }
    }

    public static void upgrade(String type) {
        try {
            currentController.upgrade(type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CantUpgradeException e) {
            System.out.println(CANT_UPGRADE_MESSAGE);
        } catch (NotEnoughMoneyException e) {
            System.out.println(NOT_ENOUGH_MONEY_MESSAGE);
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
        System.out.println(THIS_LEVEL_NOT_LOADED_MESSAGE);
    }

    public static void nextTurn(int id) {
        for (int i = 0; i < id; i++) {
            try {
                currentController.nextTurn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Controller getCurrentController() {
        return currentController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        initScenes();
        this.primaryStage=primaryStage;
        primaryStage.show();
        setScene(GameScene.getScene());
    }

    private void initScenes()
    {
        GameScene.init();
    }

    public void setScene(Scene scene)
    {
        primaryStage.setScene(scene);

    }
}
