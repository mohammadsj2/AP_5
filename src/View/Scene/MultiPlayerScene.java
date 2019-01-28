package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

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
        int height=70,width=180,textWidth=300;
        BlueButton playButton=new BlueButton("Login!",height,width
                ,((double)Constant.GAME_SCENE_WIDTH-width)/2,200,false);

        TextField textField=new TextField();
        textField.setMinWidth(textWidth);
        textField.relocate(((double)Constant.GAME_SCENE_WIDTH-textWidth)/2,100);
        textField.setStyle("-fx-font-size: 25;");

        playButton.getNode().setOnMouseClicked(event ->
        {
            login(textField);
        });
        textField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                login(textField);
            }
        });
        addNode(playButton.getNode());
        addNode(textField);
    }

    private static void login(TextField textField) {
        Client client=new Client(textField.getText());
        InputReader.setClient(client);
        MenuScene.init(false);
        InputReader.setScene(MenuScene.getScene());
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

    public static Scene getScene()
    {
        return scene;
    }
}
