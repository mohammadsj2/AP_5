package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.*;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Upgradable;
import Model.Viewable;
import View.Scene.GameScene;
import View.View;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class Transporter implements Upgradable, Viewable {
    protected int level,capacity,speed,startTime;
    protected ArrayList<Item> items=new ArrayList<>();
    protected boolean busy=false;
    ImageView imageView;

    Transporter()
    {

    }

    @Override
    public ImageView getImageView() {
        return imageView;
    }
    @Override
    public void initView() {
        imageView = new ImageView();
        GameScene.addNode(getImageView());
        refreshView();
    }
    abstract Image getImageByLevel();
    public void refreshView() {
        Image image=getImageByLevel();
        if(busy){
            image=null;
        }
        getImageView().setImage(image);
    }
    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public void setAnimation(Animation animation) {

    }

    @Override
    public void stopAnimation(int rows, int columns) {

    }

    @Override
    public void startAnimation() {

    }

    public void addItem(Item item) throws NoTransporterSpaceException {
        if(!canAddItem(item))
            throw new NoTransporterSpaceException();
        items.add(item);
    }
    public void startTransportation() throws StartBusyTransporter, StartEmptyTransporter {
        if(busy)
        {
            throw new StartBusyTransporter();
        }
        if(items.isEmpty()){
            throw new StartEmptyTransporter();
        }
        busy=true;
        refreshView();
        startTime=InputReader.getCurrentController().getTurn();
    }
    public boolean isTransportationEnds(){
        return (startTime+Constant.TOWN_DISTANCE/speed<= InputReader.getCurrentController().getTurn());
    }
    public void endTransportation(){
        busy=false;
        refreshView();
    }
    public void clear(){
        items.clear();
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }

    public int getFilledVolume()
    {
        int filledVolume=0;
        for(Item item:items)
        {
            filledVolume+=item.getVolume();
        }
        return filledVolume;
    }

    public int getValue() {
        int value=0;
        for(Item item:items)
        {
            value += item.getCost();
        }
        return value;
    }

    public boolean canAddItem(Item item){
        return (getFilledVolume()+item.getVolume()<=capacity);
    }
}
