package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;

public class Bear extends Wild{

    public static final String CAGED_BROWN_BEAR = "cagedbrownbear";

    public Bear(Cell cell) {
        super(cell);
    }
    public Bear(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public Item toItem() {
        Item item=Constant.getItemByType(CAGED_BROWN_BEAR);
        item.setCreatingTurn(InputReader.getCurrentController().getTurn());
        item.setCell(getCell());
        return item;
    }

}
