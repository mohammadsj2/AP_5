package Model.Entity.Animal;

import Model.Entity.Entity;
import Model.Loadable;
import Model.Upgradable;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    int level;

    public void walk(){

    }

    @Override
    public void upgrade() {
        level++;
    }
}