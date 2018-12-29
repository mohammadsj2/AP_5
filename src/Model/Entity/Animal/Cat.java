package Model.Entity.Animal;

import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;

import java.util.ArrayList;

import Exception.CellDoesNotExistException;
import Exception.NoWarehouseSpaceException;
import Model.Map.Map;

public class Cat extends Animal {

    public static final int CAT_SPEED = 4;

    public Cat(Cell cell) {
        super(cell);
    }

    public Cat(Cell cell, int level) {
        super(cell, level);
    }

    @Override
    public int getSpeed() {
        return CAT_SPEED;
    }

    @Override
    public void nextTurn() throws CellDoesNotExistException {
        walk();

    }

    @Override
    public void walk() {
        Map map = InputReader.getCurrentController().getMap();
        Cell targetCell = map.getRandomCell();
        if (InputReader.getCurrentController().getCatLevel() > 0) {
            targetCell = map.nearestCellWithItem(this.getCell());
        }
        targetCell = map.getBestCellBySpeed(this.getCell(), targetCell, getSpeed());
        this.changeCell(targetCell);
    }

    public void catchItem(){
        ArrayList<Item> items = this.getCell().getItems();
        for (Item item : items) {
            try {
                InputReader.getCurrentController().addItemToWareHouse(item);
            } catch (NoWarehouseSpaceException ignored) {

            }
        }
    }
}
