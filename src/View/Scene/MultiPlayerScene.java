package View.Scene;

import Constant.Constant;
import Network.Address;
import View.Label.FancyLabel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class MultiPlayerScene
{

    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);


    public static void init()
    {
        try
        {
            initBackground();
            initHost();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void initHost()
    {
        Address address=new Address(new Random().nextInt(8999)+1000,"localhost");
        FancyLabel portLabel=new FancyLabel("Port: "+address.getPort(),30,300,100);
        root.getChildren().add(portLabel.getNode());
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
