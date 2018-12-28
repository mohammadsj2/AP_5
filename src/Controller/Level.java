package Controller;

import Model.Entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    int goalMoney;
    private ArrayList<Entity> earnedEnitities,goalEntities=new ArrayList<>();

    Level(int goalMoney,ArrayList<Entity> earnedEnitities){
        this.goalMoney=goalMoney;
        this.earnedEnitities=earnedEnitities;
    }

    void entityEarned(Entity entity){
        if(!goalEntities.contains(entity))return;
        if(!earnedEnitities.contains(entity))
            earnedEnitities.add(entity);
    }
    public boolean checkLevel(){
        int earnedMoney= InputReader.getCurrentController().getMoney();
        if(earnedMoney<goalMoney){
            return false;
        }
        return (earnedEnitities.size()==goalEntities.size());
    }

    public ArrayList<Entity> getGoalEntities() {
        return goalEntities;
    }
}
