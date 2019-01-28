package Network.Server;

import Network.Address;
import Network.Chatroom;
import Network.Client.Client;
import sun.misc.Cleaner;
import Exception.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    private ArrayList<Client> clients=new ArrayList<>();
    private Address address;
    private Chatroom globalChatroom;
    private ArrayList<ArrayList<Chatroom>> privateChatrooms=new ArrayList<>();

    public Address getAddress() {
        return address;
    }

    public Chatroom getGlobalChatroom() {
        return globalChatroom;
    }
    private int getClientId(Client client) throws ClientDoesNotExist {
        for (int i = 0; i < clients.size(); i++) {
            Client client1 = clients.get(i);
            if (client1.equals(client)) {
                return i;
            }
        }
        throw new ClientDoesNotExist();
    }
    public Chatroom getPrivateChatroom(Client a,Client b) throws ClientDoesNotExist {
        int i=getClientId(a);
        int j=getClientId(b);
        return privateChatrooms.get(i).get(j);
    }
}
