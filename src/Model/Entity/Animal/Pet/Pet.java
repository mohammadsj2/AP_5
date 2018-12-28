package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.*;
import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Producer;
import Exception.StartBusyProducerException;
import Exception.CellDoesNotExistException;

import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    private int health;
    final public int HEALTH_CAP = 100;

    @Override
    public boolean haveProduct() {
        return false;
    }

    @Override
    public void startProduction() throws StartBusyProducerException {

    }

    @Override
    public ArrayList<Item> getInputItems() {
        return null;
    }

    @Override
    public ArrayList<Item> getOutputItems() {
        return null;
    }

    @Override
    public void endProduction() {

    }

    protected Pet(Cell cell) {
        super(cell);
        this.health=Constant.INIT_HEALTH;
    }
    protected Pet(Cell cell, int level) {
        super(cell, level);
        this.health=Constant.INIT_HEALTH;
    }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHungerLimit() {
        return 10;
    }
    public boolean isHungry() {
        return this.getHealth() <= getHungerLimit();
    }
    @Override
    public void nextTurn() throws CellDoesNotExistException {
        super.nextTurn();
        this.updateHealth(-1);
    }
    @Override
    public void walk() throws CellDoesNotExistException {
        if (isHungry() && InputReader.getCurrentController().getMap().nearestCellWithGrass(this.getCell()) != null) {
            if (!this.getCell().haveGrass()) {
                changeCell(InputReader.getCurrentController().getMap().nearestCellWithGrass(this.getCell()));
            } else {
                this.eatGrass();
                this.updateHealth(1);
            }
        } else {
            changeCell(InputReader.getCurrentController().getMap().getRandomCell());
        }
    }
    public void eatGrass() {
        this.getCell().destroyGrass();
    }
    public void updateHealth(int x) throws CellDoesNotExistException {
        this.setHealth(this.getHealth() + x);
        if (this.getHealth() <= 0) {
            this.destroy();
        }
        if (this.getHealth() > this.HEALTH_CAP) {
            this.setHealth(this.HEALTH_CAP);
        }
    }


}
