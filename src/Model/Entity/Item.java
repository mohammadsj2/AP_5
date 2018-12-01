package Model.Entity;

public class Item extends Entity {
    String name;
    int volume,cost;

    Item(String name,int volume,int cost){
        this.name=name;
        this.volume=volume;
        this.cost=cost;
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
}
