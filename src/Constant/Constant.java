package Constant;

import Controller.Controller;
import Controller.InputReader;
import Model.Entity.Item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Constant {


    public static final int[] WORKSHOPS_POSITION_X = {114, 730, 114, 730, 114, 730};
    public static final int[] WORKSHOPS_POSITION_Y = {500, 480, 350, 350, 205, 205};
    public static final int GAME_SCENE_HEIGHT=700;
    public static final int GAME_SCENE_WIDTH=900;

    public static final int TERM_OF_DESTROY_ITEM_IN_MAP = 50;
    public static final int MAP_ROWS = 100;
    public static final int MAP_COLUMNS = 101;
    public static final int MAX_WORKSHOP_LEVEL = 5;
    public static final int WORKSHOP_UPGRADE_COST_PER_LEVEL = 500;
    public static final int ANIMAL_SPEED = 4;
    public static final int INIT_HEALTH=50;

    public static final String BEAR_NAME = "bear";

    public static final String LION_NAME = "lion";

    public static final String DOG_NAME = "dog";
    public static final String CAT_NAME = "cat";
    public static final String COW_NAME = "cow";
    public static final String SHEEP_NAME = "sheep";
    public static final String CHICKEN_NAME = "chicken";

    public static final int DOG_ADD_COST = 2600;
    public static final int CAT_ADD_COST = 2500;
    public static final int COW_ADD_COST = 10000;
    public static final int SHEEP_ADD_COST = 5000;
    public static final int CHICKEN_ADD_COST = 100;

    public static final int CAT_UPGRADE_COST = 2000;

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

    public static final String NOT_ENOUGH_MONEY_MASSEGE = "**** Error: Not Enough Money! ****";
    public static final String WORK_SHOP_NOT_USED_EXCEPTION_MASSEGE = "**** Error: WorkShopNotUsedException! ****";
    public static final String CELL_DOES_NOT_EXIST_MASSEGE = "**** Error: Cell Does Not Exist! ****";
    public static final String BAD_INPUT_FORMAT_MASSEGE = "**** Error: Bad input Format! ****";
    public static final String NOT_ENOUGH_WATER_MASSEGE = "**** Error: Not enough water! ****";
    public static final String WORKSHOP_DOESNT_EXIST_MASSEGE = "**** Error: Invalid workshop index! ****";
    public static final String START_BUSY_WORKSPACE_EXCEPTION_MASSEGE = "**** Error: workshop is busy! ****";
    public static final String CANT_UPGRADE_MASSEGE = "**** Error: this objec cant upgrade! ****";
    public static final String THIS_LEVEL_NOT_LOADED_MASSEGE = "**** Error: this level not loaded yet! ****";
    public static final String NO_SUCH_ITEM_MESSAGE = "**** Error: You don't have that item! ****";
    public static final String NOT_ENOUGH_SPACE_MESSAGE = "**** Error: Not enough space! ****";
    public static final String CURRENT_CONTROLLER_NOT_EXIST_MASSEGE = "**** Error: Please run a Controller first! ****";
    public static final String WIN_LEVEL_MASSEGE = "*** Congratulation: bordi !! ***";

    public static Item getItemByType(String type){
        type=type.toLowerCase();
        try {
            InputStream inputStream= new FileInputStream("./src/Constant/Items.txt");

            Scanner scanner=new Scanner(inputStream);
            scanner.nextLine();
            String[] input;
            while(scanner.hasNextLine()){
                input=scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", " ").split(" ");
                if(!input[0].equals(type)){
                    continue;
                }
                return new Item(input[0],new Integer(input[1]),new Integer(input[2]), InputReader.getCurrentController().getTurn());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
