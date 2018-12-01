package Model;

import Model.Entity.Item;

import java.util.ArrayList;

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
    public int upgradeCost() {
        return 0;
    }

    @Override
    public void upgrade() {

    }
}
