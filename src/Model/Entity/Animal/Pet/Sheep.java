package Model.Entity.Animal.Pet;

import Model.Map.Cell;

public class Sheep extends Pet{
    public Sheep(Cell cell) {
        super(cell);
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
