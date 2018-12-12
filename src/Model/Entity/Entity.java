package Model.Entity;

import Controller.Controller;
import Model.Map.Cell;

public abstract class Entity {
    private Cell cell;
    private boolean alive;
    public Entity(Cell cell) {
        this.setCell(cell);
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
    public void destroyFromMap() {
        Controller.getMap().destroyEntity(this.getCell().getPositionX(), this.getCell().getPositionY(), this);
    }
    public void destroy() {
        this.destroyFromMap();
        setAlive(false);
    }
}
