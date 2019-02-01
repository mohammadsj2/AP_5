package Model.Entity;

import Constant.Constant;
import Controller.InputReader;
import Network.Client.Client;
import View.Scene.GameScene;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;


public class Item extends Entity
{
    private String name;
    private int volume, buyCost,sellCost, creatingTurn;
    private boolean isInWareHouse = false;


    public Item(String name, int volume, int buyCost,int sellCost, int creatingTurn)
    {
        super();
        this.name = name;
        this.volume = volume;
        this.buyCost=buyCost;
        this.sellCost=sellCost;
        this.creatingTurn = creatingTurn;
  //      initView();
        refreshView();
    }

    @Override
    public void initView() {
        super.initView();
        if(getImageView()==null){
            setImageView(new ImageView());
        }
        getImageView().setOnMouseClicked(event -> {
            if(InputReader.getCurrentController().isGameFinished())return;
            if(getCell()==null)return;
            int x=getCell().getPositionX();
            int y=getCell().getPositionY();
            for(int i=Math.max(0,x-3);i<Math.min(x+3,100);i++){
                for(int j=Math.max(0,y-3);j<Math.min(y+3,100);j++){
                    InputReader.pickup(i,j);
                }
            }
        });
    }

    @Override
    public void refreshView()
    {
        Image image;

        try
        {
            if (name.equals("egg"))
            {
                image = new Image(new FileInputStream("./Textures/Products/Egg/normal.png"));
            } else if (name.equals("horn"))
            {
                image = new Image(new FileInputStream("./Textures/Products/Horn/normal_5.png"));
            } else if (name.equals("wool"))
            {
                image = new Image(new FileInputStream("./Textures/Products/Wool/normal.png"));
            } else if (name.equals("plume"))
            {
                image = new Image(new FileInputStream("./Textures/Products/Plume/normal.png"));
            } else
            {
                image = new Image(new FileInputStream("./Textures/Products/" + name + ".png"));
            }
            getImageView().setImage(image);
            getImageView().setFitWidth(48);
            getImageView().setFitHeight(48);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        if(getCell()!=null){
            getImageView().setX(GameScene.modifiedX(getCell().getPositionX()));
            getImageView().setY(GameScene.modifiedY(getCell().getPositionY()));
        }

    }
    public void moveToWareHouse() {
        getCell().destroyEntity(this);
        javafx.scene.image.ImageView curImageView = this.getImageView();
        double duration = 0.3;
        KeyValue keyValueX = new KeyValue(curImageView.xProperty(), 400);
        KeyValue keyValueY = new KeyValue(curImageView.yProperty(), 610);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration)
                , keyValueX, keyValueY);
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
        timeline.setOnFinished(event ->
        {
            curImageView.setVisible(false);
            GameScene.deleteNode(curImageView);
        });
    }

    public void setCreatingTurn(int creatingTurn)
    {
        this.creatingTurn = creatingTurn;
    }

    public boolean isInWareHouse()
    {
        return isInWareHouse;
    }

    public void setInWareHouse(boolean inWareHouse)
    {
        isInWareHouse = inWareHouse;
    }

    public boolean isExpired()
    {
        if (isInWareHouse) return false;
        return (creatingTurn + Constant.TERM_OF_DESTROY_ITEM_IN_MAP <= InputReader.getCurrentController().getTurn());
    }

    public void expire()
    {
        getMap().destroyEntity(getCell(), this);
    }

    public int getBuyCost()
    {
        Client client=InputReader.getClient();
        return (client==null || !client.isOnline())?buyCost:InputReader.getClient().getBuyCost(this);
    }

    public int getSellCost()
    {
        Client client=InputReader.getClient();
        return (client==null || !client.isOnline())?sellCost:InputReader.getClient().getSellCost(this);
    }

    public int getVolume()
    {
        return volume;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public void initLoad() {
        //Only for map items
        super.initLoad();
        refreshView();
    }

    public Integer getDefaultBuyCost()
    {
        return buyCost;
    }

    public Integer getDefaultSellCost()
    {
        return sellCost;
    }
}
