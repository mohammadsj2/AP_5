package Model.Entity.Animal;

import Controller.Controller;
import Model.Map.Cell;
import Model.Entity.Animal.Wild.Wild;
import Exception.CellDoesNotExistException;

import java.util.ArrayList;

public class Dog extends Animal {
    public Dog(Cell cell) {
        super(cell);
    }
    public Dog(Cell cell, int level) {
        super(cell, level);
    }
    @Override

    public void walk() throws CellDoesNotExistException {
        Cell cur = Controller.getMap().nearestCellWithWild(this.getCell()); // injaro mn edit krdm check she doros bashe
        if (cur.equals(this.getCell())) {
            this.kill();
        } else {
            this.changeCell(cur);
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
