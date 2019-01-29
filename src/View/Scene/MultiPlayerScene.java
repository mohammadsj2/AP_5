package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
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
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    public static void init()
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

    private static void initStartButtons()
    {
        int ySpace=105;
        BlueButton button=new BlueButton("Play!",80,200,675,50,false);
        addNode(button.getNode());

        button=new BlueButton("Scoreboard",80,200,675,50+ySpace,false);
        addNode(button.getNode());

        button=new BlueButton("ChatRoom",80,200,675,50+2*ySpace,false);
        button.getNode().setOnMouseClicked(event -> {

        });
        addNode(button.getNode());

        button=new BlueButton("Disconnect",80,200,675,50+3*ySpace,false);

        button.getNode().setOnMouseClicked(event -> {
            InputReader.getClient().disconnect();
            ConnectScene.init();
            InputReader.setScene(ConnectScene.getScene());
        });

        addNode(button.getNode());

    }



    private static void initBackground() throws FileNotFoundException
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


    public static void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static Scene getScene()
    {
        return scene;
    }
}
