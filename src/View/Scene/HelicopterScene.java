package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import View.Button.BlueButton;
import View.CoinView;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import Exception.*;

public class HelicopterScene {
    public static final int ITEM_POSITION_IN_HELICOPTER_Y = 128;
    public static final int ITEM_POSITION_IN_HELICOPTER_X = 55;
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    private static HashMap<Item,Integer> items;
    private static HashMap<Item,Label> inHelicopterCounterLabel;

    public static void init(){
        try {
            if(InputReader.getClient().isOnline())
            {
                items=InputReader.getClient().getMarketItems();
            }
            else
            {
                /*TODO*///
            }
            inHelicopterCounterLabel=new HashMap<>();
            initBackground();
            initItemView();
            initBackButton();
            initGoButton();
            initClearButton();
            initCoinView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initCoinView() {
        CoinView coinView=new CoinView(120,556,1);
        addNode(coinView.getNode());
    }

    private static void initClearButton() {
        BlueButton clearButton=new BlueButton("Clear",40,90, 279,610,false);
        addNode(clearButton.getNode());
        clearButton.getNode().setOnMouseClicked(event -> {
            InputReader.clearHelicopter();
            refresh();
        });
    }

    private static void initGoButton() {
        BlueButton goButton=new BlueButton("Go",40,90, 158,610,false);
        addNode(goButton.getNode());
        goButton.getNode().setOnMouseClicked(event -> {
            try {
                InputReader.startHelicopter();
                GameScene.getNextTurnTimer().start();
                InputReader.setScene(GameScene.getScene());
                refresh();
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
        BlueButton backButton=new BlueButton("Back",40,90, 38,610,false);
        addNode(backButton.getNode());
        backButton.getNode().setOnMouseClicked(event -> {
            InputReader.clearHelicopter();
            GameScene.getNextTurnTimer().start();
            refresh();
            InputReader.setScene(GameScene.getScene());
            InputReader.clearHelicopter();
            refresh();
        });
    }

    private static void initItemView() {
        int i=-1;
        for (Item item:items.keySet()) {
            i++;
            item.initView();
            ImageView imageView = item.getImageView();
            Label label=new Label("x0");
            label.setStyle("-fx-font-size: 20;");
            label.relocate(getItemPositionInWarehouseX()+90,getItemPositionInWarehouseY(i)+10);
            inHelicopterCounterLabel.put(item,label);
            addNode(label);

            addNode(imageView);
            imageView.setX(getItemPositionInWarehouseX());
            imageView.setY(getItemPositionInWarehouseY(i));
            BlueButton addToHelicopterButton = new BlueButton("To Helicopter", 35, 120,
                    getItemPositionInWarehouseX() + 190, getItemPositionInWarehouseY(i) + 5,false);
            addNode(addToHelicopterButton.getNode());
            addToHelicopterButton.getNode().setOnMouseClicked(event -> {
                try {
                    if(InputReader.getCurrentController().getHelicopter().getNumberOfThisItem(item)<
                        items.get(item))
                    {
                        InputReader.addItemToHelicopter(item);
                        refresh();
                    }
                } catch (NoTransporterSpaceException e) {
                    System.out.println(Constant.NOT_ENOUGH_SPACE_MESSAGE);
                }
            });
        }
    }


    private static int getItemPositionInWarehouseX() {
        return ITEM_POSITION_IN_HELICOPTER_X;
    }
    private static int getItemPositionInWarehouseY(int i) {
        return ITEM_POSITION_IN_HELICOPTER_Y +37*i;
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
        refreshLabels();
    }

    private static void refreshLabels() {
        for(Item item:inHelicopterCounterLabel.keySet()){
            Label label=inHelicopterCounterLabel.get(item);
            label.setText("x"+InputReader.getCurrentController().getHelicopter().getNumberOfThisItem(item));
        }
    }

    public static void clear() {
        inHelicopterCounterLabel.clear();
        root.getChildren().clear();
    }
}
