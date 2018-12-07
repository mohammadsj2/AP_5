package Model;

import Exception.CantUpgrade;

public interface Upgradable {
    void upgrade()throws CantUpgrade;
    int upgradeCost()throws CantUpgrade;
    boolean canUpgrade();
}
