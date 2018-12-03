package Model.Map;

import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;

public class Map {
    public static final int MAX_DISTANCE_2 = 100 * 100 * 100;
    ArrayList<Cell> cells=new ArrayList<>();
    void nextTurn(){

    }
    private Cell getCell(int x,int y){
        for(Cell cell:cells){
            if(cell.getPositionX()==x && cell.getPositionY()==y){
                return cell;
            }
        }
        return null;
    }
    public void plant(int x,int y){
        Cell cell=getCell(x,y);
        cell.plantGrass();
    }
    public int distance2(Cell cell1,Cell cell2){
        int x=(cell1.getPositionX()-cell2.getPositionX());
        int y=(cell1.getPositionY()-cell2.getPositionY());
        return x*x+y*y;
    }
    public Cell nearestCellWithGrass(Cell cell){
        int distance2= MAX_DISTANCE_2;
        Cell nearestCell=null;
        for(Cell cell2:cells){
            if(!cell2.haveGrass())
                continue;
            if(distance2>distance2(cell,cell2)){
                nearestCell=cell2;
                distance2=distance2(cell,cell2);
            }
        }
        return nearestCell;
    }
    public Cell nearestCellWithItem(Cell cell){
        int distance2= MAX_DISTANCE_2;
        Cell nearestCell=null;
        for(Cell cell2:cells){
            if(!cell2.haveItem())
                continue;
            if(distance2>distance2(cell,cell2)){
                nearestCell=cell2;
                distance2=distance2(cell,cell2);
            }
        }
        return nearestCell;
    }
    public ArrayList<Item> getItems(int x,int y){
        Cell cell=getCell(x,y);
        return cell.getItems();
    }
    public void destroyEntity(int x, int y, Entity entity){
        Cell cell=getCell(x,y);
        cell.destroyEntity(entity);
    }
    public ArrayList<Item> cage(int x,int y){
        return null;
    }
    public Cell nearestCellWithWild(Cell cell){
        int distance2= MAX_DISTANCE_2;
        Cell nearestCell=null;
        for(Cell cell2:cells){
            if(cell2.getWilds().size()==0)
                continue;
            if(distance2>distance2(cell,cell2)){
                nearestCell=cell2;
                distance2=distance2(cell,cell2);
            }
        }
        return nearestCell;
    }
}
