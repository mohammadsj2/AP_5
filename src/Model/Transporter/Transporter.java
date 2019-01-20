package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.StartBusyTransporter;
import Exception.NoTransporterSpaceException;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Upgradable;

import java.util.ArrayList;

public abstract class Transporter implements Upgradable {
    protected int level,capacity,speed,startTime;
    protected ArrayList<Item> items=new ArrayList<>();
    protected boolean busy=false;

    Transporter()
    {

    }

    public void addItem(Item item) throws NoTransporterSpaceException {
        if(!canAddItem(item))
            throw new NoTransporterSpaceException();
        items.add(item);
    }
    public void startTransportation() throws StartBusyTransporter {
        if(busy)
        {
            throw new StartBusyTransporter();
        }
        busy=true;
        startTime=InputReader.getCurrentController().getTurn();
    }
    public boolean isTransportationEnds(){
        return (startTime+Constant.TOWN_DISTANCE/speed>= InputReader.getCurrentController().getTurn());
    }
    public void endTransportation(){
        busy=false;
    }
    public void clear(){
        items.clear();
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }

    public int getFilledVolume()
    {
        int filledVolume=0;
        for(Item item:items)
        {
            filledVolume+=item.getVolume();
        }
        return filledVolume;
    }

    public int getValue() {
        int value=0;
        for(Item item:items)
        {
            value += item.getCost();
        }
        return value;
    }

    public boolean canAddItem(Item item){
        return (getFilledVolume()+item.getVolume()<=capacity);
    }
}
