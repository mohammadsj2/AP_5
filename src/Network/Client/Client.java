package Network.Client;

import Network.Address;
import Network.Server.Server;
import javafx.scene.image.Image;

import java.util.Objects;

public class Client {
    private String name;
    private Server server;
    private int level;
    private int imageIndex;
    private Address address;
    private boolean isInGame=false;


    public Client(String name) {
        this.name = name;
        level=0;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public void setServer(Server server) {
        this.server = server;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public int getLevel() {
        return level;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name);
    }

}
