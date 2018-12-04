package Model.Map;

import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;

public class Cell {
    ArrayList<Entity> entities=new ArrayList<>();
    boolean grass=false;
    int positionX,positionY;

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
    void nextTurn(){
        ArrayList<Entity> copyOfEntities=new ArrayList<>();
        for(Entity entity:entities){
            copyOfEntities.add(entity);
        }
        for(Entity entity:copyOfEntities){
            if(entity instanceof Animal) {
                ((Animal) entity).nextTurn();
            }else if(entity instanceof Item){
                Item item=(Item)entity;
                if(item.isExpired()){
                    destroyEntity(entity);
                }
            }
        }
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void destroyEntity(Entity entity){
        entities.remove(entity);
    }
    public void addEntity(Entity entity){
        entities.add(entity);
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
