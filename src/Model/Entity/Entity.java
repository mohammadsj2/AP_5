
package Model.Entity;

import Controller.InputReader;
import Exception.CellDoesNotExistException;
import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Cow;
import Model.Entity.Animal.Pet.Pet;
import Model.Entity.Animal.Pet.Sheep;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Viewable;
import View.Scene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Entity implements Viewable {
    private Map map;
    protected Cell cell;
    private boolean alive;
    private ImageView imageView=new ImageView();
    private Animation animation=null;

    public Entity(){
        if(InputReader.getCurrentController()!=null)
        {
            map = InputReader.getCurrentController().getMap();
        }
        initView();
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Entity(Cell cell) {
        map=InputReader.getCurrentController().getMap();
        GameScene.addNode(imageView);
        alive=true;
        this.setCell(cell);
        InputReader.getCurrentController().getMap().addEntity(cell,this);
        initView();
    }

    @Override
    public void refreshView() {
    }

    @Override
    public void initView() {
        if(getImageView()==null){
            setImageView(new ImageView());
        }
    }

    @Override
    public void changeImageView(Image image, int count, int rows, int columns, double x, double y)
    {
        ImageView imageView = getImageView();

        imageView.setImage(image);
        GameScene.setImageViewPositionOnMap(imageView, x, y);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();

        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        if(animation!=null)
            animation.stop();
        animation= new SpriteAnimation(
                imageView,
                Duration.millis(1000),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
 //       if (animation instanceof SpriteAnimation)
   //         this.animation = (SpriteAnimation) animation;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    public abstract String getName();

    public Map getMap() {
        return map;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean getAlive() {
        return alive;
    }
    public void setAlive(boolean cur) {
        this.alive = cur;
    }
    public boolean isAlive() { return getAlive(); }


    public Cell getCell() {
        return cell;
    }
    public void setCell(Cell x) {
        cell = x;
        refreshView();
    }
    public void destroyFromMap() throws CellDoesNotExistException {
        if(getCell()==null){
            throw new CellDoesNotExistException();
        }
        InputReader.getCurrentController().getMap().destroyEntity(getCell(),this);
    }
    public void destroy() throws CellDoesNotExistException {
        if (this instanceof Pet) {
            Pet cur = (Pet) this;
            int rows = 0;
            int columns = 0;
            int count = 24;
            String animalName = "";
            if (this instanceof Cow) {
                rows = 8;
                columns = 3;
                animalName = "Cow";
            } else if (this instanceof Sheep) {
                rows = 6;
                columns = 4;
                animalName = "Sheep";
            } else if (this instanceof Chicken) {
                rows = 5;
                columns = 5;
                count = 23;
                animalName = "GuineaFowl";
            }
            Image image = null;
            try {
                image = new Image(new FileInputStream("./Textures/Animals/" + animalName + "/death.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (cur.getTimeDead() == 0)
                cur.changeImageView(image, count, rows, columns, getCell().getPositionX(), getCell().getPositionY(), true);
            if (cur.getTimeDead() > 2) {
                this.destroyFromMap();
            }
            cur.setTimeDead(cur.getTimeDead() + 1);
            setAlive(false);

        } else {
            this.destroyFromMap();
            setAlive(false);
        }
    }


    public void initLoad(){
        initView();
        GameScene.addNode(getImageView());
    }
}
