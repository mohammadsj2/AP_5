package Model.Entity.Animal;

import Controller.InputReader;
import Exception.CellDoesNotExistException;
import Exception.NoWarehouseSpaceException;
import Model.Entity.Item;
import Model.Map.Cell;
import Model.Map.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Cat extends Animal {

    public static final int CAT_SPEED = 4;

    public Cat(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Africa/Cat/down.png"));
            changeImageView(image,24,4,6,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Cat(Cell cell, int level) {
        this(cell);
        setLevel(level);
    }

    @Override
    public int getSpeed() {
        return CAT_SPEED;
    }

    @Override
    public void nextTurn() throws CellDoesNotExistException {
        walk();

    }

    @Override
    public void walk() {
        Map map = InputReader.getCurrentController().getMap();
        Cell targetCell = map.getRandomCell(getCell(), getSpeed());
        if (InputReader.getCurrentController().getCatLevel() >= 0) {
            targetCell = map.nearestCellWithItem(this.getCell());
            if (targetCell == null) {
                targetCell = map.getRandomCell(getCell(), getSpeed());
            } else if (targetCell.getPositionY() == getCell().getPositionY()
                        && targetCell.getPositionX() == getCell().getPositionX()) {
                catchItem();
            }
        }
        targetCell = map.getBestCellBySpeed(this.getCell(), targetCell, getSpeed());
        this.changeCell(targetCell);
    }

    public void catchItem(){
        ArrayList<Item> items = this.getCell().getItems();
        for (Item item : items) {
            try {
                InputReader.getCurrentController().addItemToWareHouse(item);
            } catch (NoWarehouseSpaceException ignored) {

            }
        }
    }
}
