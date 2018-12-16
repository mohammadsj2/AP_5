package Model;

import Constant.Constant;
import Controller.Controller;
import Exception.CantUpgrade;
import Exception.NotEnoughMoneyException;
import Exception.NoWaterException;

public class Well implements Upgradable,Loadable{
    private int level=0;
    private int waterRemaining=Constant.WELL_BASE_WATER,maxWater=Constant.WELL_BASE_WATER;

    private int fillCost()
    {
        return Constant.WELL_FILL_COST+level*Constant.WELL_FILL_COST_PER_LEVEL;
    }

    public void fill() throws NotEnoughMoneyException
    {
        Controller.subtractMoney(fillCost());
        waterRemaining=maxWater;
    }
    public void liftWater() throws NoWaterException
    {
        if(waterRemaining==0)throw new NoWaterException();
        waterRemaining--;
    }

    @Override
    public int upgradeCost() {
        return (level + 1) * Constant.WELL_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return upgradeCost()<=Controller.getMoney();
    }

    @Override
    public void upgrade() throws CantUpgrade {
        try
        {
            Controller.subtractMoney(upgradeCost());
        }
        catch(NotEnoughMoneyException e)
        {
            throw new CantUpgrade();
        }
        level++;
        maxWater+=Constant.WELL_WATER_PER_LEVEL;
        waterRemaining=maxWater;

    }

    public int getWaterRemaining() {
        return waterRemaining;
    }


    public int getLevel() {
        return level;
    }
}
