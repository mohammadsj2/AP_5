package Model;

public class Well implements Upgradable,Loadable{
    int level,waterRemaining,maxWater;
    public void fill(){

    }
    public void liftWater(){

    }

    @Override
    public int upgradeCost() {
        return 0;
    }

    @Override
    public void upgrade() {

    }

    public int getWaterRemaining() {
        return waterRemaining;
    }
}
