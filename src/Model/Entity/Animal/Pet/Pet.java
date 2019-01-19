package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.*;
import Model.Entity.Animal.Animal;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Producer;
import Exception.StartBusyProducerException;
import Exception.CellDoesNotExistException;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    public static final int CHANGE_PET_HEALTH_PER_TURN = -3;
    public static final int INCREASE_PET_HEALTH_AFTER_EAT_GRASS = 20;
    public static final int PET_HUNGRY_HEALTH = 30;
    public static final int ANIMAL_PRODUCT_TURN = 3;
    private int health;
    final public int PET_MAX_HEALTH = 100;
    private int lastProductTurn=0;

    @Override
    public boolean haveProduct() {
        return InputReader.getCurrentController().getTurn()>=lastProductTurn+ ANIMAL_PRODUCT_TURN;
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
        lastProductTurn= InputReader.getCurrentController().getTurn();
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

    public boolean isHungry() {
        return this.getHealth() <= PET_HUNGRY_HEALTH;
    }
    @Override
    public void nextTurn() throws CellDoesNotExistException {
        super.nextTurn();
        this.updateHealth(CHANGE_PET_HEALTH_PER_TURN);
        if(haveProduct()){
            if(getOutputItems()!=null) {
                for (Item item : getOutputItems()) {
                    getMap().addEntity(getCell(),item);
                }
                endProduction();
            }
        }
    }
    @Override
    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        Cell cur=map.nearestCellWithGrass(this.getCell());
        if (isHungry() &&  cur != null) {
            if (!this.getCell().haveGrass()) {
                changeCell(map.getBestCellBySpeed(getCell(),cur,Constant.ANIMAL_SPEED));
            } else {
                this.eatGrass();
                this.updateHealth(INCREASE_PET_HEALTH_AFTER_EAT_GRASS);
            }
        } else {
            super.walk();
        }
    }
    public void eatGrass() {
        Map map=InputReader.getCurrentController().getMap();
        map.destroyGrass(getCell());
    }
    public void updateHealth(int x) throws CellDoesNotExistException {
        this.setHealth(this.getHealth() + x);
        if (this.getHealth() <= 0) {
            this.destroy();
        }
        if (this.getHealth() > this.PET_MAX_HEALTH) {
            this.setHealth(this.PET_MAX_HEALTH);
        }
    }


}
