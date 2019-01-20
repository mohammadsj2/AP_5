package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;
import View.GameScene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Bear extends Wild{

    public static final String CAGED_BROWN_BEAR = "cagedbrownbear";

    public Bear(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Grizzly/down.png"));
            imageView.setImage(image);
            GameScene.setImageViewPositionOnMap(imageView,cell.getPositionX(),cell.getPositionY());
            int imageWidth= (int) image.getWidth();
            int imageHeight= (int) image.getHeight();

            imageView.setViewport(new Rectangle2D(0, 0, imageWidth/4, imageHeight/6));
            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(700),
                    24, 4,
                    0, 0,
                    imageWidth/4, imageHeight/6
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_BROWN_BEAR);
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
