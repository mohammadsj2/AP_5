package Controller;

import Exception.*;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Well;
import Model.WorkShop;
import View.Scene.HelicopterScene;
import View.Scene.MenuScene;
import View.Scene.TruckScene;
import com.gilecode.yagson.YaGson;
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


    public static void main(String[] args) throws StartBusyTransporter, IOException
    {

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Scanner scanner = new Scanner(System.in);
                    String[] input;
                    while (true)
                    {
                        input = scanner.nextLine().split(" ");
                        switch (input[0])
                        {
                            case "save":
                                save();
                                break;
                            case "load":
                                if (input[1].equals("level"))
                                    loadLevel(new Integer(input[2]));
                                else if (input[1].equals("save"))
                                    loadSave(new Integer(input[2]));
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
                            case "turn":
                                nextTurn(new Integer(input[1]));
                                break;
                            case "truck":
                                if (input[1].equals("go"))
                                {
                                    startTruck();
                                } else if (input[1].equals("clear"))
                                {
                                    clearTruck();
                                } else
                                {
                                    Item item = Constant.getItemByType(input[2]);
                                    for (int i = 0; i < new Integer(input[3]); i++)
                                    {
                                        try
                                        {
                                            currentController.addItemToTruck(item);
                                        } catch (NoTransporterSpaceException e)
                                        {
                                            System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                                        } catch (NoSuchItemInWarehouseException e)
                                        {
                                            System.out.println(Constant.NO_SUCH_ITEM_MESSAGE);
                                        }
                                    }
                                }
                                break;
                            case "helicopter":
                                if (input[1].equals("go"))
                                {
                                    try
                                    {
                                        startHelicopter();
                                    } catch (NotEnoughMoneyException e)
                                    {
                                        System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
                                    }
                                } else if (input[1].equals("clear"))
                                {
                                    clearHelicopter();
                                } else
                                {
                                    Item item = Constant.getItemByType(input[2]);
                                    for (int i = 0; i < new Integer(input[3]); i++)
                                    {
                                        try
                                        {
                                            addItemToHelicopter(item);
                                        } catch (NoTransporterSpaceException e)
                                        {
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
                                System.out.println(Constant.BAD_INPUT_FORMAT_MESSAGE);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        launch(args);
     /*   YaGson yaGson = new YaGson();
        ArrayList<String> goalEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            goalEntities.add("flourycake");
        }
        WorkShop workShop=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/EggPowderPlant.json"),WorkShop.class);
        WorkShop workShop2=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/CookieBakery.json"),WorkShop.class);
        WorkShop workShop3=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/CakeBakery.json"),WorkShop.class);
        WorkShop workShop4=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/Spinnery.json"),WorkShop.class);
        WorkShop workShop5=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/WeavingFactory.json"),WorkShop.class);
        WorkShop workShop6=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/SewingFactory.json"),WorkShop.class);
        workShop.setLocation(0);
        workShop2.setLocation(1);
        workShop3.setLocation(2);
        workShop4.setLocation(3);
        workShop5.setLocation(4);
        workShop6.setLocation(5);
        ArrayList<Item> helicopterItems=new ArrayList<>();
        //nmshe Constant.getItemByType ro seda krd chon creatingTurn null mishe
        helicopterItems.add(Constant.getItemByTypeForLevel("egg"));
        helicopterItems.add(Constant.getItemByTypeForLevel("flour"));
        helicopterItems.add(Constant.getItemByTypeForLevel("cake"));
        helicopterItems.add(Constant.getItemByTypeForLevel("flourycake"));
        helicopterItems.add(Constant.getItemByTypeForLevel("sewing"));
        helicopterItems.add(Constant.getItemByTypeForLevel("fabric"));
        helicopterItems.add(Constant.getItemByTypeForLevel("adornment"));
        Controller controller = new Controller(0, goalEntities,helicopterItems);
        controller.addWorkshop(workShop);
        controller.addWorkshop(workShop2);
        controller.addWorkshop(workShop3);
        controller.addWorkshop(workShop4);
        controller.addWorkshop(workShop5);
        controller.addWorkshop(workShop6);

        controller.setMoney(99999);
        currentController = controller;
        indexOfLevel=3;
        save();*/
        /*HashMap<String,Integer> map=new HashMap<>();
        map.put("Salam",132);
        System.out.println(yaGson.toJson(map));
        Double a=234.234234;*/

        //      Cell x=new Cell(1,2);
//        System.out.println(yaGson.toJson(x));

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
        YaGson yaGson = new YaGson();
        try
        {
            OutputStream outputStream = new FileOutputStream(("./ResourcesRoot/Save/save" + indexOfLevel + ".json"));
            Formatter formatter = new Formatter(outputStream);
            formatter.format(yaGson.toJson(currentController));

            formatter.flush();
            formatter.close();
            outputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public static void loadLevel(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        YaGson yaGson = new YaGson();
        currentController = yaGson.fromJson(new FileReader(("./ResourcesRoot/Levels/level" + indexOfLevel + ".json")),
                Controller.class);

    }


    public static void loadSave(int levelIndex) throws FileNotFoundException
    {
        indexOfLevel = levelIndex;
        YaGson yaGson = new YaGson();
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

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MenuScene.init(false);
        InputReader.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setX(300);
        primaryStage.setY(100);
        primaryStage.show();
        setScene(MenuScene.getScene());
    }

    public static void setScene(Scene scene)
    {
        primaryStage.setScene(scene);

    }
}
