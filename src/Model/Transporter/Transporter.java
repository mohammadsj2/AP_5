package Model.Transporter;

import Model.Entity.Item;

import java.util.ArrayList;

public abstract class Transporter {
    int level,capacity,speed,startTime;
    ArrayList<Item> items=new ArrayList<>();
    boolean busy=false;

    public boolean addItem(Item item){
        return false;
    }
    public void startTransportation(){

    }
    public boolean isTransportationEnds(){
        return true;
    }
    public void endTransportation(){

    }
    public void clear(){

    }

}