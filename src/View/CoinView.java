package View;

import View.Scene.GameScene;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CoinView {
    ImageView coinView;
    public CoinView(int x,int y,int count){
        initView(x, y, count);
    }
    public CoinView(int x,int y){
        initView(x,y,16);
    }

    public void initView(int x, int y, int count) {
        try {
            Image image=new Image(new FileInputStream("Textures/UI/Icons/Money/coin_32_anim.png"));
            coinView = new ImageView(image);
            if(count==1){
                image=new Image(new FileInputStream("Textures/UI/Icons/Money/coin_32.png"));
                GameScene.setMiddlePosition(coinView, image.getWidth(), image.getHeight(), x, y);
                coinView.setImage(image);
                return;
            }
            int imageWidth = (int) image.getWidth();
            int imageHeight = (int) image.getHeight();
            int columns=4,rows=4;
            GameScene.setMiddlePosition(coinView, imageWidth / columns, imageHeight / rows, x, y);
            coinView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
            Animation animation = new SpriteAnimation(
                    coinView,
                    Duration.millis(1200),
                    count, columns,
                    0, 0,
                    imageWidth / columns, imageHeight / rows
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Node getNode(){
        return coinView;
    }
}
