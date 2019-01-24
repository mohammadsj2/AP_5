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
import View.ProgressBar.ProgressBar;
import View.Scene.GameScene;
import View.Scene.MenuScene;
import View.Scene.TruckScene;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WareHouse implements Upgradable,Viewable{
    private ArrayList<Item> items=new ArrayList<>();
    private int level=0;
    private int capacity=Constant.WAREHOUSE_CAPACITY;
    private ImageView imageView;
    private ProgressBar progressBar;

    public void refreshView(){
        progressBar.setPercentage((double)spaceTaken()/(double)capacity);
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
        progressBar=new ProgressBar(330,470);
        progressBar.getNode().setScaleY(1.5);

        GameScene.addNode(progressBar.getNode());
        GameScene.addNode(imageView);


        imageView.setOnMouseClicked(event -> {
            if(InputReader.getCurrentController().isGameFinished())return;
            GameScene.getNextTurnTimer().stop();
            MenuScene.init(true);
            InputReader.setScene(TruckScene.refreshAndGetScene());
        });
        refreshView();
    }
    private int spaceTaken()
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
        if(spaceTaken()+item.getVolume()>capacity)
            throw new NoWarehouseSpaceException();
        item.setInWareHouse(true);
        items.add(item);
        refreshView();
    }

    public void eraseItem(Item item) throws NoSuchItemInWarehouseException {
        for(Item item2:items)
        {
            if(item.getName().equals(item2.getName()))
            {
                items.remove(item2);
                refreshView();
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

    public int getNumberOfThisItem(Item item) {
        int ans =0;
        for(Item item1:items){
            if(item1.equals(item)){
                ans++;
            }
        }
        return ans;
    }
}
