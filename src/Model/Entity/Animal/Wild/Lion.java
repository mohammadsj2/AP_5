package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Lion extends Wild{

    public static final String CAGED_LION = "cagedlion";

    public Lion(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
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
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
