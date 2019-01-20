package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import Model.Map.Cell;
import View.GameScene.GameScene;
import View.SpriteAnimation;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Chicken extends Pet {
    public Chicken(Cell cell) {
        super(cell);
        ImageView imageView=getImageView();
        Image image= null;
        try {
            image = new Image(new FileInputStream("./Textures/Animals/Africa/GuineaFowl/eat.png"));
            changeImageView(image,24,5,5,cell.getPositionX(),cell.getPositionY());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<Item> getOutputItems() {
        ArrayList<Item> items=new ArrayList<>();
        Item item =Constant.getItemByType("egg");
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        items.add(item);
        return items;
    }

    @Override
    public int upgradeCost() {
        return 0;
    }
}
