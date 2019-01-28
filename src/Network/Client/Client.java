package Network.Client;

import Controller.InputReader;
import Network.Address;
import Network.Chatroom;
import Network.Server.Server;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Objects;
import java.util.Scanner;

public class Client
{
    //server request to client on port
    //client request to server on port+1
    private String name;
    private String serverIP = null;
    private int level;
    private int imageIndex;
    private Address address;
    private boolean isInGame = false;
    private Socket socket;
    private Scanner scanner;
    private Formatter formatter;

    public Client(String name)
    {
        this.name = name;
        level = 0;
    }

    public void connectToServer(String ip)
    {
        serverIP = ip;
        try
        {
            Socket socket = new Socket(serverIP, 8060);
            OutputStream outputStream = socket.getOutputStream();
       //     System.out.println(outputStream);
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            Formatter formatter = new Formatter(outputStream);
      //      System.out.println(formatter);
            int listenPort = scanner.nextInt();
            address=new Address(listenPort,"localhost");
            listenToServer(address.getPort());
      //      System.out.println(port);
      //      listenToServer(port);
            formatter.format(address.getIp() + "\n");
            formatter.flush();
            socket.close();
            this.socket = new Socket(serverIP, listenPort + 1);
            this.scanner = new Scanner(this.socket.getInputStream());
            this.formatter = new Formatter(this.socket.getOutputStream());

            updateClient();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void listenToServer(int port)
    {
        Task task = new Task<Void>()
        {
            @Override
            public Void call()
            {
                try
                {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    Scanner scanner = new Scanner(inputStream);
                    Formatter formatter = new Formatter(outputStream);

                    while (true)
                    {
                        String input = scanner.nextLine();
                        switch (input)
                        {
                            //TODO
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

    public boolean isOnline()
    {
        return serverIP != null;
    }

    public void setServerIP(String serverIP)
    {
        this.serverIP = serverIP;
    }

    private void updateClient()
    {
        YaGson yaGson = InputReader.getYaGson();
        formatter.format("updateClient\n");
        formatter.format(yaGson.toJson(this));
        formatter.flush();
    }

    public boolean isInGame()
    {
        return isInGame;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }


    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setImageIndex(int imageIndex)
    {
        this.imageIndex = imageIndex;
    }

    public String getName()
    {
        return name;
    }


    public int getLevel()
    {
        return level;
    }

    public int getImageIndex()
    {
        return imageIndex;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    public Chatroom getGlobalChatroom()
    {
        YaGson yaGson = InputReader.getYaGson();
        formatter.format("getGlobalChatroom\n");
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), Chatroom.class);
    }

    public ArrayList<Client> getScoreBoard()
    {
        YaGson yaGson = InputReader.getYaGson();
        formatter.format("getScoreBoard\n");
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), new TypeToken<ArrayList<Client>>()
        {
        }.getType());
    }

    public Chatroom getPrivateChatroom(Client client)
    {
        YaGson yaGson = InputReader.getYaGson();
        formatter.format("getPrivateChatroom\n");
        formatter.format(yaGson.toJson(client));
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), Chatroom.class);
    }

}
