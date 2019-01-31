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
    public final static ChatroomScene CHATROOM_SCENE = new ChatroomScene();
    private Chatroom chatroom;
    private ArrayList<Node> toRemove = new ArrayList<>();
    private ImageView cancelReply;
    private Label replyLabel;
    private Message replyMessage;

    public void init(Chatroom chatroom)
    {
        cancelReply=null;
        replyLabel = null;
        replyMessage = null;
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
        setChatroom(chatroom, true);
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
        textField.setOnKeyPressed(event ->
        {
            if (event.getCode().equals(KeyCode.ENTER))
            {
                sendMessage(textField);
            }
        });
        root.getChildren().add(sendButton);

    }

    private void sendMessage(TextField textField)
    {
        String messageText = textField.getText();
        if (textField.getText().equals("")) return;
        textField.setText("");
        Client client = InputReader.getClient();
        chatroom.addMessage(new Message(client, messageText, replyMessage));
        client.sendChatroom(chatroom);
        replyLabel=null;
        replyMessage=null;
    }

    public void addNode(Node node)
    {
        if (!root.getChildren().contains(node))
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

    public void setChatroom(Chatroom chatroom, boolean force)
    {
        removeAllNodesWithForce(force, toRemove, root.getChildren());

        int textHeight = 30, nameHeight = 30;
        int y = 520;
        this.chatroom = chatroom;
        ArrayList<Message> messagesShown = chatroom.getMessages();
        for (Message message : messagesShown)
        {
            String text = handleEmojis(message.getText());
            Label newMessage = new Label("  " + text + " ");
            newMessage.relocate(0, y);
            newMessage.setMinHeight(textHeight);
            newMessage.setMaxHeight(textHeight);
            newMessage.setMaxWidth(600);
            newMessage.setFont(Font.font(15));
            newMessage.setOnMouseClicked(event ->
            {
                try
                {
                    cancelReply=new ImageView(new Image(
                            new FileInputStream("./Textures/UI/Icons/CancelReply.png")));
                    cancelReply.relocate(50,570);
                    cancelReply.setFitHeight(20);
                    cancelReply.setFitWidth(20);
                    cancelReply.setOnMouseClicked(event1 ->
                    {
                        Client client=InputReader.getClient();
                        Chatroom chatroom1 = client.getGlobalChatroom();
                        ChatroomScene.CHATROOM_SCENE.init(chatroom1);
                        InputReader.setScene(ChatroomScene.CHATROOM_SCENE.getScene());
                    });
                    addWithForce(force,cancelReply);

                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                replyLabel = new Label(" " + message.getClient().getName() + " : " + text + " ");
                replyLabel.relocate(70, 570);
                replyLabel.setMinHeight(textHeight);
                replyLabel.setMaxHeight(textHeight);
                replyLabel.setFont(Font.font(15));
                replyLabel.setStyle("-fx-background-color: AliceBlue");
                replyMessage = message;
                addWithForce(force, replyLabel);

            });
            if (message.getClient().equals(InputReader.getClient()))
            {
                newMessage.setStyle("-fx-background-color: cornsilk");
                newMessage.translateXProperty().bind(newMessage.widthProperty().negate().add(640));
                addWithForce(force, newMessage);
                y -= textHeight;
                if (message.getRepliedClient() != null)
                {
                    Label repliedLabel = new Label(" " + message.getRepliedClient().getName() + " : " +
                            handleEmojis(message.getRepliedText()) + " ");
                    repliedLabel.relocate(0, y);
                    repliedLabel.translateXProperty().bind(repliedLabel.widthProperty().negate().add(640));
                    repliedLabel.setMaxHeight(textHeight);
                    repliedLabel.setMinHeight(textHeight);
                    repliedLabel.setFont(Font.font(15));
                    repliedLabel.setStyle("-fx-background-color: AliceBlue");
                    addWithForce(force, repliedLabel);
                    y-=textHeight;
                }
                y -= 10;
            } else
            {
                newMessage.setText(newMessage.getText());
                newMessage.setStyle(" -fx-background-color: LightSkyBlue ");
                newMessage.setLayoutX(60 + textHeight + nameHeight);
                addWithForce(force, newMessage);
                y -= nameHeight;
                try
                {
                    ImageView profilePicture = new ImageView(new Image(
                            new FileInputStream("./Textures/Profile/" + message.getClient().getImageIndex() + ".png")));
                    profilePicture.setFitHeight(textHeight + nameHeight);
                    profilePicture.setFitWidth(textHeight + nameHeight);
                    profilePicture.relocate(60, y);
                    addWithForce(force, profilePicture);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                Label nameLabel = new Label("  " + message.getClient().getName() + "  ");
                nameLabel.relocate(60 + textHeight + nameHeight, y);
                nameLabel.setMaxHeight(nameHeight);
                nameLabel.setMinHeight(nameHeight);
                nameLabel.setMaxWidth(200);
                nameLabel.setFont(Font.font(15));
                nameLabel.setStyle(" -fx-background-color: LightSkyBlue;-fx-text-fill: Yellow");
                addWithForce(force, nameLabel);
                if (message.getRepliedClient() != null)
                {
                    Label repliedLabel = new Label(" " + message.getRepliedClient().getName() + " : " +
                            handleEmojis(message.getRepliedText()) + " ");
                    repliedLabel.relocate(0, y);
                    repliedLabel.translateXProperty().bind(nameLabel.widthProperty().add(60 + textHeight + nameHeight));
                    repliedLabel.setMaxHeight(textHeight);
                    repliedLabel.setMinHeight(textHeight);
                    repliedLabel.setFont(Font.font(15));
                    repliedLabel.setStyle("-fx-background-color: AliceBlue");
                    addWithForce(force, repliedLabel);
                }
                y -= textHeight + 10;
            }
        }
        System.out.println("CHAT ROOM SET!");
    }

    private String handleEmojis(String text)
    {
        HashMap<String, String> emojiMap = new HashMap<>();
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

    public void addWithForce(boolean force, Node node)
    {
        if (force)
        {
            if (!root.getChildren().contains(node))
            {
                root.getChildren().add(node);
                toRemove.add(node);
            }
        } else
        {
            Platform.runLater(() ->
            {
                root.getChildren().add(node);
                toRemove.add(node);
            });
        }
    }

}
