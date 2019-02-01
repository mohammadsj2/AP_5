package Network.Client;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Model.Entity.Item;
import Model.WorkShop;
import Network.Address;
import Network.Chatroom;
import Network.CommonGameRequest;
import Network.Relationship;
import Network.Server.Server;
import View.Scene.GameScene;
import View.Scene.HelicopterScene;
import View.Scene.HelicopterScene;
import View.Scene.MultiPlayerScene.ChatroomScene;
import View.Scene.MultiPlayerScene.ProfileScene;
import View.Scene.MultiPlayerScene.ScoreBoardScene;
import View.Scene.TruckScene;
import YaGson.*;
import Exception.*;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Client {
    //server request to client on port
    //client request to server on port+1
    private String name;
    private String serverIP = null;
    private int level;
    private int imageIndex;
    private Address address;
    private boolean isInGame = false;
    private Scanner scanner;
    private int money = 0;
    private Formatter formatter;
    private YaGson yaGson = new YaGsonBuilder().serializeSpecialFloatingPointValues().setExclusionStrategies(new YaGsonExclusionStrategyForServer()).create();
    private CommonGameRequest commonGameRequest = null;

    public Client(String name, int imageIndex) {
        address = new Address(1231, "localhost");
        this.name = name;
        level = 0;
        this.imageIndex = imageIndex;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    private void addBear() throws NotInGameException {
        Scene scene = InputReader.getScene();
        if (scene != GameScene.getScene() && scene != HelicopterScene.getScene() && scene != TruckScene.getScene()) {
            throw new NotInGameException();
        }
        Platform.runLater(() -> InputReader.buy("bear"));
    }

    public void connectToServer(String ip) throws ServerDoesNotExist, NotUniqueUsernameException {
        serverIP = ip;
        try {
            Socket socket = new Socket(serverIP, 8060);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            Formatter formatter = new Formatter(outputStream);

            formatter.format(getName() + "\n");
            formatter.flush();
            if (!scanner.nextLine().equals("sendingPort")) {
                throw new NotUniqueUsernameException();
            }
            int listenPort = scanner.nextInt();
            address = new Address(listenPort, "localhost");
            listenToServer(address.getPort());
            formatter.format(address.getIp() + "\n");
            formatter.flush();
            scanner.nextLine();
            socket.close();
            Socket socket1 = new Socket(serverIP, listenPort + 1);
            this.scanner = new Scanner(socket1.getInputStream());
            this.formatter = new Formatter(socket1.getOutputStream());

            updateClient();
        } catch (IOException e) {
            throw new ServerDoesNotExist();
        }
    }

    private void listenToServer(int port) {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    Scanner scanner = new Scanner(inputStream);
                    Formatter formatter = new Formatter(outputStream);
                    CommonGameRequest commonGameRequest2;
                    while (true) {
                        System.out.println("LISTEN TO SERVER ON PORT " + port);
                        String commandInput = scanner.nextLine();
                        String input;
                        System.out.println(commandInput + "|||||||||||||||||||");
                        switch (commandInput) {
                            case "updateChatroom":
                                input = scanner.nextLine();
                                if (InputReader.getScene() != ChatroomScene.CHATROOM_SCENE.getScene()) {
                                    break;
                                }
                                Chatroom chatroom = yaGson.fromJson(input, Chatroom.class);
                                System.out.println("CLIENT!!!: " + chatroom.getMessages().size());
                                if (ChatroomScene.CHATROOM_SCENE.getChatroom().equals(chatroom)) {
                                    ChatroomScene.CHATROOM_SCENE.setChatroom(chatroom, false);
                                }
                                System.out.println("UPDATE CHATROOM FINISHED!");
                                break;
                            case "updateScoreboard":
                                input = scanner.nextLine();
                                if (InputReader.getScene() != ScoreBoardScene.SCORE_BOARD_SCENE.getScene()) {
                                    break;
                                }
                                ArrayList<Client> clients = yaGson.fromJson(input, new TypeToken<ArrayList<Client>>() {
                                }.getType());
                                ScoreBoardScene.SCORE_BOARD_SCENE.setClients(clients, false);
                                break;
                            case "updateRelationship":
                                input = scanner.nextLine();
                                Relationship relationship = yaGson.fromJson(input, Relationship.class);
                                ProfileScene profileScene = ProfileScene.getCurrentProfileScene();
                                if (profileScene == null || InputReader.getScene() != profileScene.getScene()) {
                                    break;
                                }
                                profileScene.setRelationship(relationship, false);
                                break;
                            case "addBear":
                                try {
                                    addBear();
                                    formatter.format("bearAdded\n");
                                } catch (NotInGameException e) {
                                    formatter.format("notInGame\n");
                                }
                                formatter.flush();
                                break;
                            case "updateMarketItems":
                                input = scanner.nextLine();
                                HashMap<Item, Integer> items = arrayListToHashMap(yaGson.fromJson(input,
                                        new TypeToken<ArrayList<Item>>() {
                                        }.getType()));
                                HelicopterScene.setItems(items);
                                break;
                            case "askWatch":
                                input=scanner.nextLine();
                                int currentWatchPort=new Integer(input);
                                //Build serverSocket
                                ServerSocket serverSocket1=new ServerSocket(currentWatchPort);
                                formatter.format("Succeed\n");
                                formatter.flush();
                                Socket socket1=serverSocket1.accept();


                                break;
                            case "stopWatch":
                                break;
                            case "commonGameRequest":
                                input = scanner.nextLine();
                                commonGameRequest2 = yaGson.fromJson(input, CommonGameRequest.class);
                                if (isInGame()) {
                                    formatter.format("rejected\n");
                                    formatter.flush();
                                } else {
                                    formatter.format("accepted\n");
                                    formatter.flush();
                                    commonGameRequest = commonGameRequest2;
                                    runCommonGame(commonGameRequest2);
                                }
                                break;
                            case "getCommonGameController":
                                input=scanner.nextLine();
                                commonGameRequest2 = yaGson.fromJson(input, CommonGameRequest.class);
                                if (commonGameRequest.equals(commonGameRequest2)) {
                                    formatter.format(yaGson.toJson(InputReader.getCurrentController()) + "\n");
                                } else {
                                    formatter.format(yaGson.toJson(new Controller(0, 0, new ArrayList<>())));
                                }
                                formatter.flush();
                                break;
                            case "winCommonGame":
                                input=scanner.nextLine();
                                commonGameRequest2 = yaGson.fromJson(input, CommonGameRequest.class);
                                if (commonGameRequest.equals(commonGameRequest2)) {
                                    Platform.runLater(() ->InputReader.getCurrentController().win());
                                    setCommonGameRequest(null);
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void runCommonGame(CommonGameRequest commonGameRequest) {
        Controller controller = new Controller(commonGameRequest.getInitialMoney(),
                commonGameRequest.getGoalMoney(), commonGameRequest.getGoalEntities());
        try {
            WorkShop workShop = InputReader.getWorkShop("EggPowderPlant");
            WorkShop workShop2 = InputReader.getWorkShop("CookieBakery");
            WorkShop workShop3 = InputReader.getWorkShop("CakeBakery");
            WorkShop workShop4 = InputReader.getWorkShop("Spinnery");
            WorkShop workShop5 = InputReader.getWorkShop("WeavingFactory");
            WorkShop workShop6 = InputReader.getWorkShop("SewingFactory");
            workShop.setLocation(0);
            workShop2.setLocation(1);
            workShop3.setLocation(2);
            workShop4.setLocation(3);
            workShop5.setLocation(4);
            workShop6.setLocation(5);
            controller.addWorkshop(workShop);
            controller.addWorkshop(workShop2);
            controller.addWorkshop(workShop3);
            controller.addWorkshop(workShop4);
            controller.addWorkshop(workShop5);
            controller.addWorkshop(workShop6);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputReader.runAController(controller, false);
    }

    public void askWatch(Client client)
    {
        formatter.format("askWatch\n");
        formatter.format(yaGson.toJson(client,Client.class)+"\n");
        formatter.flush();
        int currentWatchPort=new Integer(scanner.nextLine());
        //Build Socket

    }

    public void stopWatch(Client client)
    {
        formatter.format("stopWatch\n");
        formatter.format(yaGson.toJson(client,Client.class)+"\n");
        formatter.flush();
    }


    public void attackWithBear(Client client) throws NotEnoughMoneyException,NotInGameException {
        if(InputReader.getCurrentController()==null){
            throw new NotInGameException();
        }
        InputReader.getCurrentController().subtractMoney(Constant.SERVER_BEAR_COST);
        formatter.format("attackWithBear\n");
        formatter.format(yaGson.toJson(client) + "\n");
        formatter.flush();
        String input = scanner.nextLine();
        if (input.equals("targetClientNotInGame")) {
            InputReader.getCurrentController().increaseMoney(Constant.SERVER_BEAR_COST);
            throw new NotInGameException();
        }
    }


    public boolean isOnline() {
        return serverIP != null;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void updateClient() {
        formatter.format("updateClient\n");
        formatter.format(yaGson.toJson(this) + "\n");
        formatter.flush();
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Chatroom getGlobalChatroom() {
        formatter.format("getGlobalChatroom\n");
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), Chatroom.class);
    }

    public ArrayList<Client> getScoreBoard() {
        formatter.format("getScoreBoard\n");
        formatter.flush();
        String input = scanner.nextLine();
        ArrayList<Client> clients = yaGson.fromJson(input, new TypeToken<ArrayList<Client>>() {
        }.getType());
        return clients;
    }

    public Chatroom getPrivateChatroom(Client client) {
        formatter.format("getPrivateChatroom\n");
        formatter.format(yaGson.toJson(client) + "\n");
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), Chatroom.class);
    }

    public void disconnect() {
        formatter.format("disconnect\n");
        formatter.flush();
        setServerIP(null);
    }

    public Address getAddress() {
        return address;
    }

    public void sendChatroom(Chatroom chatroom) {
        formatter.format("updateChatroom\n");
        formatter.format(yaGson.toJson(chatroom) + "\n");
        formatter.flush();
    }

    public Relationship getRelationship(Client client) {
        formatter.format("getRelationship\n");
        formatter.format(yaGson.toJson(client) + "\n");
        formatter.flush();
        return yaGson.fromJson(scanner.nextLine(), Relationship.class);
    }

    public void updateRelationship(Relationship relationship) {
        formatter.format("updateRelationship\n");
        formatter.format(yaGson.toJson(relationship) + "\n");
        formatter.flush();
    }

    public HashMap<Item, Integer> getMarketItems() {
        formatter.format("getMarketItems\n");
        formatter.flush();
        ArrayList<Item> tmp = yaGson.fromJson(scanner.nextLine(), new TypeToken<ArrayList<Item>>() {
        }.getType());
        return arrayListToHashMap(tmp);
    }

    public void removeItems(HashMap<Item, Integer> items) throws NotEnoughItemException {
        formatter.format("removeMarketItems\n");
        ArrayList<Item> itemsArrayList = hashMapToArrayList(items);
        formatter.format(yaGson.toJson(itemsArrayList, new TypeToken<ArrayList<Item>>() {
        }.getType()) + "\n");
        formatter.flush();
        if (scanner.nextLine().equals("Failed"))
            throw new NotEnoughItemException();
    }


    public void addMarketItems(HashMap<Item, Integer> items) {
        formatter.format("addMarketItems\n");
        ArrayList<Item> itemsArrayList = hashMapToArrayList(items);
        System.out.println(itemsArrayList.size());
        formatter.format(yaGson.toJson(itemsArrayList, new TypeToken<ArrayList<Item>>() {
        }.getType()) + "\n");
        formatter.flush();
    }

    private HashMap<Item, Integer> arrayListToHashMap(ArrayList<Item> items) {
        HashMap<Item, Integer> result = new HashMap<>();
        for (Item item : items) {
            if (!result.containsKey(item)) {
                result.put(item, 1);
            } else {
                result.put(item, result.get(item) + 1);
            }
        }
        return result;
    }

    private ArrayList<Item> hashMapToArrayList(HashMap<Item, Integer> items) {
        ArrayList<Item> result = new ArrayList<>();
        for (Item item : items.keySet()) {
            for (int i = 0; i < items.get(item); i++) {
                result.add(item);
            }
        }
        return result;
    }

    public int getBuyCost(Item item) {
        formatter.format("getBuyCost\n");
        formatter.format(yaGson.toJson(item, Item.class) + "\n");
        formatter.flush();
        return new Integer(scanner.nextLine());
    }

    public int getSellCost(Item item) {
        formatter.format("getSellCost\n");
        formatter.format(yaGson.toJson(item, Item.class) + "\n");
        formatter.flush();
        return new Integer(scanner.nextLine());
    }

    public boolean commonGameRequest(Client client) {
        formatter.format("commonGameRequest\n");
        formatter.format(yaGson.toJson(client) + "\n");
        formatter.flush();
        String input = scanner.nextLine();
        if (input.equals("rejected")) {
            return false;
        }
        return true;
    }

    public void checkLevel() {
        formatter.format("checkCommonGame\n");
        formatter.format(yaGson.toJson(commonGameRequest) + "\n");
        formatter.flush();
    }

    public void setCommonGameRequest(CommonGameRequest commonGameRequest) {
        this.commonGameRequest = commonGameRequest;
    }
}