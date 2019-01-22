package Model;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import Exception.CantUpgradeException;
import Exception.NoSuchItemInWarehouseException;
import Exception.NoWarehouseSpaceException;
import View.Scene.GameScene;
import View.Scene.MenuScene;
import View.Scene.TruckScene;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class WareHouse implements Upgradable,Viewable{
    private ArrayList<Item> items=new ArrayList<>();
    private int level=0;
    private int capacity=Constant.WAREHOUSE_CAPACITY;
    private ImageView imageView;

    public void refreshView(){
        try {
            Image image=new Image(new FileInputStream("Textures/Service/Depot/0"+(level+1)+".png"));
            imageView.setImage(image);
            GameScene.setMiddlePosition(imageView,image.getWidth(),image.getHeight(),430,610);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    public void initView(){
        imageView=new ImageView();
        GameScene.addNode(imageView);
        refreshView();
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                GameScene.getNextTurnTimer().stop();
                MenuScene.init(true);
                InputReader.setScene(TruckScene.getScene());
            }
        });
    }
    private int placeTaken()
    {
        int sum=0;
        for(Item item:items)sum+=item.getVolume();
        return sum;
    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    public void addItem(Item item) throws NoWarehouseSpaceException {
        if(placeTaken()+item.getVolume()>capacity)
            throw new NoWarehouseSpaceException();
        item.setInWareHouse(true);
        items.add(item);
    }

    public void eraseItem(Item item) throws NoSuchItemInWarehouseException {
        for(Item item2:items)
        {
            if(item.getName().equals(item2.getName()))
            {
                items.remove(item2);
                return ;
            }
        }
        throw new NoSuchItemInWarehouseException();
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.WAREHOUSE_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        capacity+=Constant.WAREHOUSE_CAPACITY_PER_LEVEL;
        refreshView();
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.WAREHOUSE_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.WAREHOUSE_MAX_LEVEL;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
