package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import Exception.*;
import View.Button;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class TruckScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static ArrayList<Item> items=Constant.getAllPossibleItems();
    public static void init(){
        try {
            initBackground();
            initItems();
            initBackButton();
            initGoButton();
            initClearButton();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initClearButton() {
        Button clearButton=new Button("Clear",40,90, 580,640);
        addNode(clearButton.getNode());
        clearButton.getNode().setOnMouseClicked(event -> {
            InputReader.clearTruck();
        });
    }

    private static void initGoButton() {
        Button goButton=new Button("Go",40,90, 440,640);
        addNode(goButton.getNode());
        goButton.getNode().setOnMouseClicked(event -> {
            try {
                InputReader.startTruck();
            } catch (StartBusyTransporter startBusyTransporter) {
                System.out.println(Constant.START_BUSY_TRANSPORTER_MESSAGE);
            } catch (StartEmptyTransporter startEmptyTransporter) {
                System.out.println(Constant.START_EMPTY_TRANSPORTER_MESSAGE);
            }
        });
    }

    private static void initBackButton() {
        Button backButton=new Button("Back",40,90, 300,640);
        addNode(backButton.getNode());
        backButton.getNode().setOnMouseClicked(event -> {

            GameScene.getNextTurnTimer().start();
            InputReader.setScene(GameScene.getScene());
        });
    }

    private static void initItems() {
        for (int i = 0; i < Math.min(26,items.size()); i++) {
            Item item = items.get(i);
            ImageView imageView=item.getImageView();
            addNode(imageView);
            imageView.setX(getItemPositionInWarehouseX(i));
            imageView.setY(getItemPositionInWarehouseY(i));
            Button addToTruckButton=new Button("To truck",40,80,
                    getItemPositionInWarehouseX(i)+160,getItemPositionInWarehouseY(i)+5);
            addNode(addToTruckButton.getNode());
            addToTruckButton.getNode().setOnMouseClicked(event -> {
                try {
                    InputReader.addItemToTruck(Constant.getItemByType(item.getName()));
                } catch (NoTransporterSpaceException e) {
                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                } catch (NoSuchItemInWarehouseException e) {
                    System.out.println(Constant.NO_SUCH_ITEM_MESSAGE);
                }
            });
        }
    }

    private static int getItemPositionInWarehouseX(int i) {
        return Constant.ITEM_POSITION_IN_WAREHOUSE_X[(i>=13)?1:0];
    }
    private static int getItemPositionInWarehouseY(int i) {
        return Constant.ITEM_POSITION_IN_WAREHOUSE_Y+37*((i>=13)?i-13:i);
    }

    private static void initBackground() throws FileNotFoundException {
        Image image=new Image(new FileInputStream("./Textures/TruckBackGround.png"));
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        imageView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        root.getChildren().add(imageView);
    }
    public static void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static void deleteNode(Node node) {
        root.getChildren().remove(node);
    }

    public static void setMiddlePosition(ImageView imageView,double width,double height,double x,double y){
        imageView.setX((double)x-width/2.0);
        imageView.setY((double)y-height/2.0);
    }

    public static Scene getScene() {
        return scene;
    }

    public static void refresh(){

    }
}
