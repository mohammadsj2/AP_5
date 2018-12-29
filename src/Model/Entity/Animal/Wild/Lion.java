package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;

public class Lion extends Wild{

    public static final String CAGED_LION = "cagedlion";

    public Lion(Cell cell) {
        super(cell);
    }
    public Lion(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_LION);
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
