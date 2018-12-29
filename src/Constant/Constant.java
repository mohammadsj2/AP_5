package Constant;

import Model.Entity.Item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Constant {
    public static final int TERM_OF_DESTROY_ITEM_IN_MAP = 30;
    public static final int MAP_ROWS = 100;
    public static final int MAP_COLUMNS = 101;
    public static final int MAX_WORKSHOP_LEVEL = 5;
    public static final int WORKSHOP_UPGRADE_COST_PER_LEVEL = 500;
    public static final int ANIMAL_SPEED = 4;
    public static final int INIT_HEALTH=10;

    public static final String BEAR_NAME = "Bear";
    public static final int BEAR_VOLUME = 20;
    public static final int BEAR_COST = 80;

    public static final String LION_NAME = "Lion";
    public static final int LION_VOLUME = 20;
    public static final int LION_COST = 150;

    public static final String DOG_NAME = "Dog";
    public static final String CAT_NAME = "Cat";
    public static final String COW_NAME = "Cow";
    public static final String SHEEP_NAME = "Sheep";
    public static final String CHICKEN_NAME = "Chicken";

    public static final int DOG_ADD_COST = 2600;
    public static final int CAT_ADD_COST = 2500;
    public static final int COW_ADD_COST = 10000;
    public static final int SHEEP_ADD_COST = 5000;
    public static final int CHICKEN_ADD_COST = 100;

    public static final int WAREHOUSE_CAPACITY = 50;
    public static final int WAREHOUSE_CAPACITY_PER_LEVEL = 150;
    public static final int WAREHOUSE_UPGRADE_COST_PER_LEVEL = 2000;
    public static final int WAREHOUSE_MAX_LEVEL = 3;

    public static final int WELL_BASE_WATER = 5;
    public static final int WELL_WATER_PER_LEVEL = 10;
    public static final int WELL_FILL_COST = 19;
    public static final int WELL_FILL_COST_PER_LEVEL = -3;
    public static final int WELL_UPGRADE_COST_PER_LEVEL = 700;
    public static final int WELL_MAX_LEVEL = 3;

    public static final int TOWN_DISTANCE=100;
    public static final int TRUCK_SPEED=4;
    public static final int TRUCK_SPEED_PER_LEVEL=2;
    public static final int TRUCK_CAPACITY=25;
    public static final int TRUCK_CAPACITY_PER_LEVEL=25;
    public static final int TRUCK_UPGRADE_COST_PER_LEVEL=3000;
    public static final int TRUCK_MAX_LEVEL = 3;
    public static final int HELICOPTER_SPEED=10;
    public static final int HELICOPTER_SPEED_PER_LEVEL=5;
    public static final int HELICOPTER_CAPACITY=25;
    public static final int HELICOPTER_CAPACITY_PER_LEVEL=25;
    public static final int HELICOPTER_UPGRADE_COST_PER_LEVEL=4000;
    public static final int HELICOPTER_MAX_LEVEL = 3;

    public static Item getItemByType(String type){
        Item item=null;
        type=type.toLowerCase();
        try {
            InputStream inputStream=
            new FileInputStream("C:\\Users\\Sa1378\\Desktop\\Ex1\\AP_5\\AP_5\\Project_Kooft\\src\\Constant\\Items.txt");

            Scanner scanner=new Scanner(inputStream);
            scanner.nextLine();
            String[] input;
            while(scanner.hasNextLine()){
                input=scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", " ").split(" ");
                if(!input[0].equals(type)){
                    continue;
                }
                item=new Item(input[0],new Integer(input[1]),new Integer(input[2]),0);
                return item;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
