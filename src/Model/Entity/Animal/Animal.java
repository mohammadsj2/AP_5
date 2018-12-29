package Model.Entity.Animal;

import Model.Entity.Entity;
import Model.Loadable;
import Model.Upgradable;
import Model.Map.Cell;
import Constant.Constant;
import Controller.*;
import Exception.CantUpgradeException;
import Exception.CellDoesNotExistException;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    private int level;
    private int speed = Constant.ANIMAL_SPEED;


    protected Animal(Cell cell) {
        super(cell);
    }

    protected Animal(Cell cell, int level) {
        super(cell);
        setLevel(level);
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Cell walkTowards(Cell cur) {
        return InputReader.getCurrentController().getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


    // TODO walk piade sazi bshe

    public void walk() throws CellDoesNotExistException {
        Cell cur = InputReader.getCurrentController().getMap().getRandomCell();
        this.changeCell(cur);
    }
    public void changeCell(Cell cur) {
        this.getCell().destroyEntity(this);
        this.setCell(cur);
        cur.addEntity(this);
    }
    public void nextTurn() throws CellDoesNotExistException {
        this.walk();
    }

    @Override
    public int upgradeCost() throws CantUpgradeException {
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgradeException {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
