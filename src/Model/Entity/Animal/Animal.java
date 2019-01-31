package Model.Entity.Animal;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;
import Exception.CellDoesNotExistException;
import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Pet;
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
    private int step = 0;
    protected Cell currentCell;
    String direction = null;
    Boolean isFlipDirection=false;


    protected Animal(Cell cell) {
        super(cell);
    }

    public int getSpeed() {
        return Constant.ANIMAL_SPEED;
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
        this.changeCell(map.getBestCellBySpeed(getCell(), currentCell, getSpeed()));
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

        if (this.getAlive() == false) {
            try {
                this.destroy();
            } catch (CellDoesNotExistException e) {
                e.printStackTrace();
            }
        }
        int stableDistance = getSpeed()/5+1;

        String directionName, animalName;
        int count = 23, rows = 0, columns = 0;
        boolean flipImage = false;
        boolean sameCell = (startCell == targetCell);
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

        if (sameCell && this instanceof Pet) {
            directionName = "eat";
        }
        if (this instanceof Cat) {
            rows=columns=5;
            count=24;
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
                if (directionName.equals("eat")) {
                    count = 24;
                }
            }
            animalName = "Sheep";
        } else {
            if (directionName.equals("up") || directionName.equals("eat")) {
                rows = 6;
                columns = 4;
                if (directionName.equals("eat")) {
                    count = 24;
                }
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
        if (!(directionName.equals(direction) && flipImage==isFlipDirection )) {
            changeImageViewForWalk(image, count, rows, columns,
                    GameScene.modifiedX(getCell().getPositionX()), GameScene.modifiedY(getCell().getPositionY())
            );
            getImageView().setScaleX((flipImage)?-1:1);
            direction = directionName;
            isFlipDirection=flipImage;
        }
        walkAnimation(startCell,targetCell);
    }

    private void walkAnimation(Cell startCell,Cell targetCell) {
        ImageView imageView=getImageView();
        KeyValue xKeyValue,yKeyValue;
        imageView.setX(GameScene.modifiedX(startCell.getPositionX()));
        imageView.setY(GameScene.modifiedY(startCell.getPositionY()));
        int targetX=(int)GameScene.modifiedX(targetCell.getPositionX());
        int targetY=(int)GameScene.modifiedY(targetCell.getPositionY());
        xKeyValue = new KeyValue(imageView.xProperty(),targetX);
        yKeyValue = new KeyValue(imageView.yProperty(),targetY);

        KeyFrame keyFrame=new KeyFrame(Duration.seconds((double)Constant.NEXT_TURN_DURATION/1e9),xKeyValue,yKeyValue);
        Timeline timeline=new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
    }


    public void changeImageViewForWalk(Image image, int count, int rows, int columns, double x, double y) {
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
                Duration.millis(7500.0/(double)getSpeed()),
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
