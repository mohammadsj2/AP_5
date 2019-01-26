package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import View.Button.BlueButton;
import View.Button.CircleButton;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class levelSelectScene
{
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static BlueButton newGameButton=new BlueButton("",70,190,600,270,false);
    private static BlueButton loadGameButton=new BlueButton("",70,190,600,360,false);

    public static Scene getScene()
    {
        return scene;
    }

    public static void init()
    {
        try
        {
            initBackground();
            initLevelButtons();
            initMenuButton();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void initMenuButton()
    {
        BlueButton menuButton=new BlueButton("Back to Menu",50,150,740,640,false);
        menuButton.getNode().setOnMouseClicked(event ->
        {
            MenuScene.init(false);
            InputReader.setScene(MenuScene.getScene());
        });
        root.getChildren().add(menuButton.getNode());
    }

    private static void initLevelButtons()
    {
        int levelCount = 10;
        int[] positionX = {500, 430, 360, 290, 220, 160, 230, 170, 220, 290};
        int[] positionY = {635, 610, 635, 610, 635, 570, 525, 465, 395, 365};
        for (int i = 0; i < levelCount; i++)
        {
            CircleButton button = new CircleButton(String.valueOf(i + 1), 60, 60
                    , positionX[i], positionY[i],false);
            int finalI = i;
            button.getNode().setOnMouseClicked(event ->
            {
                refreshButtons(finalI+1);
            });
            root.getChildren().add(button.getNode());
        }
        deleteNode(newGameButton.getNode());
        deleteNode(loadGameButton.getNode());
    }

    private static void refreshButtons(int levelNumber)
    {
        newGameButton.setText("Start Level "+levelNumber);
        loadGameButton.setText("Load Level "+levelNumber);
        newGameButton.getNode().setOnMouseClicked(event ->
        {
            try
            {
                InputReader.loadLevel(levelNumber);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            GameScene.init();
            InputReader.setScene(GameScene.getScene());
        });
        loadGameButton.getNode().setOnMouseClicked(event ->
        {
            try
            {
                InputReader.loadSave(levelNumber);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            GameScene.loadInit();
            InputReader.setScene(GameScene.getScene());
        });
        addNode(newGameButton.getNode());
        addNode(loadGameButton.getNode());
    }

    private static void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/levelSelect-back.png"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);
    }

    public static void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static void deleteNode(Node node) {
        root.getChildren().remove(node);
    }

}
