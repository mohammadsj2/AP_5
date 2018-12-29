package Model.Entity.Animal;

import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;

import java.util.ArrayList;
import Exception.CellDoesNotExistException;
import Exception.NoWarehouseSpaceException;

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
            Cell cur = InputReader.getCurrentController().getMap().nearestCellWithItem(this.getCell());
            cur = InputReader.getCurrentController().getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
            this.changeCell(cur);
        } else {
            Cell cur = InputReader.getCurrentController().getMap().getRandomCell();
            this.changeCell(cur);
        }
    }
    public void catchItem() throws CellDoesNotExistException {
        ArrayList<Item> items = this.getCell().getItems();
        for (Item item : items) {
            try{
            InputReader.getCurrentController().getWareHouse().addItem(item);
            item.setInWareHouse(true);
            item.destroyFromMap();}
            catch(NoWarehouseSpaceException ignored)
            {

            }
        }
    }
}
