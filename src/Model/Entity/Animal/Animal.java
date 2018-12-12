package Model.Entity.Animal;

import Model.Entity.Entity;
import Model.Loadable;
import Model.Upgradable;
import Model.Map.Cell;
import Constant.Constant;
import Controller.Controller;
import Exception.CantUpgrade;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    private int level;
    private int speed = Constant.ANIMAL_SPEED;


    public Animal(Cell cell) {
        super(cell);
    }

    public Animal(Cell cell, int level) {
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
        return Controller.getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void walk(){
        Cell cur = Controller.getMap().getNearestCellWithRandom();
        this.changeCell(cur);
    }
    public void changeCell(Cell cur) {
        this.getCell().destroyEntity(this);
        this.setCell(cur);
        cur.addEntity(this);
    }
    public void nextTurn(){
        this.walk();
    }

    @Override
    public int upgradeCost() throws CantUpgrade {
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgrade {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
