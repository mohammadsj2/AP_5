package Model.Entity.Animal.Pet;

import Model.Map.Cell;

public class Cow extends Pet{
    public Cow(Cell cell) {
        super(cell);
    }
    @Override
    public int upgradeCost() {
        return 0;
    }
}
