package Model.Entity.Animal;

import Controller.Controller;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.WareHouse;
import java.util.ArrayList;

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
            Cell cur = Controller.getMap().getNearestCellWithItem();
            cur = Controller.getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
            this.changeCell(cur);
        } else {
            Cell cur = Controller.getMap().getNearestCellWithRandom();
            this.changeCell(cur);
        }
    }
    public void catchItem() {
        ArrayList<Item> items = this.getCell().getItems();
        for (Item item : items) {
            Controller.Warehouse.addItem(item);
            item.setInWareHouse(true);
            item.destroyFromMap();
        }
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
