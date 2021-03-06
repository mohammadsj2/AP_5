package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import Model.Map.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Cow extends Pet{
    public Cow(Cell cell) {
        super(cell);


    }

    @Override
    public void initView() {
        super.initView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Cow/eat.png"));
            changeImageView(image,24,6,4,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Item> getOutputItems() {
        ArrayList<Item> items=new ArrayList<>();
        Item item =Constant.getItemByType("milk");
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        items.add(item);
        return items;
    }
    @Override
    public int upgradeCost() {
        return 0;
    }


    public String getName()
    {
        return "cow";
    }
}
