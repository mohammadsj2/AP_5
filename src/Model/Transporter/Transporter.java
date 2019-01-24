package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.*;
import Model.Entity.Entity;
import Model.Entity.Item;
import Model.Upgradable;
import Model.Viewable;
import View.Scene.GameScene;
import View.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class Transporter implements Upgradable, Viewable {
    int level,capacity,speed,startTime;
    ArrayList<Item> items=new ArrayList<>();
    boolean busy=false;

    ImageView imageView,littleImageView;

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
        initLittleImageView();
        refreshView();
    }

    public void initLittleImageView() {
        littleImageView=new ImageView();
        littleImageView.setImage(null);
        GameScene.addNode(littleImageView);
    }

    abstract void refreshLittleImageView();

    void changeLittleImageView(Image image, int count, int rows, int columns, double x, double y){
        ImageView imageView = littleImageView;
        imageView.setImage(image);
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
        GameScene.setMiddlePosition(imageView, imageWidth / 4.0
                , imageHeight / 4.0, x, y);
        imageView.setViewport(new Rectangle2D(0, 0, imageWidth / columns, imageHeight / rows));
        Animation animation = new SpriteAnimation(
                imageView,
                Duration.millis(700),
                count, columns,
                0, 0,
                imageWidth / columns, imageHeight / rows
        );
        setAnimation(animation);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }
    abstract Image getImageByLevel();
    public void refreshView() {
        Image image=getImageByLevel();
        if(busy){
            image=null;
        }
        getImageView().setImage(image);
        refreshLittleImageView();
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
        return (startTime+ getDurationOfTrip() <= InputReader.getCurrentController().getTurn());
    }
    void goAndBackLittleImageAnimation() {
        littleImageView.setScaleX(-1);
        KeyValue keyValue=new KeyValue(littleImageView.xProperty(),840);
        KeyFrame keyFrame=new KeyFrame(Duration.seconds((double)getDurationOfTrip()* Constant.NEXT_TURN_DURATION/2.1e9)
                ,keyValue);
        Timeline timeline=new Timeline(keyFrame);
        timeline.play();
        timeline.setOnFinished(event -> {
            littleImageView.setScaleX(1);
            KeyValue keyValue1 =new KeyValue(littleImageView.xProperty(),710);
            KeyFrame keyFrame1 =new KeyFrame(Duration.seconds((double)getDurationOfTrip()*Constant.NEXT_TURN_DURATION/2.1e9)
                    , keyValue1);
            Timeline timeline1 =new Timeline(keyFrame1);
            timeline1.play();
        });
    }

    public int getDurationOfTrip() {
        return Constant.TOWN_DISTANCE/speed;
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
