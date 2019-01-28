package Model;

import Controller.*;
import Model.Entity.Item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Exception.*;
import Constant.Constant;
import View.Scene.GameScene;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class WorkShop implements Producer, Upgradable, Viewable
{
    private ArrayList<Item> inputs, outputs;
    private int location, level, startTime, produceDuration;
    private String name;
    private boolean busy, isCustom;
    private int usedLevel = -10;
    private ImageView imageView;
    private Animation animation;

    public WorkShop(String name, int location, boolean isCustom, ArrayList<Item> inputs
            , ArrayList<Item> outputs, int produceDuration)
    {
        this.name = name;
        this.location = location;
        this.isCustom = isCustom;
        this.inputs = inputs;
        this.outputs = outputs;
        this.produceDuration = produceDuration;
    }

    public void initView()
    {
        imageView = new ImageView();/*
        VBox vbox=new VBox();
        vbox.relocate(200,200);
        vbox.setMinWidth(100);
        vbox.setMinHeight(100);
        vbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;");
        for(Item item:inputs)
        {

        }
        imageView.setOnMouseEntered(event ->
        {
            GameScene.addNode(vbox);
        });
        imageView.setOnMouseExited(event ->
        {
            GameScene.deleteNode(vbox);
            System.err.println("FFFF");
        });*/
        imageView.setOnMouseClicked(event ->
        {
            if(InputReader.getCurrentController().isGameFinished())return;
            InputReader.startWorkshop(location);
            if (this.busy)
                startAnimation();

        });
        GameScene.addNode(imageView);
        refreshView();
    }

    @Override
    public void refreshView()
    {
        String imageAddress = "Textures/Workshops/" + this.getName() +
                "/0" + (this.getLevel() + 1) + ".png";
        Image workshopImage = null;
        try
        {
            workshopImage = new Image(new FileInputStream(imageAddress));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        changeImageView(workshopImage, 16, 4, 4,
                Constant.WORKSHOPS_POSITION_X[location], Constant.WORKSHOPS_POSITION_Y[location]);
        //System.out.println(animation==null);
        stopAnimation(4, 4);
    }

    public void setLocation(int location)
    {
        this.location = location;
    }

    @Override
    public boolean canUpgrade()
    {
        return !(level == Constant.WORKSHOP_MAX_LEVEL || isCustom);
    }

    @Override
    public int upgradeCost()
    {
        return Constant.WORKSHOP_UPGRADE_COST_PER_LEVEL * (level + 1);
    }

    @Override
    public void upgrade() throws CantUpgradeException
    {
        if (!canUpgrade())
        {
            throw new CantUpgradeException();
        }
        level++;
        refreshView();
    }

    public int getLevel()
    {
        return level;
    }

    public int getProduceDuration()
    {
        return produceDuration;
    }

    @Override
    public boolean haveProduct()
    {
        return (busy && InputReader.getCurrentController().getTurn() >= startTime + getProduceDuration());
    }

    @Override
    public void startProduction() throws StartBusyProducerException
    {
        if (busy)
        {
            throw new StartBusyProducerException();
        }
        busy = true;
        startTime = InputReader.getCurrentController().getTurn();
    }

    private ArrayList<Item> multipleItems(ArrayList<Item> items, int cnt)
    {
        ArrayList<Item> answer = new ArrayList<>();
        for (int i = 0; i < cnt; i++)
        {
            for (Item item : items)
            {
                answer.add(Constant.getItemByType(item.getName()));
            }
        }
        return answer;
    }

    @Override
    public ArrayList<Item> getOutputItems()
    {
        return multipleItems(outputs, level + 1);
    }

    public ArrayList<Item> getOutputItemsByUsedLevel() throws WorkShopNotUsedException
    {
        if (!busy) throw new WorkShopNotUsedException();
        return multipleItems(outputs, usedLevel + 1);
    }

    @Override
    public ArrayList<Item> getInputItems()
    {
        return multipleItems(inputs, level + 1);
    }

    public ArrayList<Item> getInputItemsByUsedLevel() throws WorkShopNotUsedException
    {
        if (!busy) throw new WorkShopNotUsedException();
        return multipleItems(inputs, usedLevel + 1);
    }

    public int maxLevelCanDoWithItems(ArrayList<Item> items)
    {
        ArrayList<Item> copyOfItems = new ArrayList<>();
        copyOfItems.addAll(items);
        int level = -1;
        while (level < this.level)
        {
            boolean flag = true;
            for (Item item : inputs)
            {
                flag &= copyOfItems.remove(item);
            }
            if (!flag) break;
            level++;
        }
        return level;
    }

    @Override
    public void endProduction()
    {
        busy = false;
        usedLevel = -10;
        stopAnimation(4,4);
    }

    public void startByLevel(int usedLevel) throws StartBusyProducerException
    {
        if (busy)
        {
            throw new StartBusyProducerException();
        }
        busy = true;
        startTime = InputReader.getCurrentController().getTurn();
        this.usedLevel = usedLevel;
    }

    public ArrayList<Item> getInputs()
    {
        return inputs;
    }

    public ArrayList<Item> getOutputs()
    {
        return outputs;
    }

    public int getLocation()
    {
        return location;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public String getName()
    {
        return name;
    }

    public boolean isBusy()
    {
        return busy;
    }

    public boolean isCustom()
    {
        return isCustom;
    }

    public int getUsedLevel()
    {
        return usedLevel;
    }

    public ImageView getImageView()
    {
        return imageView;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
    }
}
