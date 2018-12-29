package Model.Entity.Animal;

import Controller.*;
import Model.Map.Cell;
import Model.Entity.Animal.Wild.Wild;
import Exception.CellDoesNotExistException;
import Model.Map.Map;

import java.util.ArrayList;

public class Dog extends Animal {

    public static final int DOG_SPEED = 4;

    public Dog(Cell cell) {
        super(cell);
    }
    public Dog(Cell cell, int level) {
        super(cell, level);
    }

    @Override
    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        Cell cur = map.nearestCellWithWild(this.getCell());
        if (cur.equals(this.getCell())) {
            kill();
        } else {
            changeCell(map.getBestCellBySpeed(getCell(),cur, DOG_SPEED));
        }
    }
    public void kill() throws CellDoesNotExistException {
        ArrayList<Wild> wilds = this.getCell().getWilds();
        for (Wild wild : wilds) {
            wild.destroy();
        }
        this.destroy();
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
