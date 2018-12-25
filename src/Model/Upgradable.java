package Model;

import Exception.CantUpgradeException;

public interface Upgradable {
    void upgrade()throws CantUpgradeException;
    int upgradeCost()throws CantUpgradeException;
    boolean canUpgrade();
}
