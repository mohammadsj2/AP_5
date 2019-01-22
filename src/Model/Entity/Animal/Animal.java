package Model.Entity.Animal;

import Model.Entity.Animal.Pet.Chicken;
import Model.Entity.Animal.Pet.Cow;
import Model.Entity.Animal.Pet.Sheep;
import Model.Entity.Animal.Wild.Bear;
import Model.Entity.Animal.Wild.Lion;
import Model.Entity.Entity;
import Model.Loadable;
import Model.Map.Map;
import Model.Upgradable;
import Model.Map.Cell;
import Constant.Constant;
import Controller.*;
import Exception.CantUpgradeException;
import Exception.CellDoesNotExistException;
import View.Scene.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Animal extends Entity implements Upgradable, Loadable {
    private int level;
    private int speed = Constant.ANIMAL_SPEED;
    private int step=0;
    private Cell currentCell;
    String direction=null;


    protected Animal(Cell cell) {
        super(cell);
    }

    protected Animal(Cell cell, int level) {
        super(cell);
        setLevel(level);
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Cell walkTowards(Cell cur) {
        return InputReader.getCurrentController().getMap().getBestCellBySpeed(this.getCell(), cur, this.getSpeed());
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


    public void walk() throws CellDoesNotExistException {
        Map map=InputReader.getCurrentController().getMap();
        if(currentCell==null || step==0 || getCell()==currentCell)
        {
            currentCell = map.getRandomCell(getCell(), getSpeed());
        }
        this.changeCell(map.getBestCellBySpeed(getCell(),currentCell,Constant.ANIMAL_SPEED));
    }


    public void changeCell(Cell cur) {
        Map map=getMap();

        map.destroyEntity(getCell(),this);
        setWalkAnimation(getCell(),cur);
        setCell(cur);
        map.addEntity(cur,this);
    }

    protected void setWalkAnimation(Cell startCell, Cell targetCell)
    {
        int stableDistance=1;
        String directionName,animalName;
        int count=24,rows=0,columns=0;
        boolean flipImage=false;
        if(Math.abs(targetCell.getPositionY()-startCell.getPositionY())<stableDistance)
        {
            if(targetCell.getPositionX()>startCell.getPositionX())
            {
                directionName = "right";
                flipImage=true;
            }
            else
                directionName="left";
        }
        else if(targetCell.getPositionY()>startCell.getPositionY())
        {
            if (Math.abs(targetCell.getPositionX() - startCell.getPositionX()) < stableDistance)
                directionName = "down";
            else if (targetCell.getPositionX() > startCell.getPositionX())
            {
                directionName = "down_right";
                flipImage=true;
            }
            else
                directionName = "down_left";
        }
        else
        {
            if (Math.abs(targetCell.getPositionX() - startCell.getPositionX()) < stableDistance)
                directionName = "up";
            else if (targetCell.getPositionX() > startCell.getPositionX())
            {
                directionName = "up_right";//"up_left";
                flipImage=true;
            }
            else
                directionName = "up_left";//"up_left";
        }
        if(this instanceof Cat)
        {
            if(directionName.equals("left") || directionName.equals("right"))
            {
                rows=6;columns=4;
            }
            else
            {
                rows=4;columns=6;
            }
            animalName = "Cat";
        }
        else if(this instanceof Dog)
        {
            if(directionName.equals("up_left") || directionName.equals("down_left")
             || directionName.equals("up_right") || directionName.equals("down_right"))
            {
                rows=5;columns=5;
            }
            else
            {
                rows=4;columns=6;
            }
            animalName = "Dog";
        }
        else if(this instanceof Bear)
        {
            rows=6;columns=4;
            animalName = "Grizzly";
        }
        else if(this instanceof Lion)
        {
            if(directionName.equals("up_left") || directionName.equals("up") || directionName.equals("up_right"))
            {
                rows=4;columns=6;
            }
            else if(directionName.equals("left") || directionName.equals("right"))
            {
                rows=8;columns=3;
            }
            else if(directionName.equals("down_left") || directionName.equals("down_right"))
            {
                rows=6;columns=4;
            }
            else
            {
                rows=5;columns=5;
            }
            animalName = "Lion";
        }
        else if(this instanceof Chicken)
        {
            animalName = "GuineaFowl";
            rows=5;columns=5;
        }
        else if(this instanceof Sheep)
        {
            if(directionName.equals("down") || directionName.equals("up")
                    || directionName.equals("up_left") || directionName.equals("up_right"))
            {
                rows=5;columns=5;
            }
            else
            {
                rows=6;columns=4;
            }
            animalName = "Sheep";
        }
        else
        {
            if(directionName.equals("up"))
            {
                rows=6;columns=4;
            }
            else
            {
                rows=8;columns=3;
            }
            animalName = "Cow";
        }
        Image image=null;
        try
        {
            image=new Image(new FileInputStream("./Textures/Animals/"+animalName+"/"+directionName+".png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        System.out.println(directionName);
        if(!directionName.equals(direction))
        {
            changeImageView(image, count, rows, columns,
                    GameScene.modifiedX(getCell().getPositionX()), GameScene.modifiedY(getCell().getPositionY()));
            direction=directionName;
        }
    }

    public void nextTurn() throws CellDoesNotExistException {
        this.walk();
        step++;
        step%=15;
    }

    @Override
    public int upgradeCost(){
        return 0;
    }

    @Override
    public void upgrade() throws CantUpgradeException {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
