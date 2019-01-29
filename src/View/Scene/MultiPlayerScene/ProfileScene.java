package View.Scene.MultiPlayerScene;

import Network.Client.Client;
import View.Label.FancyLabel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
            System.out.println(client.getImageIndex());
            ImageView profilePicture=new ImageView(new Image(
                    new FileInputStream("./Textures/Avatar/"+client.getImageIndex()+".png")));
            profilePicture.setFitHeight(200);
            profilePicture.setFitWidth(200);
            profilePicture.relocate(100,100);
            root.getChildren().add(profilePicture);

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        FancyLabel nameLabel=new FancyLabel(client.getName(),40,320,100);
        root.getChildren().add(nameLabel.getNode());
    }
}
