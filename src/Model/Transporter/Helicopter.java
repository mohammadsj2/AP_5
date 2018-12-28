package Model.Transporter;

import Constant.Constant;
import Exception.CantUpgradeException;

public class Helicopter extends Transporter {

    public Helicopter()
    {
        speed=Constant.HELICOPTER_SPEED;
        capacity=Constant.HELICOPTER_CAPACITY;
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.HELICOPTER_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        capacity+=Constant.HELICOPTER_CAPACITY_PER_LEVEL;
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.HELICOPTER_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.HELICOPTER_MAX_LEVEL;
    }
}
