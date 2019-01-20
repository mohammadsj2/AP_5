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

public class Lion extends Wild{

    public static final String CAGED_LION = "cagedlion";

    public Lion(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Africa/Lion/left.png"));
            imageView.setImage(image);
            GameScene.setImageViewPositionOnMap(imageView,cell.getPositionX(),cell.getPositionY());
            int imageWidth= (int) image.getWidth();
            int imageHeight= (int) image.getHeight();

            imageView.setViewport(new Rectangle2D(0, 0, imageWidth/3, imageHeight/8));
            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(700),
                    24, 3,
                    0, 0,
                    imageWidth/3, imageHeight/8
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_LION);
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
