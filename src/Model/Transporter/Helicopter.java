package Model.Transporter;

import Constant.Constant;
import Controller.InputReader;
import Exception.CantUpgradeException;
import Model.Entity.Item;
import View.Scene.GameScene;
import View.Scene.HelicopterScene;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Helicopter extends Transporter {
    ArrayList<Item> possibleItems = new ArrayList<>();


    public Helicopter(ArrayList<Item> possibleItems) {
        this.possibleItems = possibleItems;
        speed = Constant.HELICOPTER_SPEED;
        capacity = Constant.HELICOPTER_CAPACITY;
    }

    @Override
    Image getImageByLevel() {
        try {
            return new Image(new FileInputStream("./Textures/Service/Helicopter/0" + (level + 1) + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    void refreshLittleImageView() {
        if (!busy) {
            littleImageView.setImage(null);
            return;
        }
        try {
            changeLittleImageView(new Image(new FileInputStream("Textures/UI/Helicopter/0" + (level + 1) + "_mini.png")),
                    6, 2, 3, 710, 10);
            goAndBackLittleImageAnimation();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Item> getPossibleItems() {
        return possibleItems;
    }

    @Override
    public void initView() {
        super.initView();
        ImageView imageView = getImageView();
        Image image = getImageByLevel();
        GameScene.setMiddlePosition(imageView, image.getWidth(), image.getHeight(), 620, 600);
        imageView.setOnMouseClicked(event -> {
            if(InputReader.getCurrentController().isGameFinished())return;
            GameScene.getNextTurnTimer().stop();
            InputReader.setScene(HelicopterScene.getScene());
        });

        HelicopterScene.addNode(valueLabel);
        valueLabel.relocate(158,540);

        refreshView();
    }

    @Override
    public void refreshView() {
        super.refreshView();

    }

    @Override
    public void upgrade() throws CantUpgradeException {
        if (busy || level >= Constant.HELICOPTER_MAX_LEVEL) {
            throw new CantUpgradeException();
        }
        level++;
        capacity += Constant.HELICOPTER_CAPACITY_PER_LEVEL;
        speed += Constant.HELICOPTER_SPEED_PER_LEVEL;
        refreshView();
    }

    @Override
    public int upgradeCost() {
        return (level + 1) * Constant.HELICOPTER_UPGRADE_COST_PER_LEVEL;
    }

    @Override
    public boolean canUpgrade() {
        return level < Constant.HELICOPTER_MAX_LEVEL;
    }
}
