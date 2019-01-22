package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;

import Model.Entity.Item;
import Model.Upgradable;
import View.Scene.GameScene;
import View.Scene.TruckScene;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Truck extends Transporter{

    public Truck()
    {
        speed=Constant.TRUCK_SPEED;
        capacity=Constant.TRUCK_CAPACITY;
    }

    @Override
    Image getImageByLevel(){
        try {
            return new Image(new FileInputStream("./Textures/Service/Truck/0"+(level+1)+".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initView() {
        super.initView();
        ImageView imageView=getImageView();
        Image image=getImageByLevel();
        GameScene.setMiddlePosition(imageView,image.getWidth(),image.getHeight(),240,620);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                InputReader.setScene(TruckScene.getScene());
            }
        });
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.TRUCK_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        refreshView();
        capacity+=Constant.TRUCK_CAPACITY_PER_LEVEL;
        speed+=Constant.TRUCK_SPEED_PER_LEVEL;
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.TRUCK_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.TRUCK_MAX_LEVEL;
    }
}
