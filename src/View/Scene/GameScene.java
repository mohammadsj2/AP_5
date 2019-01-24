package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Model.*;
import Model.Transporter.Helicopter;
import Model.Transporter.Truck;
import View.Button.BlueButton;
import View.CoinView;
import View.NextTurnTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class GameScene
{
    private static NextTurnTimer nextTurnTimer;
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static Text moneyText = null;

    public static void init()
    {
        try
        {
            TruckScene.init();
            HelicopterScene.init();

            initBackground();
            initWell();
            initWareHouse();
            initWorkShops();

            initMap();
            initAddAnimalButtons();
            //nextTurnButtonForDebug();
            initTransporters();
            initMoney();
            initButtons();

            nextTurnTimer = new NextTurnTimer();
            nextTurnTimer.start();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    private static void initTransporters() throws FileNotFoundException
    {
        Date date = new Date();
        Random random = new Random(date.toString().hashCode());
        Image image = new Image(new FileInputStream("Textures/UI/Panels/road_" + (random.nextInt(100) % 4 + 1) + ".png"));
        ImageView imageView = new ImageView(image);
        addNode(imageView);
        imageView.setY(3);
        imageView.setX(600);

        initUpgradableAndViewable(InputReader.getCurrentController().getTruck());
        initUpgradableAndViewable(InputReader.getCurrentController().getHelicopter());
    }

    private static void initUpgradableAndViewable(Upgradable upgradable) throws FileNotFoundException
    {
        ((Viewable) upgradable).initView();
        ImageView imageView = ((Viewable) upgradable).getImageView();
        setUpgradeButton(upgradable, imageView.getX(), imageView.getY() + imageView.getImage().getHeight() - 23);
    }

    private static void initButtons()
    {
        BlueButton menuButton = new BlueButton("Menu", 40, 90, 20, 630,true);
        menuButton.getNode().setOnMouseClicked(event ->
        {
            if (InputReader.getCurrentController().isGameFinished()) return;
            nextTurnTimer.stop();
            MenuScene.init(true);
            InputReader.setScene(MenuScene.getScene());
        });
        addNode(menuButton.getNode());

    }

    private static void initMoney()
    {
        BlueButton moneyLabel = new BlueButton(String.valueOf(InputReader.getCurrentController().getMoney())
                , 40, 90, 615, 35,true);
        moneyLabel.onlyShowTextOfButton();
        moneyText = moneyLabel.getTextLabel();
        moneyText.setFill(Color.YELLOW);
        addNode(moneyLabel.getNode());

        CoinView coinView = new CoinView(660, 28);
        addNode(coinView.getNode());
    }

    private static void initWareHouse() throws FileNotFoundException
    {
        initUpgradableAndViewable(InputReader.getCurrentController().getWareHouse());
    }

    private static void initWell()
    {
        Well well = InputReader.getCurrentController().getWell();
        well.initView();
        ImageView wellView = well.getImageView();
        setUpgradeButton(well, wellView.getX(), wellView.getY() + wellView.getImage().getHeight() / 4 - 23);
    }

    private static void initMap()
    {
        InputReader.getCurrentController().getMap().initView();
        InputReader.getCurrentController().getMap().refreshView();
    }

    private static void nextTurnButtonForDebug() throws FileNotFoundException
    {
        Image buttonImage;
        ImageView button;
        buttonImage = new Image(new FileInputStream("./Textures/AddAnimalButtons/A.png"));
        button = new ImageView(buttonImage);
        button.setX(400);
        button.setY(10);
        button.setFitHeight(60);
        button.setFitWidth(60);
        addNode(button);

        button.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (InputReader.getCurrentController().isGameFinished()) return;
                InputReader.nextTurn(1);
            }
        });
    }

    private static void initAddAnimalButtons() throws FileNotFoundException
    {
        addAnAnimalButton("./Textures/AddAnimalButtons/ChickenButton.png", 20, "chicken");

        addAnAnimalButton("./Textures/AddAnimalButtons/CowButton.png", 80, "cow");

        addAnAnimalButton("./Textures/AddAnimalButtons/SheepButton.png", 140, "sheep");

        addAnAnimalButton("./Textures/AddAnimalButtons/CatButton.png", 200, "cat");

        addAnAnimalButton("./Textures/AddAnimalButtons/DogButton.png", 260, "dog");
    }

    private static void addAnAnimalButton(String pathToImage, int positionXOfButton, String typeOfAnimal) throws FileNotFoundException
    {
        Image buttonImage;
        ImageView button;
        buttonImage = new Image(new FileInputStream(pathToImage));
        button = new ImageView(buttonImage);
        button.setX(positionXOfButton);
        button.setY(10);
        button.setFitHeight(60);
        button.setFitWidth(60);
        addNode(button);

        button.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (InputReader.getCurrentController().isGameFinished()) return;
                InputReader.buy(typeOfAnimal);
            }
        });
    }

    private static void initWorkShops() throws FileNotFoundException
    {
        ArrayList<WorkShop> workShops = InputReader.getCurrentController().getWorkShops();
        for (WorkShop workShop : workShops)
        {
            workShop.initView();
            ImageView workShopView = workShop.getImageView();
            setUpgradeButton(workShop, workShopView.getX(), workShopView.getY() + workShopView.getImage().getHeight() / 4);
        }
    }

    private static void setUpgradeButton(Upgradable upgradable, double x, double y)
    {
        ImageView imageView = ((Viewable) upgradable).getImageView();
        BlueButton upgradeButton = new BlueButton(String.valueOf(upgradable.upgradeCost()) + "\uD83D\uDCB0"
                , 20, 50, x, y,true);
        upgradeButton.getNode().setOnMouseClicked(event ->
        {
            if (InputReader.getCurrentController().isGameFinished()) return;
            if (!(upgradable instanceof WorkShop) || !((WorkShop) upgradable).isBusy())
            {
                String inputReaderString = null;
                if (upgradable instanceof Well)
                    inputReaderString = "well";
                else if (upgradable instanceof Helicopter)
                    inputReaderString = "helicopter";
                else if (upgradable instanceof Truck)
                    inputReaderString = "truck";
                else if (upgradable instanceof WareHouse)
                    inputReaderString = "warehouse";
                else if (upgradable instanceof WorkShop)
                    inputReaderString = "workshop" + ((WorkShop) upgradable).getLocation();
                InputReader.upgrade(inputReaderString);
                if (!upgradable.canUpgrade())
                    deleteNode(upgradeButton.getNode());
                else
                    upgradeButton.setText(String.valueOf(upgradable.upgradeCost()) + "\uD83D\uDCB0");
            }
        });
        addNode(upgradeButton.getNode());
    }

    public static void addNode(Node node)
    {
        if (!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static void deleteNode(Node node)
    {
        root.getChildren().remove(node);
    }

    public static void setMiddlePosition(ImageView imageView, double width, double height, double x, double y)
    {
        imageView.setX((double) x - width / 2.0);
        imageView.setY((double) y - height / 2.0);
    }

    private static void initBackground() throws FileNotFoundException
    {
        int numberOfWorkshops = InputReader.getCurrentController().getWorkShops().size();
        Image backgroundImage = new Image(new FileInputStream("./Textures/GameBackGround/back" +
                numberOfWorkshops + ".png"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);
        backgroundView.setOnMouseClicked(event ->
        {
            if (InputReader.getCurrentController().isGameFinished()) return;
            double x = event.getSceneX();
            double y = event.getSceneY();
            int cellx = (int) ((x - 230) / 3.7);
            int celly = (int) ((y - 230) / 2.1);
            for (int i = Math.max(0, cellx - 1); i < Math.min(Constant.MAP_COLUMNS - 1, cellx + 1); i++)
            {
                for (int j = Math.max(0, celly - 1); j < Math.min(Constant.MAP_ROWS, celly + 1); j++)
                {
                    InputReader.plant(i, j);
                }
            }
        });
    }

    public static void setImageViewPositionOnMap(ImageView imageView, double x, double y)
    {
        imageView.setX(modifiedX(x));
        imageView.setY(modifiedY(y));
    }

    public static double modifiedY(double y)
    {
        return y * 2.1 + 230.0;
    }

    public static double modifiedX(double x)
    {
        return x * 3.7 + 230.0;
    }

    public static Scene getScene()
    {
        return scene;
    }

    public static NextTurnTimer getNextTurnTimer()
    {
        return nextTurnTimer;
    }

    public static void updateMoney()
    {
        if (moneyText == null) return;
        moneyText.setText(String.valueOf(InputReader.getCurrentController().getMoney()));
    }

    public static void setWinningMessage()
    {
        nextTurnTimer.stop();
        InputReader.getCurrentController().finishGame();
        int width=400,height=300;
        Group winRoot=new Group();
       // Scene winScene=new Scene(winRoot,width,height);
        winRoot.relocate((double)(Constant.GAME_SCENE_WIDTH-width)/2, (double)(Constant.GAME_SCENE_HEIGHT-height)/2);
        winRoot.resize(width, height);
        Image image = null;
        try
        {
            image = new Image(new FileInputStream("./Textures/win-back.png"));// TODO Change Background
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        winRoot.getChildren().add(imageView);
        Label label=new Label("You Won!");
        label.setTextFill(Color.YELLOWGREEN);
        label.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 50");
        label.relocate(90,100);
        winRoot.getChildren().add(label);
        BlueButton backToLevelSelectButton=new BlueButton("Continue",50,100
                ,(double)(width-100)/2,220,false);
        backToLevelSelectButton.getNode().setOnMouseClicked(event ->
        {
            levelSelectScene.init();
            InputReader.setScene(levelSelectScene.getScene());
        });
        winRoot.getChildren().add(backToLevelSelectButton.getNode());
        root.getChildren().add(winRoot);

    }
}
