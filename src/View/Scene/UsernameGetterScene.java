package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Random;

public class UsernameGetterScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static Label errorLabel=new Label();
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

        TextField textField=new TextField("user"+ new Random(LocalDateTime.now().getNano()).nextInt(100000));
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
        if(textField.getText().length()>10)
        {
            root.getChildren().remove(errorLabel);
            errorLabel=new Label("Name must have at most 10 characters!");
            errorLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 30;-fx-font-family: 'Comic Sans MS';" +
                    "-fx-background-color: rgba(0, 0, 0, 0.5);-fx-padding: 2px;" +
                    "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;");
            errorLabel.relocate(180,300);
            root.getChildren().add(errorLabel);
            return ;
        }
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

    public static void deleteNode(Node node) {
        root.getChildren().remove(node);
    }

    public static Scene getScene()
    {
        return scene;
    }
}
