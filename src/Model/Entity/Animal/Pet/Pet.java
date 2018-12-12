package Model.Entity.Animal.Pet;

import Controller.Controller;
import Model.Entity.Animal.Animal;
import Model.Map.Cell;
import Model.Producer;

public abstract class Pet extends Animal {
    private int health;
    final public int HEALTH_CAP = 100;
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
    public void nextTurn() {
        super.nextTurn();
        this.updateHealth(-1);
    }
    @Override
    public void walk() {
        if (isHungry() && Controller.getMap().getNearestCellWithGrass() != null) {
            if (!this.getCell().haveGrass()) {
                changeCell(Controller.getMap().getNearestCellWithGrass());
            } else {
                this.eatGrass();
                this.updateHealth(1);
            }
        } else {
            changeCell(Controller.getMap().getNearestCellWithRandom());
        }
    }
    public void eatGrass() {
        this.getCell().destroyGrass();
    }
    public void updateHealth(int x) {
        this.setHealth(this.getHealth() + x);
        if (this.getHealth() <= 0) {
            this.destroy();
        }
        if (this.getHealth() > this.HEALTH_CAP) {
            this.setHealth(this.HEALTH_CAP);
        }
    }


}
