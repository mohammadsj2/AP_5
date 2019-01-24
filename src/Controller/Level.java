package Controller;

import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    private int goalMoney;
    private HashMap<String,Integer> goalEntities;

    Level(int goalMoney,HashMap<String,Integer> goalEntities){
        this.goalMoney=goalMoney;
        this.goalEntities=goalEntities;
    }

    public boolean checkLevel(){
        int earnedMoney= InputReader.getCurrentController().getMoney();
        if(earnedMoney<goalMoney){
            return false;
        }
        HashMap<String,Integer> earnedEntities=new HashMap<>();
        for(Item item:InputReader.getCurrentController().getWareHouse().getItems())
        {
            earnedEntities.put(item.getName(),earnedEntities.get(item.getName())+1);
        }
        for(Entity entity:InputReader.getCurrentController().getMap().getEntities())
        {
            earnedEntities.put(entity.getName(),earnedEntities.get(entity.getName())+1);
        }
        for(String name:goalEntities.keySet())
        {
            if(!earnedEntities.containsKey(name) || earnedEntities.get(name)<goalEntities.get(name))
            {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Integer> getGoalEntities() {
        return goalEntities;
    }
}
