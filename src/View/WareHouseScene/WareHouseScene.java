package View.WareHouseScene;

import Constant.Constant;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class WareHouseScene {
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    public static void init(){
        try {
            initBackground();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initBackground() throws FileNotFoundException {
        Image image=new Image(new FileInputStream("./Textures/TruckBackGround.png"));
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        imageView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        root.getChildren().add(imageView);
    }
    public static void addNode(Node node) {
        if(!root.getChildren().contains(node))
            root.getChildren().add(node);
    }

    public static void deleteNode(Node node) {
        root.getChildren().remove(node);
    }

    public static void setMiddlePosition(ImageView imageView,double width,double height,double x,double y){
        imageView.setX((double)x-width/2.0);
        imageView.setY((double)y-height/2.0);
    }

    public static Scene getScene() {
        return scene;
    }
}
