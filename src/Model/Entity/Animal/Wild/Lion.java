package Model.Entity.Animal.Wild;

import Exception.CellDoesNotExistException;
import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;
import View.Scene.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Lion extends Wild{

    public static final String CAGED_LION = "cagedlion";

    public Lion(Cell cell) {
        super(cell);
        Image image;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Lion/left.png"));
            changeImageView(image,24,8,3,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_LION);
        item.initView();
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        try {
            this.destroy();
        } catch (CellDoesNotExistException e) {
            e.printStackTrace();
        }
        GameScene.addNode(item.getImageView());
        item.changeImageView(item.getImageView().getImage(),1,1,1, getCell().getPositionX(),getCell().getPositionY());
        return item;
    }

}
