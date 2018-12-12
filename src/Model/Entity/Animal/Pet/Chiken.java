package Model.Entity.Animal.Pet;

import Model.Map.Cell;

public class Chiken extends Pet {
    public Chiken(Cell cell) {
        super(cell);
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
