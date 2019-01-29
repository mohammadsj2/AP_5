package View.Scene.MultiPlayerScene;

import Controller.InputReader;
import Network.Chatroom;
import Network.Client.Client;
import View.Label.FancyLabel;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProfileScene extends MultiPlayerScene{
    Client client;
    public ProfileScene(Client client){
        this.client=client;
    }

    @Override
    public void init() {
        super.init();
        addProfile();
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
                ChatroomScene.CHATROOM_SCENE.init(chatroom);
                InputReader.setScene(ChatroomScene.CHATROOM_SCENE.getScene());
            });
            root.getChildren().add(privateMessageImageView);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
