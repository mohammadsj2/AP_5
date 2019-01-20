package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.InputReader;
import Exception.CellDoesNotExistException;
import Exception.StartBusyProducerException;
import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import Model.Producer;

import java.util.ArrayList;

public abstract class Pet extends Animal implements Producer {
    public static final int CHANGE_PET_HEALTH_PER_TURN = -3;
    public static final int INCREASE_PET_HEALTH_AFTER_EAT_GRASS = 50;
    public static final int PET_HUNGRY_HEALTH = 50;
    public static final int ANIMAL_PRODUCT_TURN = 6;
    private int health;
    private static final int PET_MAX_HEALTH = 300;
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
        Cell cur=map.nearestCellWithGrass(getCell());
        if (isHungry() &&  cur != null) {
            if (!getCell().haveGrass()) {
                changeCell(map.getBestCellBySpeed(getCell(),cur,Constant.ANIMAL_SPEED));
            } else {
                eatGrass();
                updateHealth(INCREASE_PET_HEALTH_AFTER_EAT_GRASS);
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
        if (getHealth() <= 0) {
            destroy();
        }
        if (getHealth() > PET_MAX_HEALTH) {
            setHealth(PET_MAX_HEALTH);
        }
    }


}
