package Model.Transporter;

import Constant.Constant;
import Exception.CantUpgradeException;

import Model.Entity.Item;
import Model.Upgradable;

public class Truck extends Transporter{

    public Truck()
    {
        speed=Constant.TRUCK_SPEED;
        capacity=Constant.TRUCK_CAPACITY;
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.TRUCK_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        capacity+=Constant.TRUCK_CAPACITY_PER_LEVEL;
        speed+=Constant.TRUCK_SPEED_PER_LEVEL;
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.TRUCK_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.TRUCK_MAX_LEVEL;
    }
}
