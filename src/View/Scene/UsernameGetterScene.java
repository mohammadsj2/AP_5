package View.Scene;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Button.BlueButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class UsernameGetterScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);
    private static Label errorLabel=new Label();
    private static int imageIndex;
    private static Pane[] avatars=new Pane[Constant.AVATAR_NUMBER+1];
    private static boolean inAvatarSelectMode=false;

    public static void init()
    {
        imageIndex=((new Random(LocalDateTime.now().getNano()).nextInt())%Constant.AVATAR_NUMBER);
        if(imageIndex<0)
            imageIndex+=Constant.AVATAR_NUMBER;
        imageIndex++;
        try
        {
            initBackground();
            initLoginTools();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void initLoginTools() throws FileNotFoundException {
        initAvatars();
        initTextFieldAndLoginButton();
    }

    private static void initAvatars() throws FileNotFoundException {
        int x=373,y=280;
        double scale=0.4;

        for(int i=1;i<=Constant.AVATAR_NUMBER;i++){
            Image image=new Image(new FileInputStream("Textures/Profile/"+i+".png"));
            ImageView imageView=new ImageView(image);
            imageView.relocate(5,0);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            Circle circle=new Circle(100, Color.WHITE);
            circle.relocate(-20,-20);
            avatars[i]=new Pane(circle,imageView);
            avatars[i].relocate(avatarPositionX(i),avatarPositionY(i));
            avatars[i].setOpacity(0);
            avatars[i].setScaleX(scale);
            avatars[i].setScaleY(scale);
            int finalI = i;
            Duration duration=Duration.seconds(0.3);
            avatars[i].setOnMouseClicked(event -> {
                ArrayList<Timeline> timelines=new ArrayList<>();
                if(inAvatarSelectMode){
                    imageIndex=finalI;
                    for(int j = 1; j <=Constant.AVATAR_NUMBER; j++){
                        if(j== finalI)continue;
                        KeyValue opacityKeyValue=new KeyValue(avatars[j].opacityProperty(),0);
                        KeyFrame keyFrame=new KeyFrame(duration,opacityKeyValue);
                        Timeline timeline=new Timeline(keyFrame);
                        timelines.add(timeline);
                    }
                    KeyValue xKeyValue=new KeyValue(avatars[finalI].layoutXProperty(),x);
                    KeyValue yKeyValue=new KeyValue(avatars[finalI].layoutYProperty(),y);
                    KeyValue scaleXKeyValue=new KeyValue(avatars[finalI].scaleXProperty(),1);
                    KeyValue scaleYKeyValue=new KeyValue(avatars[finalI].scaleYProperty(),1);
                    KeyFrame keyFrame=new KeyFrame(duration,xKeyValue,yKeyValue,scaleXKeyValue,scaleYKeyValue);
                    Timeline timeline=new Timeline(keyFrame);
                    timelines.add(timeline);
                }else{
                    for(int j = 1; j <=Constant.AVATAR_NUMBER; j++){
                        if(j== finalI)continue;
                        KeyValue opacityKeyValue=new KeyValue(avatars[j].opacityProperty(),1);
                        KeyFrame keyFrame=new KeyFrame(duration,opacityKeyValue);
                        Timeline timeline=new Timeline(keyFrame);
                        timelines.add(timeline);
                    }
                    KeyValue xKeyValue=new KeyValue(avatars[finalI].layoutXProperty(),avatarPositionX(finalI));
                    KeyValue yKeyValue=new KeyValue(avatars[finalI].layoutYProperty(),avatarPositionY(finalI));
                    KeyValue scaleXKeyValue=new KeyValue(avatars[finalI].scaleXProperty(),scale);
                    KeyValue scaleYKeyValue=new KeyValue(avatars[finalI].scaleYProperty(),scale);
                    KeyFrame keyFrame=new KeyFrame(duration,xKeyValue,yKeyValue,scaleXKeyValue,scaleYKeyValue);
                    Timeline timeline=new Timeline(keyFrame);
                    timelines.add(timeline);
                }
                inAvatarSelectMode=!inAvatarSelectMode;
                for(Timeline timeline:timelines){
                    timeline.play();
                }
            });
            addNode(avatars[i]);
        }
        avatars[imageIndex].relocate(x,y);
        avatars[imageIndex].setOpacity(1);
        avatars[imageIndex].setScaleX(1);
        avatars[imageIndex].setScaleY(1);

    }


    private static double avatarPositionX(int i) {
        double x[]={0,265,280,365,467.5,570,655,670,0,0,0,0,0,0};
        return x[i]-107;
    }

    private static double avatarPositionY(int i) {
        double y[]={0,350,250,180,140,180,250,350,0,0,0,0,0,0};
        return y[i]-30;
    }



    private static void initTextFieldAndLoginButton() {
        int height=70,width=180,textWidth=300;
        BlueButton loginButton=new BlueButton("Login!",height,width
                ,((double) Constant.GAME_SCENE_WIDTH-width)/2,600,false);

        TextField textField=new TextField("Guest"+ new Random(LocalDateTime.now().getNano()).nextInt(99999));
        textField.setMinWidth(textWidth);
        textField.relocate(((double)Constant.GAME_SCENE_WIDTH-textWidth)/2,500);
        textField.setStyle("-fx-font-size: 25;");

        loginButton.getNode().setOnMouseClicked(event -> login(textField, imageIndex));
        textField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                login(textField, imageIndex);
            }
        });
        addNode(loginButton.getNode());
        addNode(textField);
    }

    private static void login(TextField textField,int avatarId) {
        if(inAvatarSelectMode){
            root.getChildren().remove(errorLabel);
            errorLabel=new Label("Please select your avatar!");
            errorLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 30;-fx-font-family: 'Comic Sans MS';" +
                    "-fx-background-color: rgba(0, 0, 0, 0.5);-fx-padding: 2px;" +
                    "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;");
            errorLabel.relocate(270,50);
            root.getChildren().add(errorLabel);
            KeyValue opacityKeyValue=new KeyValue(errorLabel.opacityProperty(),0);
            KeyFrame keyFrame=new KeyFrame(Duration.seconds(3),opacityKeyValue);
            Timeline timeline=new Timeline(keyFrame);
            timeline.play();
            return ;
        }
        if(textField.getText().length()> Constant.MAX_NUMBER_OF_USERNAME_CHARS)
        {
            root.getChildren().remove(errorLabel);
            errorLabel=new Label("Name must have at most 10 characters!");
            errorLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 30;-fx-font-family: 'Comic Sans MS';" +
                    "-fx-background-color: rgba(0, 0, 0, 0.5);-fx-padding: 2px;" +
                    "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;");
            errorLabel.relocate(180,50);
            root.getChildren().add(errorLabel);
            KeyValue opacityKeyValue=new KeyValue(errorLabel.opacityProperty(),0);
            KeyFrame keyFrame=new KeyFrame(Duration.seconds(3),opacityKeyValue);
            Timeline timeline=new Timeline(keyFrame);
            timeline.play();
            return ;
        }
        Client client=new Client(textField.getText(),imageIndex);
        InputReader.setClient(client);
        MenuScene.init(false);
        InputReader.setScene(MenuScene.getScene());
    }


    private static void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/menu-back.jpg"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);
    }


    public static void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static void deleteNode(Node node) {
        root.getChildren().remove(node);
    }

    public static Scene getScene()
    {
        return scene;
    }
}
