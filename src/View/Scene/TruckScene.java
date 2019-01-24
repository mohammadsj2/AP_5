package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import Exception.*;
import Model.WareHouse;
import View.Button.BlueButton;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class TruckScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static ArrayList<Item> items=Constant.getAllPossibleItems();
    private static ArrayList<Node> toDeleteInRefresh =new ArrayList<>();

    public static void init(){
        try {
            initBackground();
            refreshItemView();
            initBackButton();
            initGoButton();
            initClearButton();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initClearButton() {
        BlueButton clearButton=new BlueButton("Clear",40,90, 580,640);
        addNode(clearButton.getNode());
        clearButton.getNode().setOnMouseClicked(event -> {
            InputReader.clearTruck();
            refresh();
        });
    }

    private static void initGoButton() {
        BlueButton goButton=new BlueButton("Go",40,90, 440,640);
        addNode(goButton.getNode());
        goButton.getNode().setOnMouseClicked(event -> {
            try {
                InputReader.startTruck();
                GameScene.getNextTurnTimer().start();
                InputReader.setScene(GameScene.getScene());
            } catch (StartBusyTransporter startBusyTransporter) {
                System.out.println(Constant.START_BUSY_TRANSPORTER_MESSAGE);
            } catch (StartEmptyTransporter startEmptyTransporter) {
                System.out.println(Constant.START_EMPTY_TRANSPORTER_MESSAGE);
            }
        });
    }

    private static void initBackButton() {
        BlueButton backButton=new BlueButton("Back",40,90, 300,640);
        addNode(backButton.getNode());
        backButton.getNode().setOnMouseClicked(event -> {
            InputReader.clearTruck();
            refresh();
            GameScene.getNextTurnTimer().start();
            InputReader.setScene(GameScene.getScene());
        });
    }

    private static void refreshItemView() {
        root.getChildren().removeAll(toDeleteInRefresh);
        toDeleteInRefresh.clear();
        int j=0;
        WareHouse wareHouse=InputReader.getCurrentController().getWareHouse();
        for (Item item : items) {
            item.refreshView();
            if (!wareHouse.getItems().contains(item)) {
                item.getImageView().setImage(null);
                continue;
            }
            ImageView imageView = item.getImageView();
            addNode(imageView);
            imageView.setX(getItemPositionInWarehouseX(j));
            imageView.setY(getItemPositionInWarehouseY(j));
            BlueButton addToTruckButton = new BlueButton("To truck", 35, 80,
                    getItemPositionInWarehouseX(j) + 160, getItemPositionInWarehouseY(j) + 5);
            Label label=new Label();
            label.relocate(getItemPositionInWarehouseX(j) + 80, getItemPositionInWarehouseY(j)+14);
            label.setText("x"+wareHouse.getNumberOfThisItem(item));
            addNode(label);
            toDeleteInRefresh.add(addToTruckButton.getNode());
            toDeleteInRefresh.add(label);
            addNode(addToTruckButton.getNode());
            addToTruckButton.getNode().setOnMouseClicked(event -> {
                try {
                    InputReader.addItemToTruck(Constant.getItemByType(item.getName()));
                    refresh();
                } catch (NoTransporterSpaceException e) {
                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                } catch (NoSuchItemInWarehouseException e) {
                    System.out.println(Constant.NO_SUCH_ITEM_MESSAGE);
                }
            });
            j++;
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

    public static Scene refreshAndGetScene() {
        refresh();
        return scene;
    }

    public static void refresh(){
        refreshItemView();
    }
}
