package Model;

import View.GameScene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public interface Viewable {
    ImageView getImageView();
    public void initView();
    public void refreshView();
    default public void changeImageView(Image image,int count,int rows,int columns,int x,int y){
        ImageView imageView=getImageView();

        imageView.setImage(image);
        imageView.setX(x);
        imageView.setY(y);
        int imageWidth= (int) image.getWidth();
        int imageHeight= (int) image.getHeight();

        imageView.setViewport(new Rectangle2D(0, 0, imageWidth/columns, imageHeight/rows));
        final Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(700),
                count, columns,
                0, 0,
                imageWidth/columns, imageHeight/rows
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }
}
