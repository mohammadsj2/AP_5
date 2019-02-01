package Network.Server;

import Constant.Constant;
import Model.Entity.Item;
import Network.Address;
import Network.Chatroom;
import Network.Client.Client;
import Exception.*;
import Network.Relationship;
import YaGson.*;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server
{
    private ArrayList<Client> clients = new ArrayList<>();
    private HashMap<Integer, Formatter> formatters = new HashMap<>();// key-> port, value-> formatter
    private HashMap<Integer, Scanner> scanners = new HashMap<>();
    private Address address;
    private Chatroom globalChatroom;
    private ArrayList<ArrayList<Chatroom>> privateChatrooms = new ArrayList<>();
    private ArrayList<ArrayList<Relationship>> relationShips=new ArrayList<>();
    private int currentPort,currentWatchPort;
    private YaGson yaGson = new YaGsonBuilder().serializeSpecialFloatingPointValues().setExclusionStrategies(new YaGsonExclusionStrategyForServer()).create();;
    private HashMap<Item,Integer> shopItems;
    private HashMap<Item,Integer> buyCosts, sellCosts;

    public Server(Address address)
    {
        initShop();
        initItemCosts();
        System.out.println("I'm server...");
        this.address = address;
        currentPort = address.getPort() + 1;
        currentWatchPort=7070;
        globalChatroom = new Chatroom();
        Task task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                ServerSocket serverSocket = null;
                try
                {
                    serverSocket = new ServerSocket(address.getPort());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                while (true)
                {
                    try
                    {
                        Socket socket = serverSocket.accept();
                        System.out.println("Client connected!!!");
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        Formatter formatter = new Formatter(outputStream);
                        Scanner scanner = new Scanner(inputStream);

                        String name=scanner.nextLine();
                        boolean nameExist=false;
                        for(Client client:clients){
                            if(client.getName().equals(name)){
                                nameExist=true;
                            }
                        }
                        if(nameExist){
                            formatter.format("your handle isn't unique!\n");
                            formatter.flush();
                            socket.close();
                            continue;
                        }
                        formatter.format("sendingPort\n");
                        formatter.format(String.valueOf(currentPort) + "\n");
                        formatter.flush();
                        System.out.println("PortSent");

                        String clientIp = scanner.nextLine();
                        setRequestStreams(clientIp, currentPort);
                        listenToClient(currentPort);

                        formatter.format("setRequestStreams\n");
                        formatter.flush();

                        socket.close();

                        currentPort += 2;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(task).start();
    }

    private void initItemCosts()
    {
        buyCosts =new HashMap<>();
        sellCosts =new HashMap<>();
        ArrayList<Item> items=new ArrayList<>();
        items.add(Constant.getItemByType("egg"));
        items.add(Constant.getItemByType("flour"));
        items.add(Constant.getItemByType("cake"));
        items.add(Constant.getItemByType("flourycake"));
        items.add(Constant.getItemByType("wool"));
        items.add(Constant.getItemByType("sewing"));
        items.add(Constant.getItemByType("adornment"));
        items.add(Constant.getItemByType("fabric"));
        items.add(Constant.getItemByType("milk"));
        for(Item item:items)
        {
            buyCosts.put(item,item.getDefaultBuyCost());
            sellCosts.put(item,item.getDefaultSellCost());
        }
    }

    private void initShop()
    {
        shopItems =new HashMap<>();
        shopItems.put(Constant.getItemByType("egg"),10);
        shopItems.put(Constant.getItemByType("flour"),10);
        shopItems.put(Constant.getItemByType("cake"),10);
        shopItems.put(Constant.getItemByType("flourycake"),10);
        shopItems.put(Constant.getItemByType("wool"),10);
        shopItems.put(Constant.getItemByType("sewing"),10);
        shopItems.put(Constant.getItemByType("fabric"),10);
        shopItems.put(Constant.getItemByType("adornment"),10);
        shopItems.put(Constant.getItemByType("milk"),10);
    }

    private void listenToClient(int port)
    {
        Task task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                try
                {
                    ServerSocket serverSocket = new ServerSocket(port + 1);
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    OutputStream outputStream = socket.getOutputStream();
                    Formatter formatter = new Formatter(outputStream);
                    Client client=null;
                    boolean connected=true;
                    Client otherClient;

                    while (connected)
                    {
                        System.out.println("listen to client\n");
                        String inputCommand = scanner.nextLine();
                        String input;
                        System.out.println(inputCommand);
                        switch (inputCommand)
                        {
                            case "updateClient":
                                input = scanner.nextLine();
                                input=input+"\n";
                                Client newClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    System.out.println(clients.size());
                                    int clientId = getClientId(newClient);
                                    clients.set(clientId, newClient);
                                    updateScoreBoard();
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    System.out.println("ADD CLIENT");
                                    addClient(newClient);
                                    client=newClient;
                                }
                                break;
                            case "getGlobalChatroom":
                                formatter.format(yaGson.toJson(globalChatroom, Chatroom.class) + "\n");
                                formatter.flush();
                                break;
                            case "getScoreBoard":
                                formatter.format(yaGson.toJson(clients,new TypeToken<ArrayList<Client>>(){}.getType())+"\n");
                                formatter.flush();
                                break;
                            case "getPrivateChatroom":
                                input = scanner.nextLine();
                                otherClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    Chatroom chatroom = privateChatrooms.get(getClientId(otherClient)).get(getClientId(client));
                                    formatter.format(yaGson.toJson(chatroom,Chatroom.class)+"\n");
                                    formatter.flush();
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    clientDoesNotExist.printStackTrace();
                                }
                                break;
                            case "updateChatroom":
                                input=scanner.nextLine();
                                Chatroom chatroom= yaGson.fromJson(input,Chatroom.class);
                                System.out.println(chatroom.isGlobal());
                                if(chatroom.isGlobal())
                                {
                                    globalChatroom = chatroom;
                                    for(Client client1:clients)
                                        sendChatroom(client1,chatroom);
                                } else
                                {
                                    int id1=getClientId(chatroom.getFirstClient());
                                    int id2=getClientId(chatroom.getSecondClient());
                                    privateChatrooms.get(id1).set(id2,chatroom);
                                    privateChatrooms.get(id2).set(id1,chatroom);
                                    //  System.out.println(privateChatrooms.get(0).get(1).getMessages().size());
                                    System.out.println(chatroom.getFirstClient().getName()+" "
                                            +chatroom.getSecondClient().getName());
                                    sendChatroom(chatroom.getFirstClient(),chatroom);
                                    if(!chatroom.getFirstClient().equals(chatroom.getSecondClient()))
                                    {
                                        sendChatroom(chatroom.getSecondClient(), chatroom);
                                    }
                                }
                                break;
                            case "getMarketItems":
                                formatter.format(yaGson.toJson(hashMapToArrayList(shopItems)
                                        ,new TypeToken<ArrayList<Item>>(){}.getType())+"\n");
                                formatter.flush();
                                break;
                            case "removeMarketItems":
                                System.out.println("Server: removeMarketItems");
                                input=scanner.nextLine();
                                ArrayList<Item> tmp=yaGson.fromJson(input
                                        ,new TypeToken<ArrayList<Item>>(){}.getType());
                                HashMap<Item,Integer> itemsToRemove=arrayListToHashMap(tmp);
                                boolean ok=true;
                                for(Item item:itemsToRemove.keySet())
                                {
                                    if(!shopItems.containsKey(item) || itemsToRemove.get(item)>shopItems.get(item))
                                    {
                                        ok=false;
                                        break;
                                    }
                                }
                                if(ok)
                                {
                                    for(Item item:itemsToRemove.keySet())
                                    {
                                        shopItems.put(item,shopItems.get(item)-itemsToRemove.get(item));
                                    }
                                    formatter.format("Succeed\n");
                                    formatter.flush();
                                    updateMarketItems();
                                }
                                else
                                {
                                    System.out.println("SERVER: FAILLLLLLLLLL");
                                    formatter.format("Failed\n");
                                    formatter.flush();
                                }
                                break;
                            case "addMarketItems":
                                input=scanner.nextLine();
                                ArrayList<Item> itemsToAdd=yaGson.fromJson(input
                                        ,new TypeToken<ArrayList<Item>>(){}.getType());
                                System.out.println(itemsToAdd.size());
                                for(Item item:itemsToAdd)
                                {
                                    if(!shopItems.containsKey(item))
                                    {
                                        shopItems.put(item,1);
                                    }
                                    else
                                    {
                                        System.out.println("ADD "+item.getName());
                                        shopItems.put(item,shopItems.get(item)+1);
                                    }
                                }
                                updateMarketItems();
                                break;
                            case "disconnect":
                                disconnect(client);
                                connected=false;
                                break;
                            case "getRelationship":
                                input = scanner.nextLine();
                                otherClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    Relationship relationship = relationShips.get(getClientId(otherClient)).get(getClientId(client));
                                    formatter.format(yaGson.toJson(relationship, Relationship.class)+"\n");
                                    formatter.flush();
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    clientDoesNotExist.printStackTrace();
                                }
                                break;
                            case "updateRelationship":
                                input = scanner.nextLine();
                                Relationship relationship = yaGson.fromJson(input,Relationship.class);
                                updateRelationship(relationship);
                                break;
                            case "attackWithBear":
                                otherClient=yaGson.fromJson(scanner.nextLine(),Client.class);
                                try {
                                    addBear(otherClient);
                                    formatter.format("bearAdded\n");
                                }catch (NotInGameException e){
                                    System.out.println("az server error ferestade shod");
                                    formatter.format("targetClientNotInGame\n");
                                }
                                formatter.flush();
                                break;
                            case "getBuyCost":
                                input=scanner.nextLine();
                                Item item=yaGson.fromJson(input,Item.class);
                                formatter.format(String.valueOf(buyCosts.get(item))+"\n");
                                formatter.flush();
                                break;
                            case "getSellCost":
                                input=scanner.nextLine();
                                item=yaGson.fromJson(input,Item.class);
                                formatter.format(String.valueOf(sellCosts.get(item))+"\n");
                                formatter.flush();
                                break;
                            case "askWatch":
                                input=scanner.nextLine();
                                Client client1=yaGson.fromJson(input,Client.class);
                                currentWatchPort++;
                                askWatch(client1);
                                formatter.format(String.valueOf(currentWatchPort)+"\n");
                                formatter.flush();
                                scanner.nextLine();
                                watchSocketBuilt(client1);
                                break;
                            case "stopWatch":
                                input=scanner.nextLine();
                                Client client2=yaGson.fromJson(input,Client.class);
                                stopWatch(client2);
                                break;
                        }
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void stopWatch(Client client)
    {
        Formatter formatter=formatters.get(client.getAddress().getPort());
        formatter.format("stopWatch\n");
        formatter.flush();
    }

    private void watchSocketBuilt(Client client)
    {
        Formatter formatter=formatters.get(client.getAddress().getPort());
        formatter.format("socketBuilt\n");
        formatter.flush();
    }

    private void askWatch(Client client)
    {
        Formatter formatter=formatters.get(client.getAddress().getPort());
        Scanner scanner=scanners.get(client.getAddress().getPort());
        formatter.format("askWatch\n");
        formatter.format(String.valueOf(currentWatchPort)+"\n");
        formatter.flush();
        scanner.nextLine();// serverSocket Built
    }

    private void updateMarketItems()
    {
        String marketItemsToJson= yaGson.toJson(hashMapToArrayList(shopItems), new TypeToken<ArrayList<Item>>(){}.getType())+"\n";
        for (Client client : clients) {
            Formatter formatter = formatters.get(client.getAddress().getPort());
            formatter.format("updateMarketItems\n");
            formatter.format(marketItemsToJson);
            formatter.flush();
        }
    }



    private void sendChatroom(Client client, Chatroom chatroom)
    {
        Formatter formatter=formatters.get(client.getAddress().getPort());
        System.out.println(client.getAddress().getPort());
        formatter.format("updateChatroom\n");
        formatter.format(yaGson.toJson(chatroom,Chatroom.class)+"\n");
        System.out.println("Chatroom Sent!");
        formatter.flush();

    }

    private void disconnect(Client client) throws ClientDoesNotExist {
        int id=getClientId(client);
        clients.remove(id);
        privateChatrooms.remove(id);
        relationShips.remove(id);

        for(ArrayList<Chatroom> chatrooms:privateChatrooms){
            chatrooms.remove(id);
        }
        for(ArrayList<Relationship> relationShip:relationShips){
            relationShip.remove(id);
        }
        updateScoreBoard();
        System.out.println("Client "+client.getName()+" disconnected!");
    }
    private void updateRelationship(Relationship relationShip) throws ClientDoesNotExist {
        System.out.println("this ");
        Client[] clients={relationShip.getFirstClient(),relationShip.getSecondClient()};
        int id[]={getClientId(clients[0]),getClientId(clients[1])};
        for(int i=0;i<2;i++){
            relationShips.get(id[i]).set(id[1-i],relationShip);
        }
        for(int i=0;i<2;i++){
            sendUpdatedRelationship(clients[i],relationShip);
        }
    }

    private void sendUpdatedRelationship(Client client,Relationship relationShip) {
        System.out.println("sending updated relationship to "+client.getName());
        Formatter formatter=formatters.get(client.getAddress().getPort());
        formatter.format("updateRelationship\n");
        formatter.format(yaGson.toJson(relationShip)+"\n");
        formatter.flush();
    }
    private void addBear(Client targetClient) throws NotInGameException {
        Formatter formatter=formatters.get(targetClient.getAddress().getPort());
        Scanner scanner=scanners.get(targetClient.getAddress().getPort());

        formatter.format("addBear\n");
        formatter.flush();

        String input=scanner.nextLine();
        System.out.println("beserver resid: "+input);
        if(!input.equals("bearAdded")){
           throw new NotInGameException();
        }
    }

    private void updateScoreBoard() {
        String clientsToJson= yaGson.toJson(clients, new TypeToken<ArrayList<Client>>(){}.getType())+"\n";
        System.out.println("updateSB\n");
        for (Client client : clients) {
            Formatter formatter = formatters.get(client.getAddress().getPort());
            formatter.format("updateScoreboard\n");
            formatter.format(clientsToJson);
            formatter.flush();
        }
    }


    private void addClient(Client client)
    {
        clients.add(client);

        ArrayList<Chatroom> tmpChatroomArrayList = new ArrayList<>();
        ArrayList<Relationship> tmpRelationships =new ArrayList<>();
        for (int i=0;i<privateChatrooms.size();i++)
        {
            ArrayList<Chatroom> chatroomArrayList=privateChatrooms.get(i);
            ArrayList<Relationship> relationshipArrayList =relationShips.get(i);

            Chatroom chatroom = new Chatroom(clients.get(i),client);
            Relationship relationShip=new Relationship(clients.get(i),client);

            chatroomArrayList.add(chatroom);
            tmpChatroomArrayList.add(chatroom);

            relationshipArrayList.add(relationShip);
            tmpRelationships.add(relationShip);
        }
        tmpChatroomArrayList.add(new Chatroom(client,client));
        tmpRelationships.add(new Relationship(client,client));

        privateChatrooms.add(tmpChatroomArrayList);
        relationShips.add(tmpRelationships);
        updateScoreBoard();
    }

    private void setRequestStreams(String ip, int port)
    {
        try
        {
            Socket socket = new Socket(ip, port);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            scanners.put(port, new Scanner(inputStream));
            formatters.put(port, new Formatter(outputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Address getAddress()
    {
        return address;
    }

    public Chatroom getGlobalChatroom()
    {
        return globalChatroom;
    }

    private int getClientId(Client client) throws ClientDoesNotExist
    {
        for (int i = 0; i < clients.size(); i++)
        {
            Client client1 = clients.get(i);
            if (client1.equals(client))
            {
                return i;
            }
        }
        throw new ClientDoesNotExist();
    }

    public Chatroom getPrivateChatroom(Client a, Client b) throws ClientDoesNotExist
    {
        int i = getClientId(a);
        int j = getClientId(b);
        return privateChatrooms.get(i).get(j);
    }

    public int getBuyCost(Item item)
    {
        return buyCosts.get(item);
    }

    public int getSellCost(Item item)
    {
        return sellCosts.get(item);
    }

    private HashMap<Item,Integer> arrayListToHashMap(ArrayList<Item> items)
    {
        HashMap<Item,Integer> result=new HashMap<>();
        for(Item item:items)
        {
            if(!result.containsKey(item))
            {
                result.put(item,1);
            }
            else
            {
                result.put(item,result.get(item)+1);
            }
        }
        return result;
    }

    private ArrayList<Item> hashMapToArrayList(HashMap<Item, Integer> items)
    {
        ArrayList<Item> result=new ArrayList<>();
        for(Item item:items.keySet())
        {
            for(int i=0;i<items.get(item);i++)
            {
                result.add(item);
            }
        }
        return result;
    }

    public void setBuyCosts(HashMap<Item, Integer> buyCosts)
    {
        this.buyCosts=buyCosts;
    }

    public void setSellCosts(HashMap<Item, Integer> sellCosts)
    {
        this.sellCosts=sellCosts;
    }
}
