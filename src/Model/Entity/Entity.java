package Model.Entity;

import Controller.*;
import Model.Map.Cell;
import Exception.CellDoesNotExistException;

public abstract class Entity {
    private Cell cell;
    private boolean alive;

    public Entity(){

    }

    public Entity(Cell cell) {
        alive=true;
        this.setCell(cell);
        cell.addEntity(this);
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
        getCell().destroyEntity(this);
    }
    public void destroy() throws CellDoesNotExistException {
        this.destroyFromMap();
        setAlive(false);
    }

}
