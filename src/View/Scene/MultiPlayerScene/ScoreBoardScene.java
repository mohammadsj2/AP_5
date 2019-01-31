package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
import View.Label.FancyLabel;
import View.Scene.ConnectScene;
import javafx.application.Platform;
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
import java.util.Collections;
import java.util.Comparator;

public class ScoreBoardScene extends MultiPlayerScene{
    public final static ScoreBoardScene SCORE_BOARD_SCENE=new ScoreBoardScene();

    private static ArrayList<Client> clients=null;
    private static ArrayList<Node> toRemove=new ArrayList<>();
    private static String sortBy="level";

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
        setClients(InputReader.getClient().getScoreBoard(),true);
    }

    public void setClients(ArrayList<Client> clients,boolean force) {
        ScoreBoardScene.clients = clients;
        if(sortBy.equals("money")){
            Collections.sort(clients, (o1, o2) -> -o1.getMoney()+o2.getMoney());
        }else{
            Collections.sort(clients, (o1, o2) -> -o1.getLevel()+o2.getLevel());
        }
        refresh(force);
    }

    public void refresh(boolean force) {
        removeAllNodesWithForce(force, toRemove, root.getChildren());
        int x=100;
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            FancyLabel label = new FancyLabel(client.getName(),23,x,positionInScoreBoardY(i));
            addNodeWithForce(label.getNode(),force);
            label.getNode().setOnMouseClicked(event -> {
                ProfileScene profileScene=new ProfileScene(client);
                profileScene.init();
                InputReader.setScene(profileScene.getScene());
            });
            FancyLabel score=new FancyLabel(Integer.toString(client.getLevel()),23,x+200,positionInScoreBoardY(i));
            addNodeWithForce(score.getNode(),force);
            FancyLabel money=new FancyLabel(Integer.toString(client.getMoney()),23,x+400,positionInScoreBoardY(i));
            addNodeWithForce(money.getNode(),force);
        }
    }

    private double positionInScoreBoardY(int i) {
        return 120+i*40;
    }

    public void deleteNode(Node node)
    {
        root.getChildren().remove(node);
    }

    private void addNodeWithForce(Node node,boolean force){
        if(force){
            root.getChildren().add(node);
            toRemove.add(node);
        }else{
            Platform.runLater(() -> {
                root.getChildren().add(node);
                toRemove.add(node);
            });
        }
    }

    public static void setSortBy(String sortBy) {
        ScoreBoardScene.sortBy = sortBy;
    }

    @Override
    protected void initStartButtons() {
        super.initStartButtons();
        BlueButton sortByMoney=new BlueButton("Sort by money",40,130,370,60);
        BlueButton sortByLevel=new BlueButton("Sort by level",40,130,220,60);
        addNode(sortByLevel.getNode());
        addNode(sortByMoney.getNode());

        sortByMoney.getNode().setOnMouseClicked(event -> {
            setSortBy("money");
            setClients(clients,true);
        });
        sortByLevel.getNode().setOnMouseClicked(event -> {
            setSortBy("level");
            setClients(clients,true);
        });
    }
}
