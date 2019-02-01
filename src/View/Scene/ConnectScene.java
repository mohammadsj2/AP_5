package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Network.Address;
import Network.Server.Server;
import View.Button.BlueButton;
import Exception.*;
import View.Label.FancyLabel;
import View.Scene.MultiPlayerScene.MultiPlayerScene;
import com.sun.org.apache.xalan.internal.xsltc.dom.AdaptiveResultTreeImpl;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectScene
{

    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);


    public static void init()
    {
        try
        {
            initBackground();
            initHost();
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

    private static void initHost()
    {
        InetAddress inetAddress=null;
        try {
            inetAddress = InetAddress. getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Address address = new Address(8060, inetAddress. getHostAddress());

        FancyLabel portLabel=new FancyLabel("Port: "+address.getPort(),30,250,100);
        BlueButton hostButton=new BlueButton("Host",45,200,450,100,false);
        hostButton.getNode().setOnMouseClicked(event ->
        {

            InputReader.setServer(new Server(address));
            MultiPlayerScene.MULTI_PLAYER_SCENE.init();
            InputReader.setScene(MultiPlayerScene.MULTI_PLAYER_SCENE.getScene());
            try {

                addClient(InputReader.getClient().getAddress().getIp());
            } catch (ServerDoesNotExist serverDoesNotExist) {
                serverDoesNotExist.printStackTrace();
            } catch (NotUniqueUsernameException e) {
                System.out.println(Constant.NOT_UNIQUE_USERNAME_EXCEPTION_MESSAGE);
            }
        });
        root.getChildren().add(hostButton.getNode());
        root.getChildren().add(portLabel.getNode());

        FancyLabel hostIpLabel=new FancyLabel("Host IP: ",30,275,300);
        TextField hostIpTextField=new TextField("localhost");
        hostIpTextField.relocate(425,300);
        hostIpTextField.setMaxWidth(250);
        hostIpTextField.setStyle("-fx-font-size: 21.5;-fx-font-family: 'Comic Sans MS';");
        hostIpTextField.setAlignment(Pos.CENTER);
        hostIpTextField.setOnKeyPressed(event ->
        {
            if(event.getCode().equals(KeyCode.ENTER))
            {
                try {
                    addClient(hostIpTextField.getText());
                } catch (ServerDoesNotExist serverDoesNotExist) {
                    System.out.println(Constant.SERVER_DOES_NOT_EXIST_MESSAGE);
                    return;
                } catch (NotUniqueUsernameException e) {
                    System.out.println(Constant.NOT_UNIQUE_USERNAME_EXCEPTION_MESSAGE);
                    return;
                }
                MultiPlayerScene.MULTI_PLAYER_SCENE.init();
                InputReader.setScene(MultiPlayerScene.MULTI_PLAYER_SCENE.getScene());
            }
        });
        BlueButton joinButton=new BlueButton("Join",45,200,450,360,false);
        joinButton.getNode().setOnMouseClicked(event ->
        {
            try {
                addClient(hostIpTextField.getText());
            } catch (ServerDoesNotExist serverDoesNotExist) {
                System.out.println(Constant.SERVER_DOES_NOT_EXIST_MESSAGE);
                return;
            } catch (NotUniqueUsernameException e) {
                System.out.println(Constant.NOT_UNIQUE_USERNAME_EXCEPTION_MESSAGE);
                return;
            }
            MultiPlayerScene.MULTI_PLAYER_SCENE.init();
            InputReader.setScene(MultiPlayerScene.MULTI_PLAYER_SCENE.getScene());
        });
        root.getChildren().add(hostIpLabel.getNode());
        root.getChildren().add(hostIpTextField);
        root.getChildren().add(joinButton.getNode());
    }

    private static void addClient(String ip) throws ServerDoesNotExist, NotUniqueUsernameException {
        InputReader.getClient().connectToServer(ip);
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
