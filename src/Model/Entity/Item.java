package Model.Entity;

import Constant.Constant;
import Controller.Controller;
import Model.Map.Cell;

public class Item extends Entity {
    private String name;
    private int volume,cost,creatingTurn;
    private boolean isInWareHouse=false;



    public Item(String name,int volume,int cost,int creatingTurn){
        super(null);
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

    public boolean isInWareHouse() {
        return isInWareHouse;
    }

    public void setInWareHouse(boolean inWareHouse) {
        isInWareHouse = inWareHouse;
    }
    public boolean isExpired(){
        if(isInWareHouse)return false;
        return (creatingTurn+ Constant.TERM_OF_DESTROY_ITEM_IN_MAP >= Controller.getTurn());
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


    public boolean equals(Item x)
    {
        if(this.name.equals(x.getName()))return true;
        return false;
    }
}
