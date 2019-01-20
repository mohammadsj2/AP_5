package View.GameScene;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Model.*;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import View.FancyButton;
import View.NextTurnTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GameScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    public static void init() {
        try {
            Controller controller=InputReader.getCurrentController();
            initBackground();
            initWorkShops();
            initWell();
            initWareHouse();
            initMap();
            initAddAnimalButtons();
            nextTurnButtonForDebug();
            new NextTurnTimer().start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initWareHouse()
    {
        WareHouse wareHouse=InputReader.getCurrentController().getWareHouse();
        wareHouse.initView();
        ImageView wareHouseView=wareHouse.getImageView();
        setUpgradeButton(wareHouse,wareHouseView.getX(),wareHouseView.getY()+wareHouseView.getImage().getHeight()-23);

    }

    private static void initWell() {
        Well well=InputReader.getCurrentController().getWell();
        well.initView();
        well.refreshView();
        ImageView wellView=well.getImageView();
        setUpgradeButton(well,wellView.getX(),wellView.getY()+wellView.getImage().getHeight()/4-23);


    }
    private static void initMap() {
        InputReader.getCurrentController().getMap().initView();
        InputReader.getCurrentController().getMap().refreshView();
    }
    private static void nextTurnButtonForDebug() throws FileNotFoundException {
        Image buttonImage;
        ImageView button;
        buttonImage = new Image(new FileInputStream("./Textures/AddAnimalButtons/A.png"));
        button = new ImageView(buttonImage);
        button.setX(400);
        button.setY(10);
        button.setFitHeight(60);
        button.setFitWidth(60);
        addNode(button);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                InputReader.nextTurn(1);
            }
        });
    }

    private static void initAddAnimalButtons() throws FileNotFoundException {
        addAnAnimalButton("./Textures/AddAnimalButtons/ChickenButton.png", 20, "chicken");

        addAnAnimalButton("./Textures/AddAnimalButtons/CowButton.png", 80, "cow");

        addAnAnimalButton("./Textures/AddAnimalButtons/SheepButton.png", 140, "sheep");

        addAnAnimalButton("./Textures/AddAnimalButtons/CatButton.png", 200, "cat");

        addAnAnimalButton("./Textures/AddAnimalButtons/DogButton.png", 260, "dog");
    }

    private static void addAnAnimalButton(String pathToImage, int positionXOfButton, String typeOfAnimal) throws FileNotFoundException {
        Image buttonImage;
        ImageView button;
        buttonImage = new Image(new FileInputStream(pathToImage));
        button = new ImageView(buttonImage);
        button.setX(positionXOfButton);
        button.setY(10);
        button.setFitHeight(60);
        button.setFitWidth(60);
        addNode(button);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                InputReader.buy(typeOfAnimal);
            }
        });
    }

    private static void initWorkShops() throws FileNotFoundException {
        ArrayList<WorkShop> workShops=InputReader.getCurrentController().getWorkShops();
        for(WorkShop workShop:workShops)
        {
            workShop.initView();
            ImageView workShopView=workShop.getImageView();
            setUpgradeButton(workShop,workShopView.getX(),workShopView.getY()+workShopView.getImage().getHeight()/4);
        }
    }

    private static void setUpgradeButton(Upgradable upgradable,double x,double y)
    {
        ImageView imageView=((Viewable)upgradable).getImageView();
        FancyButton upgradeButton=new FancyButton(String.valueOf(upgradable.upgradeCost())+"\uD83D\uDCB0",20,50
                ,x,y);
        upgradeButton.getNode().setOnMouseClicked(event ->
        {
            if(!(upgradable instanceof WorkShop) || !((WorkShop) upgradable).isBusy())
            {
                String inputReaderString = null;
                if(upgradable instanceof Well)
                    inputReaderString="well";
                else if(upgradable instanceof Helicopter)
                    inputReaderString="helicopter";
                else if(upgradable instanceof Truck)
                    inputReaderString="truck";
                else if(upgradable instanceof WareHouse)
                    inputReaderString="warehouse";
                else if(upgradable instanceof WorkShop)
                    inputReaderString="workshop" + ((WorkShop)upgradable).getLocation();
                InputReader.upgrade(inputReaderString);
                if(!upgradable.canUpgrade())
                    deleteNode(upgradeButton.getNode());
                else
                    upgradeButton.setText(String.valueOf(upgradable.upgradeCost())+"\uD83D\uDCB0");
            }
        });
        addNode(upgradeButton.getNode());
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

    private static void initBackground() throws FileNotFoundException {
        Image backgroundImage = new Image(new FileInputStream("Textures/back.png"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);
        backgroundView.setOnMouseClicked(event ->
        {
            double x = event.getSceneX();
            double y = event.getSceneY();
            int cellX = (int) ((x - 230) / 3.7);
            int cellY = (int) ((y - 230) / 2.1);
            if (cellX >= 0 && cellX < 100 && cellY >= 0 && cellY < 100) {
                InputReader.plant(cellX, cellY);
            }
        });
    }

    public static void setImageViewPositionOnMap(ImageView imageView, double x, double y) {
        imageView.setX(x * 3.7 + 230.0);
        imageView.setY(y * 2.1 + 230.0);
    }

    public static Scene getScene() {
        return scene;
    }
}
