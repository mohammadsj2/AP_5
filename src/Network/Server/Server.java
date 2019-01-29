package Network.Server;

import Controller.InputReader;
import Network.Address;
import Network.Chatroom;
import Network.Client.Client;
import Exception.*;
import Network.Message;
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
    private HashMap<Integer, Formatter> formatters = new HashMap<>();
    private HashMap<Integer, Scanner> scanners = new HashMap<>();
    private Address address;
    private Chatroom globalChatroom;
    private ArrayList<ArrayList<Chatroom>> privateChatrooms = new ArrayList<>();
    private int currentPort;
    private YaGson yaGson = InputReader.getYaGson();

    public Server(Address address)
    {
        System.out.println("I'm server...");
        this.address = address;
        currentPort = address.getPort() + 1;
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
                        listenToClient(currentPort);
                        formatter.format(String.valueOf(currentPort) + "\n");
                        formatter.flush();
                        System.out.println("Port sent!");
                        Scanner scanner = new Scanner(inputStream);
                        String clientIp = scanner.nextLine();
                        socket.close();
                        setRequestStreams(clientIp, currentPort);
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
                    YaGson yaGson=new YaGsonBuilder().serializeSpecialFloatingPointValues().setExclusionStrategies(new YaGsonExclusionStrategy()).create();;
                    while (connected)
                    {
                        System.out.println("listen to client\n");
                        String inputCommand = scanner.nextLine();
                        String input;
                        System.out.println(inputCommand);
                        switch (inputCommand)
                        {
                            case "updateClient":
                                System.out.println("beforeInput");
                                input = scanner.nextLine();
                                System.out.println("afterInput");
                                System.out.println(input);
                                System.out.println();
                                input=input+"\n";
                                Client newClient = yaGson.fromJson(input, Client.class);//TODO az in khat rad nemishe !
                                System.out.println("afterInput");
                                try
                                {
                                    int clientId = getClientId(newClient);
                                    System.out.println("afterInput");
                                    clients.set(clientId, newClient);
                                    System.out.println("afterInput");
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    System.out.println("afterInput");
                                    addClient(newClient);
                                    client=newClient;
                                    System.out.println("afterInput");
                                }
                                System.out.println("afterInput");
                                break;
                            case "getGlobalChatroom":
                                formatter.format(yaGson.toJson(globalChatroom, Chatroom.class) + "\n");
                                formatter.flush();
                                break;
                            case "getScoreBoard":
                                formatter.format(yaGson.toJson(clients),
                                        new TypeToken<ArrayList<Client>>(){}.getType());
                                formatter.flush();
                                break;
                            case "getPrivateChatroom":
                                input = scanner.nextLine();
                                Client otherClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    Chatroom chatroom = privateChatrooms.get(getClientId(otherClient)).get(getClientId(client));
                                    formatter.format(yaGson.toJson(chatroom), Chatroom.class);
                                    formatter.format("\n");
                                    formatter.flush();
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    clientDoesNotExist.printStackTrace();
                                }
                                break;
                            case "disconnect":
                                disconnect(client);
                                connected=false;
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

    private void disconnect(Client client) throws ClientDoesNotExist {
        int id=getClientId(client);
        clients.remove(id);
        privateChatrooms.remove(id);
        for(ArrayList<Chatroom> chatrooms:privateChatrooms){
            chatrooms.remove(id);
        }
        System.out.println("Client "+client.getName()+" disconnected!");
    }

    private void addClient(Client client)
    {
        clients.add(client);

        ArrayList<Chatroom> tmpArrayList = new ArrayList<>();
        for (ArrayList<Chatroom> arrayList : privateChatrooms)
        {
            Chatroom chatroom = new Chatroom();
            arrayList.add(chatroom);
            tmpArrayList.add(chatroom);
        }
        tmpArrayList.add(new Chatroom());
        privateChatrooms.add(tmpArrayList);
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
        } catch (IOException e)
        {
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
}
