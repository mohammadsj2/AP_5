package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
import View.Label.FancyLabel;
import View.Scene.ConnectScene;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ScoreBoardScene extends MultiPlayerScene{
    public final static ScoreBoardScene SCORE_BOARD_SCENE=new ScoreBoardScene();

    private static ArrayList<Client> clients;
    private static ArrayList<Node> toRemove=new ArrayList<>();

    @Override
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
        setClients(InputReader.getClient().getScoreBoard());
    }

    public void setClients(ArrayList<Client> clients) {
        ScoreBoardScene.clients = clients;
        refresh();
    }

    public void refresh() {
        for(Node node:toRemove){
            deleteNode(node);
        }
        int x=100;
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            FancyLabel label = new FancyLabel(client.getName(),23,x,positionInScoreBoardY(i));
            addNode(label.getNode());
            label.getNode().setOnMouseClicked(event -> {
                ProfileScene profileScene=new ProfileScene(client);
                profileScene.init();
                InputReader.setScene(profileScene.getScene());
            });
        }
    }

    private double positionInScoreBoardY(int i) {
        return 70+i*40;
    }

    public void deleteNode(Node node)
    {
        root.getChildren().remove(node);
    }
}
