package Model;

import View.Scene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileNotFoundException;

public interface Viewable {
    ImageView getImageView();

    default public void initView() throws FileNotFoundException {
        ImageView imageView = new ImageView();
        GameScene.addNode(getImageView());
    }

    public void refreshView();

    Animation getAnimation();

    void setAnimation(Animation animation);

    default public void changeImageView(Image image, int count, int rows, int columns, double x, double y) {
        ImageView imageView = getImageView();
        imageView.setImage(image);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        GameScene.setMiddlePosition(imageView, imageWidth / columns
                , imageHeight / rows, x, y);
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(700),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        setAnimation(animation);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }
    default void changeImageViewOnce(Image image, int count, int rows, int columns, double x, double y) {
        ImageView imageView = getImageView();
        imageView.setImage(image);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        GameScene.setMiddlePosition(imageView, imageWidth / columns
                , imageHeight / rows, x, y);
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(700),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        setAnimation(animation);
        animation.setCycleCount(1);
        animation.play();
    }

    default public void stopAnimation(int rows, int columns) {
        getAnimation().stop();
        ImageView imageView = getImageView();
        Image image = imageView.getImage();
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
    }

    default public void startAnimation() {
        getAnimation().play();
    }
}
