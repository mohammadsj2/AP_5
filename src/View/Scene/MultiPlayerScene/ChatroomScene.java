package View.Scene.MultiPlayerScene;

import Network.Chatroom;
import Network.Client.Client;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Formatter;

public class ChatroomScene extends MultiPlayerScene
{
    public final static ChatroomScene CHATROOM_SCENE=new ChatroomScene();
    Chatroom chatroom;

    private ChatroomScene()
    {

    }

    public void init(Chatroom chatroom) {
        super.init();
        this.chatroom=chatroom;
        setChatTools();
    }

    private void setChatTools()
    {
        TextField textField = new TextField();
        textField.relocate(10, 500);
        textField.setMinHeight(50);
        textField.setMaxHeight(50);
        textField.setMinWidth(680);
        textField.setMaxWidth(680);
        textField.setFont(Font.font(20));
        root.getChildren().add(textField);
        Label sendButton = new Label("Send");
        sendButton.setStyle("-fx-background-color: dodgerblue; -fx-font-size: 20");
        sendButton.relocate(695, 640);
        sendButton.setMinHeight(50);
        sendButton.setMinWidth(95);
        sendButton.setTextFill(Color.WHITE);
        sendButton.setAlignment(Pos.CENTER);
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                sendMessage(textField.getText());
            }
        });
        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    sendMessage(textField.getText());
                }
            }
        });
        root.getChildren().add(sendButton);

    }

    private void sendMessage(String messageText)
    {

    }

    public void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public Scene getScene()
    {
        return scene;
    }
}
