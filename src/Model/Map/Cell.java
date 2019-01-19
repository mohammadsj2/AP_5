package Model.Map;

import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;
import Exception.CellDoesNotExistException;

import java.util.ArrayList;

public class Cell {
    private ArrayList<Entity> entities=new ArrayList<>();
    private boolean grass=false;
    private int positionX,positionY;

    Cell(int x,int y){
        positionX=x;
        positionY=y;
    }

    void plantGrass(){
        grass=true;
    }
    void destroyGrass(){
        grass=false;
    }

    public boolean haveGrass() {
        return grass;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    void destroyEntity(Entity entity){
        entities.remove(entity);
    }
    void addEntity(Entity entity){
        entities.add(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    boolean haveItem(){
        for(Entity entity:entities){
            if(entity instanceof Item){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Item> getItems(){
        ArrayList<Item> items=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Item){
                items.add((Item)entity);
            }
        }
        return items;
    }
    public ArrayList<Wild> getWilds(){
        ArrayList<Wild> wilds=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Wild){
                wilds.add((Wild)entity);
            }
        }
        return wilds;
    }
    ArrayList<Animal> getAnimals(){
        ArrayList<Animal> animals=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Animal){
                animals.add((Animal)entity);
            }
        }
        return animals;
    }


}
