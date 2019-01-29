package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Network.Chatroom;
import Network.Client.Client;
import Network.Message;
import View.Button.BlueButton;
import View.Scene.ConnectScene;
import View.Scene.MenuScene;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;

public class ChatroomScene
{
    public final static ChatroomScene CHATROOM_SCENE=new ChatroomScene();
    protected Group root=new Group();
    protected Scene scene=new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private Chatroom chatroom;
    private ArrayList<Node> toRemove=new ArrayList<>();

    private ChatroomScene()
    {

    }

    public void init(Chatroom chatroom) {
        System.out.println("INIT3");
        System.out.println(chatroom.getMessages().size());
        try
        {
            initBackground();
            initStartButtons();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        System.out.println("INIT2");
        setChatTools();
        setChatroom(chatroom);
    }


    protected void initStartButtons()
    {
        int ySpace=105;
        BlueButton button=new BlueButton("Play!",80,200,675,50);
        addNode(button.getNode());

        button=new BlueButton("Scoreboard",80,200,675,50+ySpace);
        button.getNode().setOnMouseClicked(event -> {
            ScoreBoardScene.SCORE_BOARD_SCENE.init();
            InputReader.setScene(ScoreBoardScene.SCORE_BOARD_SCENE.getScene());
        });
        addNode(button.getNode());

        button=new BlueButton("ChatRoom",80,200,675,50+2*ySpace);
        button.getNode().setOnMouseClicked(event -> {

            Client client=InputReader.getClient();
            Chatroom chatroom = client.getGlobalChatroom();
            ChatroomScene.CHATROOM_SCENE.init(chatroom);
            InputReader.setScene(ChatroomScene.CHATROOM_SCENE.getScene());
        });
        addNode(button.getNode());

        button=new BlueButton("Disconnect",80,200,675,50+3*ySpace);

        button.getNode().setOnMouseClicked(event -> {
            InputReader.getClient().disconnect();
            ConnectScene.init();
            InputReader.setScene(ConnectScene.getScene());
        });

        addNode(button.getNode());

    }

    protected void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/menu-back.jpg"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);

        Rectangle rectangle=new Rectangle(0,0,600,600);
        rectangle.relocate(50,50);
        rectangle.setOpacity(0.5);
        addNode(rectangle);
    }


    private void setChatTools()
    {
        TextField textField = new TextField();
        textField.relocate(50, 600);
        textField.setMinHeight(50);
        textField.setMaxHeight(50);
        textField.setMinWidth(500);
        textField.setMaxWidth(500);
        textField.setFont(Font.font(20));
        root.getChildren().add(textField);
        Label sendButton = new Label("Send");
        sendButton.setStyle("-fx-background-color: dodgerblue; -fx-font-size: 20");
        sendButton.relocate(550, 600);
        sendButton.setMinHeight(50);
        sendButton.setMinWidth(100);
        sendButton.setTextFill(Color.WHITE);
        sendButton.setAlignment(Pos.CENTER);
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                sendMessage(textField);
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    sendMessage(textField);
                }
            }
        });
        root.getChildren().add(sendButton);

    }

    private void sendMessage(TextField textField)
    {
        String messageText=textField.getText();
        if(textField.getText().equals(""))return ;
        textField.setText("");
        Client client=InputReader.getClient();
        chatroom.addMessage(new Message(client,messageText));
        client.sendChatroom(chatroom);
    }

    public void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public Scene getScene()
    {
        return scene;
    }

    public Chatroom getChatroom()
    {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom)
    {
        for(Node node:toRemove){
            Platform.runLater(() -> root.getChildren().remove(node));
        }
        toRemove.clear();
        int height=50;
        int y=540;
        this.chatroom = chatroom;
        ArrayList<Message> messagesShown=chatroom.getMessages();
        System.out.println(messagesShown.size());
        for(Message message:messagesShown)
        {
            Label newMessage = new Label("  " + message.getText() + "  ");
            newMessage.relocate(0, y);
            newMessage.setMaxHeight(height);
            newMessage.setMinHeight(height);
            newMessage.setMaxWidth(600);
            newMessage.setFont(Font.font(10));
            if (message.getClient().equals(InputReader.getClient()))
            {
                newMessage.setStyle(" -fx-background-color: LightSkyBlue ");
                newMessage.setLayoutX(50);
            } else
            {
                newMessage.setStyle("-fx-background-color: cornsilk");
                newMessage.translateXProperty().bind(newMessage.widthProperty().negate().add(650));
            }
            y-=60;
            System.out.println("KOFT");
            toRemove.add(newMessage);
            Platform.runLater(() -> root.getChildren().add(newMessage));
        }
    }

}
