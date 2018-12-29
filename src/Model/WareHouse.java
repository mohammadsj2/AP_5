package Model;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Model.Entity.Item;

import java.util.ArrayList;
import Exception.CantUpgradeException;
import Exception.NotEnoughMoneyException;
import Exception.NoSuchItemInWarehouseException;
import Exception.NoWarehouseSpaceException;

public class WareHouse implements Upgradable{
    private ArrayList<Item> items=new ArrayList<>();
    private int level=0;
    private int capacity=Constant.WAREHOUSE_CAPACITY;

    private int placeTaken()
    {
        int sum=0;
        for(Item item:items)sum+=item.getVolume();
        return sum;
    }

    public void addItem(Item item) throws NoWarehouseSpaceException {
        if(placeTaken()+item.getVolume()>capacity)
            throw new NoWarehouseSpaceException();
        item.setInWareHouse(true);
        items.add(item);
    }

    public void eraseItem(Item item) throws NoSuchItemInWarehouseException {
        for(Item item2:items)
        {
            if(item.getName().equals(item2.getName()))
            {
                items.remove(item2);
                return ;
            }
        }
        throw new NoSuchItemInWarehouseException();
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.WAREHOUSE_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        capacity+=Constant.WAREHOUSE_CAPACITY_PER_LEVEL;
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.WAREHOUSE_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.WAREHOUSE_MAX_LEVEL;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
