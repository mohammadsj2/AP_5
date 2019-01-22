package Model.Entity.Animal.Wild;

import Exception.CellDoesNotExistException;
import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Dog;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Map.Cell;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public abstract class Wild extends Animal {
    protected Wild(Cell cell) {
        super(cell);
    }
    protected Wild(Cell cell, int level) {
        super(cell, level);
    }
    public boolean kill(Cell cell) throws CellDoesNotExistException {
        Entity[] entities = cell.getEntities().toArray(new Entity[0]);
        boolean dog_near = false;
        for (int j = 0; j < entities.length; ++j) if (entities[j].getAlive()) {
            Entity e = entities[j];
            if (!(e instanceof Wild)) {
                if (e instanceof Dog)
                    dog_near = true;
                e.destroy();
            }
        }
        return dog_near;
    }

    @Override
    public void nextTurn() throws CellDoesNotExistException {
        super.nextTurn();
        int range = 2;
        boolean die = false;
        int x = getCell().getPositionX();
        int y = getCell().getPositionY();
        ArrayList<Cell> inRange = getMap().getNearbyCells(getCell(), 2);
        for (Cell cell : inRange) {
            if (kill(cell)) {
                die = true;
            }
        }

        if (die) {
            this.destroy();
        }

    }
    @Override
    public void initView() {
        super.initView();
        getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    System.out.println("CLICKED ALJDKASHLKFHASLFHKAS");
                    getMap().cage(getCell().getPositionX(), getCell().getPositionY());
                } catch (CellDoesNotExistException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cage() throws CellDoesNotExistException {
        Cell cur = this.getCell();
        this.destroyFromMap();
        getMap().addEntity(cur,toItem());
    }
    abstract public Item toItem();


}
