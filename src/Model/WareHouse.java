package Model;

import Model.Entity.Item;

import java.util.ArrayList;
import Exception.CantUpgrade;

public class WareHouse implements Upgradable{
    ArrayList<Item> items=new ArrayList<>();
    int level;

    public boolean addItem(Item item){
        return false;
    }
    public boolean eraseItem(Item item){
        return false;
    }

    @Override
    public void upgrade() throws CantUpgrade {

    }

    @Override
    public int upgradeCost() throws CantUpgrade {
        return 0;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
