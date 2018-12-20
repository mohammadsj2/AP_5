package Model.Entity.Animal.Pet;

import Controller.Controller;
import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Producer;
import Exception.StartBusyProducer;
import Exception.CellDoesNotExist;

import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    private int health;
    final public int HEALTH_CAP = 100;

    @Override
    public boolean haveProduct() {
        return false;
    }

    @Override
    public void startProduction() throws StartBusyProducer {

    }

    @Override
    public ArrayList<Item> getInPutItems() {
        return null;
    }

    @Override
    public ArrayList<Item> getOutPutItems() {
        return null;
    }

    @Override
    public void endProduction() {

    }

    public Pet(Cell cell) {
        super(cell);
    }
    public Pet(Cell cell, int level) {
        super(cell, level);
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
    public void nextTurn() throws CellDoesNotExist {
        super.nextTurn();
        this.updateHealth(-1);
    }
    @Override
    public void walk() throws CellDoesNotExist {
        if (isHungry() && Controller.getMap().nearestCellWithGrass(this.getCell()) != null) {
            if (!this.getCell().haveGrass()) {
                changeCell(Controller.getMap().nearestCellWithGrass(this.getCell()));
            } else {
                this.eatGrass();
                this.updateHealth(1);
            }
        } else {
            changeCell(Controller.getMap().getRandomCell());
        }
    }
    public void eatGrass() {
        this.getCell().destroyGrass();
    }
    public void updateHealth(int x) throws CellDoesNotExist {
        this.setHealth(this.getHealth() + x);
        if (this.getHealth() <= 0) {
            this.destroy();
        }
        if (this.getHealth() > this.HEALTH_CAP) {
            this.setHealth(this.HEALTH_CAP);
        }
    }


}
