package Model;

import Exception.CantUpgrade;

public class Well implements Upgradable,Loadable{
    int level,waterRemaining,maxWater;
    public void fill(){

    }
    public void liftWater(){

    }

    @Override
    public int upgradeCost() throws CantUpgrade {
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgrade {

    }

    public int getWaterRemaining() {
        return waterRemaining;
    @Override
    public boolean canUpgrade() {
        return false;
    }
}
