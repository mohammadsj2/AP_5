package Model.Entity.Animal;

import Controller.Controller;
import Model.Entity.Item;
import Model.Map.Cell;

import java.util.ArrayList;
import Exception.CellDoesNotExistException;

public class Cat extends Animal{
    public Cat(Cell cell) {
        super(cell);
    }

    public Cat(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public void walk() {
        if (this.getLevel() > 1) {
            Cell cur = Controller.getMap().nearestCellWithItem(this.getCell());
            cur = Controller.getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
            this.changeCell(cur);
        } else {
            Cell cur = Controller.getMap().getRandomCell();
            this.changeCell(cur);
        }
    }
    public void catchItem() throws CellDoesNotExistException {
        ArrayList<Item> items = this.getCell().getItems();
        for (Item item : items) {
            Controller.getWareHouse().addItem(item);
            item.setInWareHouse(true);
            item.destroyFromMap();
        }
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
