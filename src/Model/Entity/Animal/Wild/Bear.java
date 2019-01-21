package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Bear extends Wild{

    public static final String CAGED_BROWN_BEAR = "cagedbrownbear";

    public Bear(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Grizzly/down.png"));
            changeImageView(image,24,6,4,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_BROWN_BEAR);
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
