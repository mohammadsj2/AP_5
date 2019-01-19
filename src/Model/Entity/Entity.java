package Model.Entity;

import Controller.*;
import Model.Map.Cell;
import Exception.CellDoesNotExistException;
import Model.Map.Map;
import View.GameScene.GameScene;
import javafx.scene.image.ImageView;

public abstract class Entity {
    private Map map;
    private Cell cell;
    private boolean alive;
    private ImageView imageView=new ImageView();

    public Entity(){
        map=InputReader.getCurrentController().getMap();
    }

    public Entity(Cell cell) {
        map=InputReader.getCurrentController().getMap();
        GameScene.addNode(imageView);
        alive=true;
        this.setCell(cell);
        InputReader.getCurrentController().getMap().addEntity(cell,this);
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
