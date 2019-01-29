package View.Scene.MultiPlayerScene;

import Network.Client.Client;
import javafx.scene.control.Label;

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
        Label label=new Label(client.getName());
        label.relocate(100,100);
    }
}
