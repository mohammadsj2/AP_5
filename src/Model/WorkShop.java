package Model;

import Controller.Controller;
import Model.Entity.Item;
import java.util.ArrayList;
import Exception.*;
import Constant.Constant;

public class WorkShop implements Producer,Upgradable{
    ArrayList<Item> inputs,outputs;
    int location,level,startTime,produceDuration;
    String name;
    boolean busy,isCustom;

    WorkShop(String name,int location,boolean isCustom,ArrayList<Item> inputs
            ,ArrayList<Item> outputs,int produceDuration){
        this.name=name;
        this.location=location;
        this.isCustom=isCustom;
        this.inputs=inputs;
        this.outputs=outputs;
        this.produceDuration=produceDuration;
    }

    @Override
    public boolean canUpgrade() {
        return (level== Constant.MAX_WORKSHOP_LEVEL || isCustom);
    }

    @Override
    public int upgradeCost() throws CantUpgradeException {
        if(!canUpgrade()){
            throw new CantUpgradeException();
        }
        return Constant.WORKSHOP_UPGRADE_COST_PER_LEVEL *(level+1);
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(!canUpgrade()){
            throw new CantUpgradeException();
        }
        level++;
    }

    public int getProduceDuration() {
        return produceDuration;
    }

    @Override
    public boolean haveProduct() {
        return (busy && Controller.getTurn()>=startTime+getProduceDuration());
    }

    @Override
    public void startProduction() throws StartBusyProducerException {
        if(busy){
            throw new StartBusyProducerException();
        }
        busy=true;
        startTime=Controller.getTurn();
    }

    private ArrayList<Item> multipleItems(ArrayList<Item> items,int cnt){
        ArrayList<Item> answer=new ArrayList<>();
        for(int i=0;i<cnt;i++){
            for(Item item:items){
                answer.add(item);
            }
        }
        return answer;
    }

    @Override
    public ArrayList<Item> getOutPutItems() {
        return multipleItems(outputs,level+1);
    }

    public ArrayList<Item> getOutPutItemsByLevel(int level) {
        return multipleItems(outputs,level+1);
    }

    @Override
    public ArrayList<Item> getInPutItems() {
        return multipleItems(inputs,level+1);
    }
    public ArrayList<Item> getInPutItemsByLevel(int level) {
        return multipleItems(inputs,level+1);
    }
    public int maxLevelCanDoWithItems(ArrayList<Item> items){
        ArrayList<Item> copyOfItems=new ArrayList<>();
        copyOfItems.addAll(items);
        int level=-1;
        while(level<this.level){
            boolean flag=true;
            for(Item item:inputs){
                flag&=copyOfItems.remove(item);
            }
            if(!flag)break;
            level++;
        }
        return level;
    }

    @Override
    public void endProduction() {
        busy=false;
    }
}
