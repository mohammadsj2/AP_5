package Model;

import Constant.Constant;
import Controller.*;
import Exception.CantUpgradeException;
import Exception.NotEnoughMoneyException;
import Exception.NoWaterException;

public class Well implements Upgradable,Loadable{
    public static final int WELL_MAX_LEVEL = 3;
    private int level=0;
    private int waterRemaining=Constant.WELL_BASE_WATER,maxWater=Constant.WELL_BASE_WATER;

    private int fillCost()
    {
        return Constant.WELL_FILL_COST+level*Constant.WELL_FILL_COST_PER_LEVEL;
    }

    public void fill()
    {
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
        return level<WELL_MAX_LEVEL;
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= WELL_MAX_LEVEL){
            throw new CantUpgradeException();
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
