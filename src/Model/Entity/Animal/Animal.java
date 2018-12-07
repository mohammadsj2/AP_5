package Model.Entity.Animal;

import Model.Entity.Entity;
import Model.Loadable;
import Model.Upgradable;
import Exception.CantUpgrade;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    int level;

    public void walk(){

    }
    public void nextTurn(){

    }

    @Override
    public int upgradeCost() throws CantUpgrade {
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgrade {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
