package Model.Entity.Animal;

import Controller.*;
import Model.Map.Cell;
import Model.Entity.Animal.Wild.Wild;
import Exception.CellDoesNotExistException;
import Model.Map.Map;
import View.GameScene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Dog extends Animal {

    public static final int DOG_SPEED = 4;

    public Dog(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Africa/Dog/down.png"));
            imageView.setImage(image);
            GameScene.setImageViewPositionOnMap(imageView,cell.getPositionX(),cell.getPositionY());
            int imageWidth= (int) image.getWidth();
            int imageHeight= (int) image.getHeight();

            imageView.setViewport(new Rectangle2D(0, 0, imageWidth/6, imageHeight/4));
            final Animation animation = new SpriteAnimation(
                    imageView,
                    Duration.millis(700),
                    24, 6,
                    0, 0,
                    imageWidth/6, imageHeight/4
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Dog(Cell cell, int level) {
        super(cell, level);
    }

    @Override
    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        Cell cur = map.nearestCellWithWild(this.getCell());
        if (cur.equals(this.getCell())) {
            kill();
        } else {
            changeCell(map.getBestCellBySpeed(getCell(),cur, DOG_SPEED));
        }
    }
    public void kill() throws CellDoesNotExistException {
        ArrayList<Wild> wilds = this.getCell().getWilds();
        for (Wild wild : wilds) {
            wild.destroy();
        }
        this.destroy();
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
