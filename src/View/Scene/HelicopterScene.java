package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import View.Button.BlueButton;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import Exception.*;

public class HelicopterScene {
    public static final int ITEM_POSITION_INHELICOPTER_Y = 128;
    public static final int ITEM_POSITION_INHELICOPTER_X = 55;
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static ArrayList<Item> items=InputReader.getCurrentController().getHelicopter().getPossibleItems();

    public static void init(){
        try {
            initBackground();
            initItemView();
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
            InputReader.clearHelicopter();
        });
    }

    private static void initGoButton() {
        BlueButton goButton=new BlueButton("Go",40,90, 440,640);
        addNode(goButton.getNode());
        goButton.getNode().setOnMouseClicked(event -> {
            try {
                InputReader.startHelicopter();
                GameScene.getNextTurnTimer().start();
                InputReader.setScene(GameScene.getScene());
            } catch (StartBusyTransporter startBusyTransporter) {
                System.out.println(Constant.START_BUSY_TRANSPORTER_MESSAGE);
            } catch (StartEmptyTransporter startEmptyTransporter) {
                System.out.println(Constant.START_EMPTY_TRANSPORTER_MESSAGE);
            } catch (NotEnoughMoneyException e) {
                System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
            }
        });
    }

    private static void initBackButton() {
        BlueButton backButton=new BlueButton("Back",40,90, 300,640);
        addNode(backButton.getNode());
        backButton.getNode().setOnMouseClicked(event -> {
            GameScene.getNextTurnTimer().start();
            InputReader.setScene(GameScene.getScene());
        });
    }

    private static void initItemView() {
        for (int i = 0; i < items.size(); i++) {
            Item item = Constant.getItemByType(items.get(i).getName());

            item.initView();
            ImageView imageView = item.getImageView();
            addNode(imageView);
            imageView.setX(getItemPositionInWarehouseX());
            imageView.setY(getItemPositionInWarehouseY(i));
            BlueButton addToHelicopterButton = new BlueButton("To Helicopter", 35, 120,
                    getItemPositionInWarehouseX() + 190, getItemPositionInWarehouseY(i) + 5);
            addNode(addToHelicopterButton.getNode());
            addToHelicopterButton.getNode().setOnMouseClicked(event -> {
                try {
                    InputReader.addItemToHelicopter(Constant.getItemByType(item.getName()));
                } catch (NoTransporterSpaceException e) {
                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                }
            });
        }
    }

    private static int getItemPositionInWarehouseX() {
        return ITEM_POSITION_INHELICOPTER_X;
    }
    private static int getItemPositionInWarehouseY(int i) {
        return ITEM_POSITION_INHELICOPTER_Y +37*i;
    }

    private static void initBackground() throws FileNotFoundException {
        Image image=new Image(new FileInputStream("Textures/HelicopterBack.png"));
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
