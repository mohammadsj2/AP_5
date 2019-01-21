package Model.Entity.Animal;

import Controller.*;
import Model.Map.Cell;
import Model.Entity.Animal.Wild.Wild;
import Exception.CellDoesNotExistException;
import Model.Map.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Dog extends Animal {

    public static final int DOG_SPEED = 4;

    public Dog(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Africa/Dog/down.png"));
            changeImageView(image,24,4,6,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Dog(Cell cell, int level) {
        super(cell, level);
    }

    @Override
    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        Cell cur = map.nearestCellWithWild(getCell());
        if(cur==null){
            super.walk();
            return ;
        }
        if (cur.equals(getCell())) {
            kill();
        } else {
            changeCell(map.getBestCellBySpeed(getCell(),cur, DOG_SPEED));
        }
    }
    public void kill() throws CellDoesNotExistException {
        ArrayList<Wild> wilds = this.getCell().getWilds();
        for (Wild wild : wilds) {
            wild.destroy();
        }
        this.destroy();
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
