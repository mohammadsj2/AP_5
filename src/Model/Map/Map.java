package Model.Map;

import Model.Entity.Entity;
import Model.Entity.Item;

import java.util.ArrayList;

public class Map {
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
    public Cell nearestCellWithGrass(Cell cell){
        return null;
    }
    public Cell nearestCellWithItem(Cell cell){
        return null;
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
        return null;
    }
}
