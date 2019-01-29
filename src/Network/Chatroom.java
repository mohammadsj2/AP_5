package Network;

import Network.Client.Client;

import java.util.ArrayList;

public class Chatroom {
    ArrayList<Message> messages=new ArrayList<>();
    boolean isGlobal;
    Client firstClient,secondClient;

    public Chatroom()
    {
        isGlobal=true;
    }

    public Chatroom(Client firstClient,Client secondClient)
    {
        isGlobal=false;
        this.firstClient=firstClient;
        this.secondClient=secondClient;
    }

}
