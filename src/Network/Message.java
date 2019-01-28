package Network;

import Network.Client.Client;

import java.util.ArrayList;

public class Message {
    String text;
    Client clients;

    public Message(Client clients, String text) {
        this.text = text;
        this.clients = clients;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Client getClients() {
        return clients;
    }

    public void setClients(Client clients) {
        this.clients = clients;
    }
}
