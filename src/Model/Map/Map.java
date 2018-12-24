package Model.Map;

import Constant.Constant;
import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;
import Exception.CellDoesNotExist;

import java.util.ArrayList;
import java.util.Collections;

public class Map {
    public static final int MAX_DISTANCE_2 = 100 * 100 * 100;
    ArrayList<Cell> cells = new ArrayList<>();

    public Map(){
        for(int i = 0; i< Constant.MAP_ROWS; i++){
            for(int j = 0; j< Constant.MAP_COLUMNS; j++){
                cells.add(new Cell(i,j));
            }
        }
    }
    public void nextTurn(){
        ArrayList<Animal> animals=new ArrayList<>();
        for(Cell cell:cells){
            animals.addAll(cell.getAnimals());
        }
        Collections.shuffle(animals);
        //bekhatere in ke ye dog nare donbale wild bad wild nobatesh beshe bere ye ja dg
        for(Animal animal:animals){
            try {
                animal.nextTurn();
            } catch (CellDoesNotExist cellDoesNotExist) {
                cellDoesNotExist.printStackTrace();
            }
        }
    }


    private Cell getCell(int x, int y) throws CellDoesNotExist {
        for (Cell cell : cells) {
            if (cell.getPositionX() == x && cell.getPositionY() == y) {
                return cell;
            }
        }
        throw new CellDoesNotExist();
    }

    public boolean plant(int x, int y) throws CellDoesNotExist{
        Cell cell = getCell(x, y);
        if(cell.haveGrass())return false;
        cell.plantGrass();
        return true;

    }

    public int distance2(Cell cell1, Cell cell2) {
        int x = (cell1.getPositionX() - cell2.getPositionX());
        int y = (cell1.getPositionY() - cell2.getPositionY());
        return x * x + y * y;
    }

    public Cell nearestCell(Cell cell, ArrayList<Cell> cells) {
        int distance2 = MAX_DISTANCE_2;
        Cell nearestCell = null;
        for (Cell cell2 : cells) {
            if (distance2 > distance2(cell, cell2)) {
                nearestCell = cell2;
                distance2 = distance2(cell, cell2);
            }
        }
        return nearestCell;
    }

    public Cell nearestCellWithGrass(Cell cell) {
        ArrayList<Cell> cellsWithGrass = new ArrayList<>();
        for (Cell cell2 : cells) {
            if (cell2.haveGrass())
                cellsWithGrass.add(cell2);
        }
        return nearestCell(cell, cellsWithGrass);
    }

    public Cell nearestCellWithItem(Cell cell) {
        ArrayList<Cell> cellsWithItem = new ArrayList<>();
        for (Cell cell2 : cells) {
            if (cell2.haveItem())
                cellsWithItem.add(cell2);
        }
        return nearestCell(cell, cellsWithItem);
    }

    public Cell nearestCellWithWild(Cell cell) {
        ArrayList<Cell> cellsWithWild = new ArrayList<>();
        for (Cell cell2 : cells) {
            if (cell2.getWilds().size() != 0)
                cellsWithWild.add(cell2);
        }
        return nearestCell(cell, cellsWithWild);
    }

    public ArrayList<Item> getItems(int x, int y) throws CellDoesNotExist{
        Cell cell = getCell(x, y);
        return cell.getItems();
    }

    public void destroyEntity(int x, int y, Entity entity) throws CellDoesNotExist{
        Cell cell = getCell(x, y);
        cell.destroyEntity(entity);
    }

    private void wildsToItems(ArrayList<Wild> wilds) {
        for (Wild wild : wilds) {
            wild.toItem();
        }
    }

    public void cage(int x, int y) throws CellDoesNotExist{
        Cell cell = getCell(x, y);
        wildsToItems(cell.getWilds());
    }
    public Cell getRandomCell(){
        int t=(int)(Math.random()*2.0*cells.size());
        return cells.get(t%cells.size());
    }
    public Cell getBestCellBySpeed(Cell first,Cell last,int speed){
        speed*=speed;
        if(distance2(first,last)<=speed)
            return last;
        int mini=distance2(first,last);
        Cell answer=first;
        for(Cell cell:cells){
            if(distance2(first,cell)>speed)continue;
            int tmp=distance2(first,cell)+distance2(cell,last);
            if(tmp<mini){
                mini=tmp;
                answer=cell;
            }
        }
        return answer;
    }

}
