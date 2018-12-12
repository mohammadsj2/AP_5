package Model.Entity.Animal.Wild;

import Constant.Constant;
import Controller.Controller;
import Model.Entity.Item;
import Model.Map.Cell;

public class Lion extends Wild{
    public Lion(Cell cell) {
        super(cell);
    }
    public Lion(Cell cell, int level) {
        super(cell, level);
    }
    @Override
    public Item toItem() {
        return new Item(Constant.LION_NAME, Constant.LION_VOLUME, Constant.LION_COST, Controller.getTurn(), this.getCell());
    }

}
