package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import View.Button.BlueButton;
import View.Scene.MultiPlayerScene.MultiPlayerScene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuScene
{

    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    public static void init(boolean isInGame)
    {
        try
        {
            initBackground();
            if(isInGame)
                initInGameButtons();
            else
                initStartButtons();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void initStartButtons()
    {
        int height=70,width=180;
        BlueButton singlePlayerButton=new BlueButton("SinglePlayer",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,100,false);
        singlePlayerButton.getNode().setOnMouseClicked(event ->
        {
            LevelSelectScene.init();
            InputReader.setScene(LevelSelectScene.getScene());
            /*
            InputReader.loadLevel(1);
            InputReader.runByLevelNumber(1);
            GameScene.init();
            InputReader.setScene(GameScene.getScene());*/
        });
        addNode(singlePlayerButton.getNode());
        BlueButton multiPlayerButton=new BlueButton("MultiPlayer",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200,false);
        multiPlayerButton.getNode().setOnMouseClicked(event ->
        {
            ConnectScene.init();
            InputReader.setScene(ConnectScene.getScene());
            /*
            InputReader.loadLevel(1);
            InputReader.runByLevelNumber(1);
            GameScene.init();
            InputReader.setScene(GameScene.getScene());*/
        });
        addNode(multiPlayerButton.getNode());
        /*
        BlueButton loadButton=new BlueButton("Load Game",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200);
        loadButton.getNode().setOnMouseClicked(event ->
        {
            try
            {
                InputReader.load("save");
                GameScene.init();
                InputReader.setScene(GameScene.getScene());

            } catch (FileNotFoundException e)
            {
                System.out.println(Constant.NO_SAVE_MESSAGE);
            }
        });
        addNode(loadButton.getNode());*/
        BlueButton exitButton=new BlueButton("Exit",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,300,false);
        exitButton.getNode().setOnMouseClicked(event ->
        {
            Media media=new Media(new File("Textures/Sound/khodahafez.wav").toURI().toString());
            MediaPlayer mediaPlayer=new MediaPlayer(media);
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                InputReader.primaryStage.close();
                System.exit(0);
            });
        });
        addNode(exitButton.getNode());

    }

    private static void initInGameButtons()
    {
        int height=70,width=180;
        BlueButton resumeButton=new BlueButton("Resume",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,100,false);
        resumeButton.getNode().setOnMouseClicked(event ->
        {
            GameScene.getNextTurnTimer().start();
            InputReader.setScene(GameScene.getScene());
        });
        addNode(resumeButton.getNode());
        BlueButton saveButton=new BlueButton("Save Game",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200,false);
        saveButton.getNode().setOnMouseClicked(event ->
        {
            InputReader.save();
        });
        addNode(saveButton.getNode());
        /*
        BlueButton loadButton=new BlueButton("Load Game",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,300);
        loadButton.getNode().setOnMouseClicked(event ->
        {
            try
            {
                InputReader.load("save");
            } catch (FileNotFoundException e)
            {
                System.out.println(Constant.NO_SAVE_MESSAGE);
            }
            GameScene.init();
            InputReader.setScene(GameScene.getScene());
        });
        addNode(loadButton.getNode());*/
        BlueButton backToMenuButton=new BlueButton("Back to Menu",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,300,false);
        backToMenuButton.getNode().setOnMouseClicked(event ->
        {
            GameScene.clear();
            if(InputReader.getClient().isOnline())
            {
                MultiPlayerScene.MULTI_PLAYER_SCENE.init();
                InputReader.setScene(MultiPlayerScene.MULTI_PLAYER_SCENE.getScene());
            }
            else
            {
                MenuScene.init(false);
                InputReader.setScene(MenuScene.getScene());
            }
        });
        addNode(backToMenuButton.getNode());
    }

    private static void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/menu-back.jpg"));
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

    public static Scene getScene()
    {
        return scene;
    }

}
