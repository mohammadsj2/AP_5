package Controller;

import Model.Entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    int goalMoney,earnedMoney=0;
    ArrayList<Entity> earnedEnitities=new ArrayList<>(),goalEntities;

    Level(int goalMoney,ArrayList<Entity> earnedEnitities){
        this.goalMoney=goalMoney;
        this.earnedEnitities=earnedEnitities;
    }

    void entityEarned(Entity entity){
        if(!goalEntities.contains(entity))return;
        earnedEnitities.add(entity);
    }

    public void setEarnedMoney(int earnedMoney) {
        this.earnedMoney = earnedMoney;
    }
    public boolean checkLevel(){
        setEarnedMoney(InputReader.getCurrentController().getMoney());
        if(earnedMoney!=goalMoney){
            return false;
        }
        if(earnedEnitities.size()!=goalEntities.size()){
            return false;
        }
        Collections.sort((List)earnedEnitities);
        Collections.sort((List)goalEntities);
        for(int i=0;i<earnedEnitities.size();i++){
            if(earnedEnitities.get(i)!=goalEntities.get(i)){
                return false;
            }
        }
        return true;
    }

}
