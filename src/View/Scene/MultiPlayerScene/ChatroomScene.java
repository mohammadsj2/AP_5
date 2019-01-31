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
import java.util.HashMap;

public class ChatroomScene extends MultiPlayerScene
{
    public final static ChatroomScene CHATROOM_SCENE=new ChatroomScene();
    private Chatroom chatroom;
    private ArrayList<Node> toRemove=new ArrayList<>();

    public void init(Chatroom chatroom) {
        root.getChildren().clear();
        try
        {
            initBackground();
            initStartButtons();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        setChatTools();
        setChatroom(chatroom,true);
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
        sendButton.setOnMouseClicked(event -> sendMessage(textField));
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
            {
                sendMessage(textField);
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

    public void setChatroom(Chatroom chatroom,boolean force)
    {
        removeAllNodesWithForce(force, toRemove, root.getChildren());

        int textHeight=30,nameHeight=30;
        int y=560;
        this.chatroom = chatroom;
        ArrayList<Message> messagesShown=chatroom.getMessages();
        for(Message message:messagesShown)
        {
            Label newMessage = new Label("  " + handleEmojis(message.getText()) + "  ");
            newMessage.relocate(0, y);
            newMessage.setMinHeight(textHeight);
            newMessage.setMaxHeight(textHeight);
            newMessage.setMaxWidth(600);
            newMessage.setFont(Font.font(15));
            if (message.getClient().equals(InputReader.getClient()))
            {
                newMessage.setStyle("-fx-background-color: cornsilk");
                newMessage.translateXProperty().bind(newMessage.widthProperty().negate().add(640));
                addWithForce(force, newMessage);
                y-=textHeight+10;
            } else
            {
                newMessage.setText(newMessage.getText());
                newMessage.setStyle(" -fx-background-color: LightSkyBlue ");
                newMessage.setLayoutX(60+textHeight+nameHeight);
                addWithForce(force, newMessage);
                y-=nameHeight;
                try
                {
                    ImageView profilePicture=new ImageView(new Image(
                            new FileInputStream("./Textures/Profile/"+message.getClient().getImageIndex()+".png")));
                    profilePicture.setFitHeight(textHeight+nameHeight);
                    profilePicture.setFitWidth(textHeight+nameHeight);
                    profilePicture.relocate(60,y);
                    addWithForce(force,profilePicture);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                Label nameLabel=new Label("  "+message.getClient().getName()+"  ");
                nameLabel.relocate(60+textHeight+nameHeight,y);
                nameLabel.setMaxHeight(nameHeight);
                nameLabel.setMinHeight(nameHeight);
                nameLabel.setMaxWidth(200);
                nameLabel.setFont(Font.font(15));
                nameLabel.setStyle(" -fx-background-color: LightSkyBlue;-fx-text-fill: Yellow");

                addWithForce(force, nameLabel);
                y-=textHeight+10;
            }
        }
        System.out.println("CHAT ROOM SET!");
    }

    private String handleEmojis(String text)
    {
        HashMap<String,String> emojiMap=new HashMap<>();
        emojiMap.put(":)", "\uD83D\uDE42");
        emojiMap.put(":(", "\uD83D\uDE41");
        emojiMap.put(":|", "\uD83D\uDE10");
        emojiMap.put(":P", "\uD83D\uDE0B");
        emojiMap.put(":D", "\uD83D\uDE03");
        emojiMap.put(":_(", "\uD83D\uDE2D");
        emojiMap.put(":O", "\uD83D\uDE32");
        StringBuilder changedMessage = new StringBuilder("");
        String[] words = text.split(" ");
        for (String word : words)
        {
            changedMessage.append(emojiMap.getOrDefault(word, word) + " ");
        }
        return String.valueOf(changedMessage);
    }

    public void addWithForce(boolean force, Node node) {
        if(force){
            root.getChildren().add(node);
            toRemove.add(node);
        }else {
            Platform.runLater(() -> {
                root.getChildren().add(node);
                toRemove.add(node);
            });
        }
    }

}
