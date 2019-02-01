package Model.Map;

import Model.Entity.Animal.Animal;
import Model.Entity.Animal.Wild.Wild;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Viewable;
import View.Scene.GameScene;
import javafx.animation.Animation;
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

    public void initView() {
        imageView = new ImageView();
        if(grass){
            showGrass();
        }
        GameScene.addNode(getImageView());
    }

    @Override
    public void refreshView()
    {

    }

    @Override
    public Animation getAnimation()
    {
        return null;
    }

    @Override
    public void setAnimation(Animation animation)
    {

    }

    public Cell(int x, int y) {
        positionX = x;
        positionY = y;
    }

    void plantGrass(){
        showGrass();
        grass = true;
    }
    /*
    public void changeImageViewOnce(Image image, int count, int rows, int columns, double x, double y) {
        ImageView imageView = getImageView();
        imageView.setImage(image);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        GameScene.setMiddlePosition(imageView, imageWidth / columns
                , imageHeight / rows, x, y);
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(700),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        setAnimation(animation);
        animation.setCycleCount(1);
        animation.play();
    }
    */
    private void showGrass(){
        Image image;
        try {
            image = new Image(new FileInputStream("./Textures/Grass/grass1.png"));
            changeImageViewOnce(image, 16, 4, 4, GameScene.modifiedX(positionX), GameScene.modifiedY(positionY));
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

    public void destroyEntity(Entity entity){
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cell)){
            return false;
        }
        Cell cell=(Cell)obj;
        return (cell.getPositionX()==getPositionX() && cell.getPositionY()==getPositionY());
    }
}
