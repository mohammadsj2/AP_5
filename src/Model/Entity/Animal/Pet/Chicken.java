package Model.Entity.Animal.Pet;

import Model.Map.Cell;

public class Chicken extends Pet {
    public Chicken(Cell cell) {
        super(cell);
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
