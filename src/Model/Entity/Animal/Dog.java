package Model.Entity.Animal;

import Constant.Constant;
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

    public Dog(Cell cell) {
        super(cell);
    }

    @Override
    public void initView() {
        super.initView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Dog/down.png"));
            changeImageView(image,24,4,6,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            changeCell(map.getBestCellBySpeed(getCell(),cur, Constant.DOG_SPEED));
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


    public String getName()
    {
        return "dog";
    }
}
