package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Exception.CellDoesNotExistException;
import Exception.StartBusyProducerException;
import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Producer;
import View.ProgressBar.HealthProgressBar;
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

import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    private int health;
    private int lastProductTurn=0;
    private int timeDead = 0;
    private HealthProgressBar healthProgressBar;

    protected Pet(Cell cell) {
        super(cell);
        setHealth(Constant.INIT_HEALTH);
        this.lastProductTurn=InputReader.getCurrentController().getTurn();
    }

    @Override
    public void initView() {
        super.initView();
        healthProgressBar=new HealthProgressBar(GameScene.modifiedX(getCell().getPositionX()),GameScene.modifiedY(getCell().getPositionY()));
        GameScene.addNode(healthProgressBar.getNode());
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
        healthProgressBar.setPercentage((double)health/(double) Constant.PET_MAX_HEALTH);
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

    @Override
    protected void walkAnimation(Cell startCell, Cell targetCell) {
        super.walkAnimation(startCell, targetCell);
        healthProgressBar.getNode().setLayoutX(GameScene.modifiedX(startCell.getPositionX())+13);
        healthProgressBar.getNode().setLayoutY(GameScene.modifiedY(startCell.getPositionY())-15);

        KeyValue xKeyValue,yKeyValue;
        int targetX=(int)GameScene.modifiedX(targetCell.getPositionX())+13;
        int targetY=(int)GameScene.modifiedY(targetCell.getPositionY())-15;
        xKeyValue = new KeyValue(healthProgressBar.getNode().layoutXProperty(),targetX);
        yKeyValue = new KeyValue(healthProgressBar.getNode().layoutYProperty(),targetY);

        KeyFrame keyFrame=new KeyFrame(Duration.seconds((double)Constant.NEXT_TURN_DURATION/1e9),xKeyValue,yKeyValue);
        Timeline timeline=new Timeline(keyFrame);
        timeline.setCycleCount(1);
        timeline.play();
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

    @Override
    public void destroy() throws CellDoesNotExistException {
        super.destroy();
        GameScene.deleteNode(healthProgressBar.getNode());
    }
}
