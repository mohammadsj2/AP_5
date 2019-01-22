package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;
import Model.Entity.Item;
import View.Scene.GameScene;
import View.Scene.HelicopterScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Helicopter extends Transporter {
    ArrayList<Item> possibleItems=new ArrayList<>();

    public Helicopter(ArrayList<Item> possibleItems)
    {
        this.possibleItems=possibleItems;
        speed=Constant.HELICOPTER_SPEED;
        capacity=Constant.HELICOPTER_CAPACITY;
    }
    @Override
    Image getImageByLevel(){
        try {
            return new Image(new FileInputStream("./Textures/Service/Helicopter/0"+(level+1)+".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Item> getPossibleItems() {
        return possibleItems;
    }

    @Override
    public void initView() {
        super.initView();
        ImageView imageView=getImageView();
        Image image=getImageByLevel();
        GameScene.setMiddlePosition(imageView,image.getWidth(),image.getHeight(),620,600);
        imageView.setOnMouseClicked(event -> {
            GameScene.getNextTurnTimer().stop();
            InputReader.setScene(HelicopterScene.getScene());
        });
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.HELICOPTER_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        capacity+=Constant.HELICOPTER_CAPACITY_PER_LEVEL;
        speed+=Constant.HELICOPTER_SPEED_PER_LEVEL;
        refreshView();
    }

    @Override
    public int upgradeCost(){
        return (level+1)*Constant.HELICOPTER_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.HELICOPTER_MAX_LEVEL;
    }
}
