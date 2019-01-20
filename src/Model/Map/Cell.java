package Model.Map;

import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Viewable;
import View.GameScene.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Cell implements Viewable {
    private ArrayList<Entity> entities=new ArrayList<>();
    private boolean grass=false;
    private int positionX,positionY;
    private ImageView imageView = new ImageView();
    @Override
    public ImageView getImageView() { return this.imageView; }

    void initView() {
        imageView = new ImageView();
        GameScene.addNode(getImageView());
    }

    Cell(int x,int y) {
        initView();
        positionX = x;
        positionY = y;
    }

    void plantGrass(){
        Image image = null;
        try {
            image = new Image(new FileInputStream("./Textures/Grass/grass1.png"));
            changeImageView(image, 16, 4, 4, (230 + 3.7 * getPositionX()), (230 + 2.1 * getPositionY()));
            grass = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    void destroyGrass(){
        getImageView().setImage(null);
        grass = false;
    }

    public boolean haveGrass() {
        return grass;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    void destroyEntity(Entity entity){
        entities.remove(entity);
    }
    void addEntity(Entity entity){
        entities.add(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    boolean haveItem(){
        for(Entity entity:entities){
            if(entity instanceof Item){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Item> getItems(){
        ArrayList<Item> items=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Item){
                items.add((Item)entity);
            }
        }
        return items;
    }
    public ArrayList<Wild> getWilds(){
        ArrayList<Wild> wilds=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Wild){
                wilds.add((Wild)entity);
            }
        }
        return wilds;
    }
    ArrayList<Animal> getAnimals(){
        ArrayList<Animal> animals=new ArrayList<>();
        for(Entity entity:entities){
            if(entity instanceof Animal){
                animals.add((Animal)entity);
            }
        }
        return animals;
    }



}
