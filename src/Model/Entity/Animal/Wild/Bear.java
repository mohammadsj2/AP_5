package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.*;
import Model.Entity.Item;
import Model.Map.Cell;

public class Bear extends Wild{
    public Bear(Cell cell) {
        super(cell);
    }
    public Bear(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public Item toItem() {
        return new Item(Constant.BEAR_NAME, Constant.BEAR_VOLUME, Constant.BEAR_COST, InputReader.getCurrentController().getTurn(), this.getCell());
    }

}
