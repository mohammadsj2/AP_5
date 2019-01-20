package Model.Entity;

import Constant.Constant;
import Controller.*;
import Model.Map.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Item extends Entity {
    private String name;
    private int volume,cost,creatingTurn;
    private boolean isInWareHouse=false;



    public Item(String name,int volume,int cost,int creatingTurn){
        this.name=name;
        this.volume=volume;
        this.cost=cost;
        this.creatingTurn=creatingTurn;
        try {
            okImageView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Item(String name,int volume,int cost,int creatingTurn, Cell cell){
        super(cell);
        this.name=name;
        this.volume=volume;
        this.cost=cost;
        this.creatingTurn=creatingTurn;
        try {
            okImageView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void okImageView() throws FileNotFoundException {
        ImageView imageView=getImageView();
        Image image;
        if(name.equals("egg")){
            image=new Image(new FileInputStream("./Textures/Products/Egg/normal_2.png"));
        }else if(name.equals("horn")){
            image=new Image(new FileInputStream("./Textures/Products/Horn/normal_5.png"));
        }else if(name.equals("wool")){
            image=new Image(new FileInputStream("./Textures/Products/Wool/normal.png"));
        }else if(name.equals("plume")){
            image=new Image(new FileInputStream("./Textures/Products/Plume/normal.png"));
        }else{
            image=new Image(new FileInputStream("./Textures/Products/"+name+".png"));
        }
        imageView.setImage(image);

    }


    public void setCreatingTurn(int creatingTurn) {
        this.creatingTurn = creatingTurn;
    }

    public boolean isInWareHouse() {
        return isInWareHouse;
    }

    public void setInWareHouse(boolean inWareHouse) {
        isInWareHouse = inWareHouse;
    }
    public boolean isExpired(){
        if(isInWareHouse)return false;
        return (creatingTurn+ Constant.TERM_OF_DESTROY_ITEM_IN_MAP <= InputReader.getCurrentController().getTurn());
    }
    public void expire(){
        getMap().destroyEntity(getCell(),this);
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item))return false;
        return ((Item) obj).getName().equals(getName());
    }
}
