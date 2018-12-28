package Model.Entity.Animal.Wild;

import Model.Entity.Animal.Animal;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Map.Cell;
import Exception.CellDoesNotExistException;

import java.util.ArrayList;

public abstract class Wild extends Animal {
    protected Wild(Cell cell) {
        super(cell);
    }
    protected Wild(Cell cell, int level) {
        super(cell, level);
    }
    public void kill(Cell cell) throws CellDoesNotExistException {
        ArrayList<Entity> entities = cell.getEntities();
        for (Entity e : entities) {
            if (!(e instanceof Wild)) {
                e.destroy();
            }
        }
    }
    public void cage() throws CellDoesNotExistException {
        Cell cur = this.getCell();
        this.destroyFromMap();
        cur.addEntity(this.toItem());
    }
    abstract public Item toItem();


}
