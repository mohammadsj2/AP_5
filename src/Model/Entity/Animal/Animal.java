package Model.Entity.Animal;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;
import Exception.CellDoesNotExistException;
import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Sheep;
import Model.Entity.Animal.Wild.Bear;
import Model.Entity.Animal.Wild.Lion;
import Model.Entity.Entity;
import Model.Loadable;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Upgradable;
import View.Scene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    private int level;
    private int speed = Constant.ANIMAL_SPEED;
    private int step = 0;
    private Cell currentCell;
    String direction = null;
    Boolean isFlipDirection=false;


    protected Animal(Cell cell) {
        super(cell);
    }

    protected Animal(Cell cell, int level) {
        super(cell);
        setLevel(level);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Cell walkTowards(Cell cur) {
        return InputReader.getCurrentController().getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


    public void walk() throws CellDoesNotExistException {
        Map map = InputReader.getCurrentController().getMap();
        if (currentCell == null || step == 0 || getCell() == currentCell) {
            currentCell = map.getRandomCell(getCell(), getSpeed());
        }
        this.changeCell(map.getBestCellBySpeed(getCell(), currentCell, Constant.ANIMAL_SPEED));
    }

    @Override
    public void refreshView() {

    }

    public void changeCell(Cell cur) {
        Map map = getMap();

        map.destroyWalkAnimal(getCell(), this);
        setWalkAnimation(getCell(), cur);
        setCell(cur);
        map.addWalkAnimal(cur, this);
    }

    protected void setWalkAnimation(Cell startCell, Cell targetCell) {
        int stableDistance = 1;
        String directionName, animalName;
        int count = 23, rows = 0, columns = 0;
        boolean flipImage = false;

        if (Math.abs(targetCell.getPositionY() - startCell.getPositionY()) < stableDistance) {
            directionName = "left";
            if (targetCell.getPositionX() > startCell.getPositionX()) {
                flipImage = true;
            }
        } else if (targetCell.getPositionY() > startCell.getPositionY()) {
            if (Math.abs(targetCell.getPositionX() - startCell.getPositionX()) < stableDistance)
                directionName = "down";
            else if (targetCell.getPositionX() > startCell.getPositionX()) {
                directionName = "down_left";
                flipImage = true;
            } else
                directionName = "down_left";
        } else {
            if (Math.abs(targetCell.getPositionX() - startCell.getPositionX()) < stableDistance)
                directionName = "up";
            else if (targetCell.getPositionX() > startCell.getPositionX()) {
                directionName = "up_left";
                flipImage = true;
            } else
                directionName = "up_left";
        }


        if (this instanceof Cat) {
            if (directionName.equals("left")) {
                rows = 6;
                columns = 4;
            } else {
                rows = 4;
                columns = 6;
            }
            animalName = "Cat";
        } else if (this instanceof Dog) {
            if (directionName.equals("up_left") || directionName.equals("down_left")) {
                rows = 5;
                columns = 5;
            } else {
                rows = 4;
                columns = 6;
            }
            animalName = "Dog";
        } else if (this instanceof Bear) {
            rows = 6;
            columns = 4;
            animalName = "Grizzly";
        } else if (this instanceof Lion) {
            if (directionName.equals("up_left") || directionName.equals("up")) {
                rows = 4;
                columns = 6;
            } else if (directionName.equals("left")) {
                rows = 8;
                columns = 3;
            } else if (directionName.equals("down_left")) {
                rows = 6;
                columns = 4;
            } else {
                rows = 5;
                columns = 5;
            }
            animalName = "Lion";
        } else if (this instanceof Chicken) {
            animalName = "GuineaFowl";
            rows = 5;
            columns = 5;
        } else if (this instanceof Sheep) {
            if (directionName.equals("down") || directionName.equals("up")
                    || directionName.equals("up_left")) {
                rows = 5;
                columns = 5;
            } else {
                rows = 6;
                columns = 4;
            }
            animalName = "Sheep";
        } else {
            if (directionName.equals("up")) {
                rows = 6;
                columns = 4;
            } else {
                rows = 8;
                columns = 3;
            }
            animalName = "Cow";
        }
        Image image = null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/" + animalName + "/" + directionName + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(getImageView().getX());
        if (!(directionName.equals(direction) && flipImage==isFlipDirection )) {
            changeImageView(image, count, rows, columns,
                    GameScene.modifiedX(getCell().getPositionX()), GameScene.modifiedY(getCell().getPositionY()),
                    GameScene.modifiedX(targetCell.getPositionX()) - GameScene.modifiedX(startCell.getPositionX()),
                    GameScene.modifiedY(targetCell.getPositionY()) - GameScene.modifiedY(startCell.getPositionY()));
            getImageView().setScaleX((flipImage)?-1:1);
            direction = directionName;
            isFlipDirection=flipImage;
        }
        System.out.println(getImageView().getX());
        walkAnimation(startCell,targetCell);
    }

    private void walkAnimation(Cell startCell,Cell targetCell) {
        ImageView imageView=getImageView();
        KeyValue xKeyValue,yKeyValue;
        imageView.setX(GameScene.modifiedX(startCell.getPositionX()));
        imageView.setY(GameScene.modifiedY(startCell.getPositionY()));
        int targetX=(int)GameScene.modifiedX(targetCell.getPositionX());
        int targetY=(int)GameScene.modifiedY(targetCell.getPositionY());
        System.out.println(imageView.getX()+" "+targetX);
        xKeyValue = new KeyValue(imageView.xProperty(),targetX);
        yKeyValue = new KeyValue(imageView.yProperty(),targetY);

        KeyFrame keyFrame=new KeyFrame(Duration.seconds((double)Constant.NEXT_TURN_DURATION/1e9),xKeyValue,yKeyValue);
        Timeline timeline=new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void changeImageView(Image image, int count, int rows, int columns, double x, double y, double moveX, double moveY) {
        ImageView imageView = getImageView();

        imageView.setImage(image);
        GameScene.setImageViewPositionOnMap(imageView, x, y);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();

        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        if (getAnimation() != null)
            getAnimation().stop();
        Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(750),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        setAnimation(animation);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }


    public void nextTurn() throws CellDoesNotExistException {
        this.walk();
        step++;
        step %= 15;
    }

    @Override
    public int upgradeCost() {
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgradeException {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
