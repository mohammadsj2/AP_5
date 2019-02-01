package Model.Map;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;
import Exception.CellDoesNotExistException;
import View.Scene.GameScene;
import com.gilecode.yagson.YaGson;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import Exception.NoWarehouseSpaceException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Map
{
    public static final int MAX_DISTANCE_2 = 100 * 100 * 100;
    private ArrayList<Cell> cells = new ArrayList<>();

    public Map()
    {
        for (int i = 0; i < Constant.MAP_ROWS; i++)
        {
            for (int j = 0; j < Constant.MAP_COLUMNS; j++)
            {
                cells.add(new Cell(i, j));
            }
        }
    }

    public String printInfo()
    {
        StringBuilder stringBuilder = new StringBuilder("");
        YaGson yaGson = new YaGson();
        for (Cell cell : cells)
        {
            if (cell.getEntities().size() == 0 && !cell.haveGrass()) continue;
            stringBuilder.append(yaGson.toJson(cell));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void nextTurn()
    {
        ArrayList<Animal> animals = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();
        for (Cell cell : cells)
        {
            animals.addAll(cell.getAnimals());
        }
        Collections.shuffle(animals);
        //bekhatere in ke ye dog nare donbale wild bad wild nobatesh beshe bere ye ja dg
        for (Animal animal : animals)
            if (animal.getAlive())
            {
                try
                {
                    animal.nextTurn();
                } catch (CellDoesNotExistException cellDoesNotExistException)
                {
                    cellDoesNotExistException.printStackTrace();
                }
            } else
            {
                try
                {
                    animal.destroy();
                } catch (CellDoesNotExistException e)
                {
                    e.printStackTrace();
                }
            }

        for (Cell cell : cells)
        {
            items.addAll(cell.getItems());
        }
        for (Item item : items)
        {
            if (item.isExpired())
            {
                item.expire();
            }
        }
    }

    public void destroyGrass(Cell cell)
    {
        cell.destroyGrass();
    }

    public Cell getCell(int x, int y) throws CellDoesNotExistException
    {
        for (Cell cell : cells)
        {
            if (cell.getPositionX() == x && cell.getPositionY() == y)
            {
                return cell;
            }
        }
        throw new CellDoesNotExistException();
    }

    public boolean plant(int x, int y) throws CellDoesNotExistException
    {
        Cell cell = getCell(x, y);
        if (cell.haveGrass()) return false;
        cell.plantGrass();
        return true;

    }

    public void refreshView()
    {
        for (Cell cell : cells)
        {
            //      cell.refreshView();
        }
    }

    public void initView()
    {
        for (Cell cell : cells)
        {
            cell.initView();
        }
    }

    private int distance2(Cell cell1, Cell cell2)
    {
        int x = (cell1.getPositionX() - cell2.getPositionX());
        int y = (cell1.getPositionY() - cell2.getPositionY());
        return x * x + y * y;
    }

    private Cell nearestCell(Cell cell, ArrayList<Cell> cells)
    {
        int distance2 = MAX_DISTANCE_2;
        Cell nearestCell = null;
        for (Cell cell2 : cells)
        {
            if (distance2 > distance2(cell, cell2))
            {
                nearestCell = cell2;
                distance2 = distance2(cell, cell2);
            }
        }
        return nearestCell;
    }

    public Cell nearestCellWithGrass(Cell cell)
    {
        ArrayList<Cell> cellsWithGrass = new ArrayList<>();
        for (Cell cell2 : cells)
        {
            if (cell2.haveGrass())
                cellsWithGrass.add(cell2);
        }
        return nearestCell(cell, cellsWithGrass);
    }

    public Cell nearestCellWithItem(Cell cell)
    {
        ArrayList<Cell> cellsWithItem = new ArrayList<>();
        for (Cell cell2 : cells)
        {
            if (cell2.haveItem())
                cellsWithItem.add(cell2);
        }
        return nearestCell(cell, cellsWithItem);
    }

    public Cell nearestCellWithWild(Cell cell)
    {
        ArrayList<Cell> cellsWithWild = new ArrayList<>();
        for (Cell cell2 : cells)
        {
            if (cell2.getWilds().size() != 0)
                cellsWithWild.add(cell2);
        }
        return nearestCell(cell, cellsWithWild);
    }

    public ArrayList<Item> getItems(int x, int y) throws CellDoesNotExistException
    {
        Cell cell = getCell(x, y);
        return cell.getItems();
    }

    public void destroyEntity(int x, int y, Entity entity) throws CellDoesNotExistException
    {
        Cell cell = getCell(x, y);
        destroyEntity(cell, entity);
    }

    public void destroyEntity(Cell cell, Entity entity)
    {
        cell.destroyEntity(entity);
        entity.getImageView().setVisible(false);
        GameScene.deleteNode(entity.getImageView());
        entity.getImageView().setVisible(true);
    }

    public void addEntity(Cell cell, Entity entity)
    {
        GameScene.setImageViewPositionOnMap(entity.getImageView(), cell.getPositionX(), cell.getPositionY());
        GameScene.addNode(entity.getImageView());
        cell.addEntity(entity);
    }

    public void destroyWalkAnimal(Cell cell, Animal animal)
    {
        cell.destroyEntity(animal);
    }

    public void addWalkAnimal(Cell cell, Animal animal)
    {
        cell.addEntity(animal);
    }


    private void wildsToItems(ArrayList<Wild> wilds)
    {
        for (Wild wild : wilds)
        {
            ImageView cagedWildImageView = null;
            ImageView cageImageView = null;
            int imageHeight = 0, imageWidth = 0;
            String itemName = null;
            String type = wild.getName();
            if (type.equals("lion"))
            {
                type = "Lion";
                itemName = "cagedlion";
                imageWidth = 132;
                imageHeight = 142;
            } else
            {
                type = "Grizzly";
                itemName = "cagedbrownbear";
                imageWidth = 142;
                imageHeight = 126;
            }

            try
            {
                cagedWildImageView = new ImageView(new Image(new FileInputStream(
                        "./Textures/Animals/" + type + "/caged.png")));
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            cagedWildImageView.setViewport(new Rectangle2D(0, 0, imageWidth, imageHeight));
            cagedWildImageView.relocate(GameScene.modifiedX(wild.getCell().getPositionX()) - 50,
                    GameScene.modifiedY(wild.getCell().getPositionY()) - 50);
            GameScene.addNode(cagedWildImageView);
            String finalItemName = itemName;

            try
            {
                cageImageView = new ImageView(new Image(new FileInputStream(
                        "./Textures/Cages/build04.png")));
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            cageImageView.setViewport(new Rectangle2D(260, 260, 260, 260));
            cageImageView.relocate(GameScene.modifiedX(wild.getCell().getPositionX()) - 100,
                    GameScene.modifiedY(wild.getCell().getPositionY()) - 100);
            GameScene.addNode(cageImageView);
            ImageView finalCagedWildImageView = cagedWildImageView;
            ImageView finalCageImageView = cageImageView;
            cagedWildImageView.setOnMouseClicked(event ->
            {
                Item item = Constant.getItemByType(finalItemName);
                item.initView();
                try
                {
                    InputReader.getCurrentController().getWareHouse().addItem(item);
                    GameScene.deleteNode(finalCagedWildImageView);
                    GameScene.deleteNode(finalCageImageView);
                } catch (NoWarehouseSpaceException e)
                {
                    e.printStackTrace();
                }

            });
            cageImageView.setOnMouseClicked(event ->
            {
                Item item = Constant.getItemByType(finalItemName);
                item.initView();
                try
                {
                    InputReader.getCurrentController().getWareHouse().addItem(item);
                    GameScene.deleteNode(finalCagedWildImageView);
                    GameScene.deleteNode(finalCageImageView);
                } catch (NoWarehouseSpaceException e)
                {
                    e.printStackTrace();
                }
            });
            new AnimationTimer()
            {

                private long lastTime = 0;

                @Override
                public void handle(long now)
                {
                    if (lastTime == 0)
                        lastTime = now;
                    if(now-lastTime>10000000000l)
                    {
                        GameScene.deleteNode(finalCagedWildImageView);
                        GameScene.deleteNode(finalCageImageView);
                        this.stop();
                    }
                }
            }.start();
            try
            {
                wild.destroy();
            } catch (CellDoesNotExistException e)
            {
                e.printStackTrace();
            }

            //   Item item = wild.toItem();
            //  item.getCell().addEntity(item);
        }
    }

    public void cage(int x, int y) throws CellDoesNotExistException
    {
        Cell cell = getCell(x, y);
        wildsToItems(cell.getWilds());
    }

    public ArrayList<Cell> getNearbyCells(Cell first, int speed)
    {
        ArrayList<Cell> inRange = new ArrayList<>();
        for (int i = 0; i < cells.size(); ++i)
            if (Math.abs(cells.get(i).getPositionY() + cells.get(i).getPositionX() -
                    first.getPositionX() - first.getPositionY()) <= speed)
                inRange.add(cells.get(i));
        return inRange;
    }

    public Cell getRandomCell(Cell first, int speed)
    {
        Random random = new Random();
        int t = random.nextInt((int) cells.size());
        return cells.get(t);
    }

    public Cell getRandomCellWithItem()
    {
        ArrayList<Cell> itemCells = new ArrayList<>();
        for (Cell cell : cells)
        {
            if (cell.getItems().size() > 0)
            {
                itemCells.add(cell);
            }
        }
        Random random = new Random();
        int t = random.nextInt((int) itemCells.size());
        return itemCells.get(t);
    }

   /* public Cell getRandomCell(Cell first, int speed){
        Random random=new Random();
        ArrayList<Cell> inRange = getNearbyCells(first, speed);
        int t= random.nextInt((int) inRange.size());
        return inRange.get(t);
    }*/

    public Cell getRandomCell()
    {
        Random random = new Random();
        int t = random.nextInt((int) cells.size());
        return cells.get(t);
    }

    public Cell getBestCellBySpeed(Cell first, Cell last, int speed)
    {

        speed *= speed;
        if (distance2(first, last) <= speed)
            return last;
        int mini = distance2(first, last);
        Cell answer = first;
        for (Cell cell : cells)
        {
            if (distance2(first, cell) > speed) continue;
            int tmp = distance2(first, cell) + distance2(cell, last);
            if (tmp < mini)
            {
                mini = tmp;
                answer = cell;
            }
        }
        return answer;
    }

    public ArrayList<Entity> getEntities()
    {
        ArrayList<Entity> everything = new ArrayList<>();
        for (Cell cell : cells)
        {
            everything.addAll(cell.getEntities());
        }
        return everything;

    }
}
