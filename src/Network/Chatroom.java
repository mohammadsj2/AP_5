package Network;

import Network.Client.Client;

import java.util.ArrayList;
import java.util.Objects;

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

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public boolean isGlobal()
    {
        return isGlobal;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        if(isGlobal && chatroom.isGlobal)return true;
        else if(isGlobal || chatroom.isGlobal)return false;
        return firstClient==chatroom.firstClient && secondClient==chatroom.secondClient;

    }

    public ArrayList<Message> getMessages()
    {
        ArrayList<Message> resultMessages=new ArrayList<>();
        for(int i=messages.size()-1;i>=Math.max(0,messages.size()-13);i--)
        {
            resultMessages.add(messages.get(i));
        }
        return resultMessages;
    }
}
