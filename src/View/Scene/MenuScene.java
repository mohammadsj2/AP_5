package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import View.Button;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        Button playButton=new Button("Play",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,100);
        playButton.getNode().setOnMouseClicked(event ->
        {
            levelSelectScene.init();
            InputReader.setScene(levelSelectScene.getScene());
            /*
            InputReader.loadLevel(1);
            InputReader.runByLevelNumber(1);
            GameScene.init();
            InputReader.setScene(GameScene.getScene());*/
        });
        addNode(playButton.getNode());
        /*
        Button loadButton=new Button("Load Game",height,width
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
        Button exitButton=new Button("Exit",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200);
        exitButton.getNode().setOnMouseClicked(event ->
        {
            InputReader.primaryStage.close();
        });
        addNode(exitButton.getNode());

    }

    private static void initInGameButtons()
    {
        int height=70,width=180;
        Button resumeButton=new Button("Resume",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,100);
        resumeButton.getNode().setOnMouseClicked(event ->
        {
            GameScene.getNextTurnTimer().start();
            InputReader.setScene(GameScene.getScene());
        });
        addNode(resumeButton.getNode());
        Button saveButton=new Button("Save Game",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200);
        saveButton.getNode().setOnMouseClicked(event ->
        {
            InputReader.save("save");
        });
        addNode(saveButton.getNode());
        /*
        Button loadButton=new Button("Load Game",height,width
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
        Button backToMenuButton=new Button("Back to Menu",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,300);
        backToMenuButton.getNode().setOnMouseClicked(event ->
        {
            MenuScene.init(false);
            InputReader.setScene(MenuScene.getScene());
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
