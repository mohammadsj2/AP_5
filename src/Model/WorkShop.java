package Model;

import Controller.*;
import Model.Entity.Item;
import java.util.ArrayList;
import Exception.*;
import Constant.Constant;

public class WorkShop implements Producer,Upgradable{
    private ArrayList<Item> inputs,outputs;
    private int location,level,startTime,produceDuration;
    private String name;
    private boolean busy,isCustom;
    private int usedLevel=-10;

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
        return (level== Constant.MAX_WORKSHOP_LEVEL || isCustom );
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
        return (busy && InputReader.getCurrentController().getTurn()>=startTime+getProduceDuration());
    }

    @Override
    public void startProduction() throws StartBusyProducerException {
        if(busy){
            throw new StartBusyProducerException();
        }
        busy=true;
        startTime=InputReader.getCurrentController().getTurn();
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
    public ArrayList<Item> getOutputItems() {
        return multipleItems(outputs,level+1);
    }

    public ArrayList<Item> getOutputItemsByUsedLevel() throws WorkShopNotUsedException {
        if(usedLevel==-10)throw new WorkShopNotUsedException();
        return multipleItems(outputs,usedLevel+1);
    }

    @Override
    public ArrayList<Item> getInputItems() {
        return multipleItems(inputs,level+1);
    }
    public ArrayList<Item> getInputItemsByUsedLevel() throws WorkShopNotUsedException {
        if(usedLevel==-10)throw new WorkShopNotUsedException();
        return multipleItems(inputs,usedLevel+1);
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
        usedLevel=-10;
    }

    public void startByLevel(int usedLevel) throws StartBusyProducerException {
        if(busy){
            throw new StartBusyProducerException();
        }
        busy=true;
        startTime=InputReader.getCurrentController().getTurn();
        this.usedLevel=usedLevel;
    }
}
