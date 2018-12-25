package Model;

import Constant.Constant;
import Controller.Controller;
import Model.Entity.Item;

import java.util.ArrayList;
import Exception.CantUpgradeException;
import Exception.NotEnoughMoneyException;

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

    public boolean addItem(Item item)
    {
        if(placeTaken()+item.getVolume()>capacity)return false;
        items.add(item);
        return true;
    }

    public boolean eraseItem(Item item)
    {
        for(Item item2:items)
        {
            if(item.equals(item2))
            {
                items.remove(item2);
                return true;
            }
        }
        return false;
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        try
        {
            Controller.subtractMoney(upgradeCost());
        }
        catch(NotEnoughMoneyException e)
        {
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
        return upgradeCost()<=Controller.getMoney();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
