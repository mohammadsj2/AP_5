package Model.Entity.Animal;

import Model.Entity.Entity;
import Model.Loadable;
import Model.Map.Map;
import Model.Upgradable;
import Model.Map.Cell;
import Constant.Constant;
import Controller.*;
import Exception.CantUpgradeException;
import Exception.CellDoesNotExistException;
import javafx.scene.image.ImageView;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    private int level;
    private int speed = Constant.ANIMAL_SPEED;
    private int step=0;
    private Cell currentCell;


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


    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        System.out.println(step);
        if(currentCell==null || step==0 || getCell()==currentCell)
        {
            System.out.println("KIR");
            currentCell = map.getRandomCell(getCell(), getSpeed());
        }
        this.changeCell(map.getBestCellBySpeed(getCell(),currentCell,Constant.ANIMAL_SPEED));
    }
    public void changeCell(Cell cur) {
        Map map=getMap();

        map.destroyEntity(getCell(),this);
        setCell(cur);
        map.addEntity(cur,this);
    }
    public void nextTurn() throws CellDoesNotExistException {
        this.walk();
        step++;
        step%=15;
    }

    @Override
    public int upgradeCost(){
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
