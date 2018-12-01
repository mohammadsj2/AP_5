package Model.Entity.Animal.Wild;

import Model.Entity.Item;

public class Bear extends Wild{
    @Override
    Item toItem() {
        return null;
    }

    @Override
    public void upgrade() {
        super.upgrade();
    }

    @Override
    public int upgradeCost() {
        return 0;
    }
}
