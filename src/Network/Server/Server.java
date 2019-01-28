package Network.Server;

import Controller.InputReader;
import Network.Address;
import Network.Chatroom;
import Network.Client.Client;
import Exception.*;
import Network.Message;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;
import org.junit.experimental.theories.Theories;

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
                        System.out.println("Client Connected!!!");
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        Formatter formatter = new Formatter(outputStream);
                        listenToClient(currentPort);
                        formatter.format(String.valueOf(currentPort) + "\n");
                        formatter.flush();
                        System.out.println("Port Sent!");
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
                    int id = 0;
                    while (true)
                    {
                        String inputCommand = scanner.nextLine();
                        String input;
                        switch (inputCommand)
                        {
                            case "updateClient":
                                input = scanner.nextLine();
                                Client newClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    int clientId = getClientId(newClient);
                                    clients.set(clientId, newClient);
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    addClient(newClient);
                                    id = clients.size() - 1;
                                }
                                break;
                            case "getGlobalChatroom":
                                formatter.format(yaGson.toJson(globalChatroom, Chatroom.class) + "\n");
                                formatter.flush();
                                break;
                            case "getScoreBoard":
                                formatter.format(yaGson.toJson(clients), new TypeToken<ArrayList<Client>>()
                                {
                                }.getType());
                                formatter.flush();
                                break;
                            case "getPrivateChatroom":
                                input = scanner.nextLine();
                                Client otherClient = yaGson.fromJson(input, Client.class);
                                try
                                {
                                    Chatroom chatroom = privateChatrooms.get(getClientId(otherClient)).get(id);
                                    formatter.format(yaGson.toJson(chatroom), Chatroom.class);
                                } catch (ClientDoesNotExist clientDoesNotExist)
                                {
                                    clientDoesNotExist.printStackTrace();
                                }
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
