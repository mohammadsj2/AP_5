package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Network.Chatroom;
import Network.Client.Client;
import View.Button.BlueButton;
import View.Scene.ConnectScene;
import View.Scene.LevelSelectScene;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MultiPlayerScene {
    public final static MultiPlayerScene MULTI_PLAYER_SCENE=new MultiPlayerScene();
    protected Group root = new Group();
    protected Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    protected MultiPlayerScene()
    {

    }

    public void init()
    {
        try
        {
            initBackground();
            initStartButtons();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    protected void initStartButtons()
    {
        int ySpace=105;
        BlueButton button=new BlueButton("Play!",80,200,675,50);
        button.getNode().setOnMouseClicked(event -> {
            LevelSelectScene.initInMultiPlayer();
            InputReader.setScene(LevelSelectScene.getScene());
        });
        addNode(button.getNode());

        button=new BlueButton("Scoreboard",80,200,675,50+ySpace);
        button.getNode().setOnMouseClicked(event -> {
            ScoreBoardScene.SCORE_BOARD_SCENE.init();
            InputReader.setScene(ScoreBoardScene.SCORE_BOARD_SCENE.getScene());
        });
        addNode(button.getNode());

        button=new BlueButton("ChatRoom",80,200,675,50+2*ySpace);
        button.getNode().setOnMouseClicked(event -> {

            Client client=InputReader.getClient();
            Chatroom chatroom = client.getGlobalChatroom();
            ChatroomScene.CHATROOM_SCENE.init(chatroom);
            InputReader.setScene(ChatroomScene.CHATROOM_SCENE.getScene());
        });
        addNode(button.getNode());

        button=new BlueButton("Disconnect",80,200,675,50+3*ySpace);

        button.getNode().setOnMouseClicked(event -> {
            InputReader.getClient().disconnect();
            ConnectScene.init();
            InputReader.setScene(ConnectScene.getScene());
        });

        addNode(button.getNode());

    }



    protected void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/menu-back.jpg"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);

        Rectangle rectangle=new Rectangle(0,0,600,600);
        rectangle.relocate(50,50);
        rectangle.setOpacity(0.5);
        addNode(rectangle);
    }


    public void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public Scene getScene()
    {
        return scene;
    }
}
