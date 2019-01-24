package Model;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;
import Exception.NoWaterException;
import View.ProgressBar.BlueProgressBar;
import View.Scene.GameScene;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Well implements Upgradable, Loadable, Viewable {
    private int level=0;
    private int waterRemaining=Constant.WELL_BASE_WATER,maxWater=Constant.WELL_BASE_WATER;
    private ImageView imageView = new ImageView();
    private BlueProgressBar progressBar;
    @Override
    public ImageView getImageView() { return imageView; }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void refreshView() {
        progressBar.setPercentage((double)waterRemaining/(double)getMaxWater());
        try {
            Image image = new Image(new FileInputStream("./Textures/Service/Well/0" + (new Integer(getLevel() + 1)).toString() + ".png"));
            changeImageView(image, 1, 4, 4, 450, 125);
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

    public void initView() {
        setImageView(new ImageView());
        getImageView().setOnMouseClicked(event -> InputReader.fillWell());
        progressBar=new BlueProgressBar(520,115);
        GameScene.addNode(progressBar.getNode());
        GameScene.addNode(getImageView());
        refreshView();
    }
    public Well() {
        this(0);
    }
    public Well(int level) {

    }

    public void setLevel(int level) {
        this.level = level;
        refreshView();
    }

    private int fillCost()
    {
        return Constant.WELL_FILL_COST+level*Constant.WELL_FILL_COST_PER_LEVEL;
    }

    public void fill()
    {
        waterRemaining=maxWater;
        refreshView();
    }
    public void liftWater() throws NoWaterException
    {
        if(waterRemaining==0)throw new NoWaterException();
        waterRemaining--;
        refreshView();
    }

    @Override
    public int upgradeCost() {
        return (level + 1) * Constant.WELL_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level<Constant.WELL_MAX_LEVEL;
    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if(level>= Constant.WELL_MAX_LEVEL){
            throw new CantUpgradeException();
        }
        level++;
        maxWater+=Constant.WELL_WATER_PER_LEVEL;
        waterRemaining=maxWater;
        refreshView();
    }

    public int getWaterRemaining() {
        return waterRemaining;
    }
    public int getMaxWater() {
        return maxWater;
    }


    public int getLevel() {
        return level;
    }
}
