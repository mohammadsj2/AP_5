package Network;

import Network.Client.Client;

public class Message {
    String text;
    Client client;

    public Message(Client client, String text) {
        this.text = text;
        this.client = client;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
