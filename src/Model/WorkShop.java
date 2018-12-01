package Model;

import Model.Entity.Item;

import java.util.ArrayList;

public class WorkShop implements Producer,Upgradable{
    ArrayList<Item> inputs=new ArrayList<>(),outputs=new ArrayList<>();
    int location,level,startTime;
    String name;
    boolean busy;

    @Override
    public int upgradeCost() {
        return 0;
    }

    @Override
    public void upgrade() {

    }
}
