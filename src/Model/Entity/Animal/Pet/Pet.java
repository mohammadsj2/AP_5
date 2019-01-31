package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.InputReader;
import Exception.CellDoesNotExistException;
import Exception.StartBusyProducerException;
import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Producer;
import View.Scene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    private int health;
    private int lastProductTurn=0;
    private int timeDead = 0;
    protected Pet(Cell cell) {
        super(cell);
        this.health=Constant.INIT_HEALTH;
        this.lastProductTurn=InputReader.getCurrentController().getTurn();
    }
    public void changeImageView(Image image, int count, int rows, int columns, double x, double y, boolean definite) {
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
        if (!definite) {
            animation.setCycleCount(Animation.INDEFINITE);
        } else {
            animation.setCycleCount(1);
        }
        animation.play();
    }
    public int getTimeDead() {
        return timeDead;
    }

    public void setTimeDead(int timeDead) {
        this.timeDead = timeDead;
    }

    @Override
    public boolean haveProduct() {
        return InputReader.getCurrentController().getTurn()>=lastProductTurn+ Constant.ANIMAL_PRODUCT_TURN;
    }

    @Override
    public void startProduction() throws StartBusyProducerException {

    }

    @Override
    public ArrayList<Item> getInputItems() {
        return null;
    }

    @Override
    public ArrayList<Item> getOutputItems() {
        return null;
    }

    @Override
    public void endProduction() {
        lastProductTurn= InputReader.getCurrentController().getTurn();
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isHungry() {
        return this.getHealth() <= Constant.PET_HUNGRY_HEALTH;
    }
    @Override
    public void nextTurn() throws CellDoesNotExistException {
        super.nextTurn();
        this.updateHealth(Constant.CHANGE_PET_HEALTH_PER_TURN);
        if(haveProduct()){
            if(getOutputItems()!=null) {
                for (Item item : getOutputItems()) {
                    getMap().addEntity(getCell(),item);
                }
                endProduction();
            }
        }
    }
    @Override
    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        Cell cur=map.nearestCellWithGrass(getCell());
        if (isHungry() &&  cur != null) {
            if (!getCell().haveGrass()) {
                changeCell(map.getBestCellBySpeed(getCell(),cur,getSpeed()));
            } else {
                setWalkAnimation(getCell(), getCell());
                eatGrass();
                updateHealth(Constant.INCREASE_PET_HEALTH_AFTER_EAT_GRASS);
            }
        } else {
            super.walk();
        }
    }
    public void eatGrass() {
        Map map=InputReader.getCurrentController().getMap();
        map.destroyGrass(getCell());
    }
    public void updateHealth(int x) throws CellDoesNotExistException {
        this.setHealth(this.getHealth() + x);
        if (getHealth() <= 0) {
            destroy();
        }
        if (getHealth() > Constant.PET_MAX_HEALTH) {
            setHealth(Constant.PET_MAX_HEALTH);
        }
    }

    @Override
    public int getSpeed() {
        if(!isHungry()) {
            return super.getSpeed();
        }else{
            return Constant.HUNGRY_PET_SPEED;
        }
    }
}
