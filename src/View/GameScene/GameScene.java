package View.GameScene;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Model.WorkShop;
import View.NextTurnTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
            initMap();
            initAddAnimalButtons();
            nextTurnButtonForDebug();
            controller.getWareHouse().initView();
            new NextTurnTimer().start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initWell() {
        InputReader.getCurrentController().getWell().initView();
        InputReader.getCurrentController().getWell().refreshView();

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
            workShop.initView();
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
        backgroundView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                int cellx = (int) ((x - 230) / 3.7);
                int celly = (int) ((y - 230) / 2.1);
                if (cellx >= 0 && cellx < 100 && celly >= 0 && celly < 100) {
                    InputReader.plant(cellx, celly);
                }
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
