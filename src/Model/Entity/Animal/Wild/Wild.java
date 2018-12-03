package Model.Entity.Animal.Wild;

import Model.Entity.Animal.Animal;
import Model.Entity.Item;
import Model.Map.Cell;

public abstract class Wild extends Animal {
    public void destroy(Cell cell){

    }
    abstract public Item toItem();

}
