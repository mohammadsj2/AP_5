package Model.Map;

import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;

public class Cell {
    ArrayList<Entity> entities=new ArrayList<>();
    boolean grass=false;
    int positionX,positionY;

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

    public void destroyEntity(Entity entity){

    }
    public void addEntity(Entity entity){

    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    public boolean haveItem(){
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

}
