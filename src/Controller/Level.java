package Controller;

import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    private int goalMoney;
    private ArrayList<String> goalEntities;

    Level(int goalMoney,ArrayList<String> goalEntities){
        this.goalMoney=goalMoney;
        this.goalEntities=goalEntities;
    }

    public boolean checkLevel(){
        int earnedMoney= InputReader.getCurrentController().getMoney();
        if(earnedMoney<goalMoney){
            return false;
        }
        ArrayList<String> goalEntitiesCopy = new ArrayList<>(goalEntities);
        for(Item item:InputReader.getCurrentController().getWareHouse().getItems())
        {
            if(goalEntitiesCopy.contains(item.getName()))
                goalEntitiesCopy.remove(item.getName());
        }
        for(Entity entity:InputReader.getCurrentController().getMap().getEntities())
        {
            if(goalEntitiesCopy.contains(entity.getName()))
                goalEntitiesCopy.remove(entity.getName());
        }
        return goalEntitiesCopy.size()==0;
    }

    public ArrayList<String> getGoalEntities() {
        return goalEntities;
    }
    public int getNumberOfThisItem(String name){
        int ans=0;
        for(String item:goalEntities){
            if(item.equals(name)){
                ans++;
            }
        }
        return ans;
    }

    public int getGoalMoney() {
        return goalMoney;
    }
}
