package Model.Entity.Animal.Pet;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import Model.Map.Cell;

import java.util.ArrayList;

public class Cow extends Pet{
    public Cow(Cell cell) {
        super(cell);
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
}
