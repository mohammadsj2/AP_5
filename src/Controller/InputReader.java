package Controller;

import com.gilecode.yagson.YaGson;

import java.io.*;
import java.util.Formatter;
import java.util.Scanner;
import Exception.*;

public class InputReader {
    public static final String NOT_ENOUGH_MONEY_MASSEGE = "**** Error: Not Enough Money! ****";
    public static final String CELL_DOES_NOT_EXIST_MASSEGE = "**** Error: Cell Does Not Exist! ****";
    public static final String BAD_INPUT_FORMAT_MASSEGE = "**** Error: Bad input Format! ****";
    public static final String NOT_ENOUGH_WATER_MASSEGE = "**** Error: Not enough water! ****";
    public static final String WORKSHOP_DOESNT_EXIST_MASSEGE = "**** Error: Invalid workshop index! ****";
    public static final String START_BUSY_WORKSPACE_EXCEPTION_MASSEGE = "**** Error: workshop is busy! ****";
    public static final String WORK_SHOP_NOT_USED_EXCEPTION_MASSEGE = "**** Error: WorkShopNotUsedException! ****";
    static Controller currentController = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] input;
        while (true) {
            input = scanner.nextLine().split(" ");
            //TODO kamel nist
            switch (input[0]) {
                case "save":
                    save(input[1]);
                    break;
                case "load":
                    load(input[1]);
                    break;
                case "buy":
                    buy(input[1]);
                    break;
                case "pickup":
                    pickup(new Integer(input[1]),new Integer(input[2]));
                    break;
                case "cage":
                    cage(new Integer(input[1]),new Integer(input[2]));
                    break;
                case "plant":
                    plant(new Integer(input[1]),new Integer(input[2]));
                    break;
                case "well":
                    fillWell();
                case "start":
                    startWorkshop(new Integer(input[1]));
                default:
                    System.out.println(BAD_INPUT_FORMAT_MASSEGE);
            }
        }
    }

    public static void save(String saveName) {
        YaGson yaGson = new YaGson();
        try {
            OutputStream outputStream = new FileOutputStream(("./ResourcesRoot/Save/" + saveName + ".txt"));
            Formatter formatter = new Formatter(outputStream);
            formatter.format(yaGson.toJson(currentController));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String saveName) {
        YaGson yaGson = new YaGson();
        try {
            currentController=yaGson.fromJson(new FileReader(("./ResourcesRoot/Save/" + saveName + ".txt")),Controller.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void buy(String type){
        try {
            currentController.addAnimal(type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotEnoughMoneyException e) {
            System.out.println(NOT_ENOUGH_MONEY_MASSEGE);
        }
    }

    public static void pickup(int x,int y){
        try {
            currentController.pickup(x,y);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MASSEGE);
        }
    }

    public static void cage(int x,int y){
        try {
            currentController.cage(x,y);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MASSEGE);
        }
    }

    public static void plant(int x,int y){
        try {
            currentController.plant(x,y);
        } catch (NoWaterException e) {
            System.out.println(NOT_ENOUGH_WATER_MASSEGE);
        } catch (CellDoesNotExistException e) {
            System.out.println(CELL_DOES_NOT_EXIST_MASSEGE);
        }

    }

    public static void fillWell(){
        try {
            currentController.fillWell();
        } catch (NotEnoughMoneyException e) {
            System.out.println(NOT_ENOUGH_MONEY_MASSEGE);
        }
    }

    public static void startWorkshop(int index){
        try {
            currentController.startAWorkShop(index);
        } catch (WorkshopDoesntExistException e) {
            System.out.println(WORKSHOP_DOESNT_EXIST_MASSEGE);
        } catch (StartBusyProducerException e) {
            System.out.println(START_BUSY_WORKSPACE_EXCEPTION_MASSEGE);
        } catch (WorkShopNotUsedException e) {
            System.out.println(WORK_SHOP_NOT_USED_EXCEPTION_MASSEGE);
        }
    }


    public static Controller getCurrentController() {
        return currentController;
    }
}
