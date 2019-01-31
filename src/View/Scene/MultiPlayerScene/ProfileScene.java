package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Network.Chatroom;
import Network.Client.Client;
import Network.Relationship;
import View.Label.FancyLabel;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Exception.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProfileScene extends MultiPlayerScene{
    private static ProfileScene currentProfileScene=null;
    Client client;
    private static ArrayList<Node> toRemove=new ArrayList<>();

    ProfileScene(Client client){
        this.client=client;
    }

    @Override
    public void init() {
        currentProfileScene=this;
        super.init();
        addProfile();
        setRelationship(InputReader.getClient().getRelationship(client),true);
    }

    public static ProfileScene getCurrentProfileScene() {
        return currentProfileScene;
    }

    public void setRelationship(Relationship relationship, boolean force) {
        removeAllNodesWithForce(force, toRemove, root.getChildren());
        Image image=null;
        int x=320,y=230;
        int setWidth=75,setHeight=75;
        if(relationship.isUnFriend()){
            try {
                image=new Image(new FileInputStream("Textures/UI/Icons/RedFirendIcon.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(relationship.isFriend()){
            try {
                image=new Image(new FileInputStream("Textures/UI/Icons/GreenFirendIcon.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            try {
                image=new Image(new FileInputStream("Textures/UI/Icons/YellowFirendIcon.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ImageView imageView=new ImageView(image);
        imageView.relocate(x,y);
        imageView.setFitHeight(setHeight);
        imageView.setFitWidth(setWidth);
        imageView.setOnMouseClicked(event -> {
            relationship.clientAccept(InputReader.getClient());
            setRelationship(InputReader.getClient().getRelationship(client),true);
            InputReader.getClient().updateRelationship(relationship);
        });
        addNodeWithForce(force, imageView);


        try {
            image=new Image(new FileInputStream("Textures/UI/Icons/CancelRelationshipButton.png"));
            ImageView anotherImageView=new ImageView(image);
            anotherImageView.relocate(x+100,y);
            anotherImageView.setFitHeight(setHeight);
            anotherImageView.setFitWidth(setWidth);
            anotherImageView.setOnMouseClicked(event -> {
                relationship.reject();
                setRelationship(InputReader.getClient().getRelationship(client),true);
                InputReader.getClient().updateRelationship(relationship);
            });
            addNodeWithForce(force, anotherImageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addNodeWithForce(boolean force, Node node) {
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


    private void addProfile() {
        try
        {
            ImageView profilePicture=new ImageView(new Image(
                    new FileInputStream("./Textures/Profile/"+client.getImageIndex()+".png")));
            profilePicture.setFitHeight(200);
            profilePicture.setFitWidth(200);
            profilePicture.relocate(100,100);
            root.getChildren().add(profilePicture);

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        FancyLabel nameLabel=new FancyLabel(client.getName(),40,300,100);
        root.getChildren().add(nameLabel.getNode());
        try
        {
            ImageView privateMessageImageView=new ImageView(new Image(
                    new FileInputStream("./Textures/Profile/pv.png")));
            privateMessageImageView.relocate(570,100);
            privateMessageImageView.setFitWidth(60);
            privateMessageImageView.setFitHeight(60);
            privateMessageImageView.setOnMouseClicked(event ->
            {
                Chatroom chatroom=InputReader.getClient().getPrivateChatroom(client);
                ChatroomScene.CHATROOM_SCENE.setChatroom(chatroom,true);
                InputReader.setScene(ChatroomScene.CHATROOM_SCENE.getScene());
            });
            root.getChildren().add(privateMessageImageView);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void initStartButtons() {
        super.initStartButtons();
        ImageView bearButton= null;
        try {
            bearButton = new ImageView(new Image(new FileInputStream("Textures/UI/Icons/BearIcon.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bearButton.relocate(520,215);
        bearButton.setOnMouseClicked(event -> {
            try {
                InputReader.getClient().attackWithBear(client);
            } catch (NotEnoughMoneyException e) {
                System.out.println(Constant.NOT_ENOUGH_MONEY_MESSAGE);
            } catch (NotInGameException e) {
                System.out.println(Constant.NOT_IN_GAME_EXCEPTION_MESSAGE);
            }
        });
        addNode(bearButton);
    }
}
