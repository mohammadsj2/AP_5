package View.GameScene;

import Constant.Constant;
import Controller.InputReader;
import Model.WorkShop;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GameScene
{
    private static Group root = new Group();
    private static Scene scene = new Scene(root, Constant.GAME_SCENE_WIDTH, Constant.GAME_SCENE_HEIGHT);

    public static void init()
    {
        try
        {
            initBackground();
            initWorkShops();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //  Rectangle rectangle=new Rectangle(100,100,100,100);
        //  root.getChildren().add(rectangle);
    }

    private static void initWorkShops() throws FileNotFoundException
    {
        int[] workshopsPositionX = {70, 0, 0, 0, 0, 0};
        int[] workshopsPositionY = {450, 0, 0, 0, 0, 0};
        WorkShop[] workShops = InputReader.getCurrentController().getWorkShops().toArray(new WorkShop[0]);
        for (int i = 0; i < workShops.length; i++)
        {
            //   System.out.println("file:Texture/Workshops/"+workShops[i].getName()+
            //   "/0"+(workShops[i].getLevel()+1)+".png");
            String imageAddress="Textures/Workshops/" + workShops[i].getName() +
                    "/0" + (workShops[i].getLevel() + 1) + ".png";
            Image workshopImage = new Image(new FileInputStream(imageAddress));
            ImageView workshopView = new ImageView();
            workshopView.setImage(workshopImage);
            workshopView.setX(workshopsPositionX[i]);
            workshopView.setY(workshopsPositionY[i]);
            int imageWidth= (int) workshopImage.getWidth();
            int imageHeight= (int) workshopImage.getHeight();

            root.getChildren().add(workshopView);

            workshopView.setViewport(new Rectangle2D(0, 0, imageWidth/4, imageHeight/4));
//sprite animation  useful for your project
            final Animation animation = new SpriteAnimation(
                    workshopView,
                    Duration.millis(700),
                    16, 4,
                    0, 0,
                    // 64=829/13
                    imageWidth/4, imageHeight/4
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        }

    }

    private static void initBackground() throws FileNotFoundException
    {
        Image backgroundImage = new Image(new FileInputStream("Textures/back.png"));
        ImageView backgroundView = new ImageView();
        backgroundView.setFitWidth(Constant.GAME_SCENE_WIDTH);
        backgroundView.setFitHeight(Constant.GAME_SCENE_HEIGHT);
        backgroundView.setImage(backgroundImage);
        root.getChildren().add(backgroundView);
    }

    public static Scene getScene()
    {
        return scene;
    }
}
