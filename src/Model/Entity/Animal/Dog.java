package Model.Entity.Animal;

import Controller.Controller;
import Model.Map.Cell;
import Model.Entity.Animal.Wild.Wild;
import Exception.CellDoesNotExist;

import java.util.ArrayList;

public class Dog extends Animal {
    public Dog(Cell cell) {
        super(cell);
    }
    public Dog(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public void walk() throws CellDoesNotExist {
        Cell cur = Controller.getMap().nearestCellWithWild(getCell());
        if (cur.equals(this.getCell())) {
            this.kill();
        } else {
            this.changeCell(cur);
        }
    }
    public void kill() throws CellDoesNotExist {
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
