package Model.Entity;

import Constant.Constant;
import Controller.*;
import Model.Map.Cell;

public class Item extends Entity {
    private String name;
    private int volume,cost,creatingTurn;
    private boolean isInWareHouse=false;



    public Item(String name,int volume,int cost,int creatingTurn){
        this.name=name;
        this.volume=volume;
        this.cost=cost;
        this.creatingTurn=creatingTurn;
    }
    public Item(String name,int volume,int cost,int creatingTurn, Cell cell){
        super(cell);
        this.name=name;
        this.volume=volume;
        this.cost=cost;
        this.creatingTurn=creatingTurn;
    }

    public void setCreatingTurn(int creatingTurn) {
        this.creatingTurn = creatingTurn;
    }

    public boolean isInWareHouse() {
        return isInWareHouse;
    }

    public void setInWareHouse(boolean inWareHouse) {
        isInWareHouse = inWareHouse;
    }
    public boolean isExpired(){
        if(isInWareHouse)return false;
        return (creatingTurn+ Constant.TERM_OF_DESTROY_ITEM_IN_MAP <= InputReader.getCurrentController().getTurn());
    }
    public void expire(){
        getMap().destroyEntity(getCell(),this);
    }
    public int getCost() {
        return cost;
    }

    public int getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item))return false;
        return ((Item) obj).getName().equals(getName());
    }
}
