package Model.Entity;

import Controller.*;
import Model.Map.Cell;
import Exception.CellDoesNotExistException;
import Model.Map.Map;
import Model.Viewable;
import View.GameScene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public abstract class Entity implements Viewable {
    private Map map;
    private Cell cell;
    private boolean alive;
    private ImageView imageView=new ImageView();

    public Entity(){
        if(InputReader.getCurrentController()!=null)
            map=InputReader.getCurrentController().getMap();
    }

    public Entity(Cell cell) {
        map=InputReader.getCurrentController().getMap();
        GameScene.addNode(imageView);
        alive=true;
        this.setCell(cell);
        InputReader.getCurrentController().getMap().addEntity(cell,this);
    }

    @Override
    public void refreshView() {

    }//TODO

    @Override
    public void initView() {

    }//TODO

    @Override
    public void changeImageView(Image image, int count, int rows, int columns, int x, int y) {
        ImageView imageView=getImageView();

        imageView.setImage(image);
        GameScene.setImageViewPositionOnMap(imageView,x,y);
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
    public void setAnimation(Animation animation)
    {

    }

    @Override
    public Animation getAnimation()
    {
        return null;
    }

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
    }
    public void destroyFromMap() throws CellDoesNotExistException {
        if(getCell()==null){
            throw new CellDoesNotExistException();
        }
        InputReader.getCurrentController().getMap().destroyEntity(getCell(),this);
    }
    public void destroy() throws CellDoesNotExistException {
        this.destroyFromMap();
        setAlive(false);
    }

}
