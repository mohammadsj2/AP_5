package View.Scene.MultiPlayerScene;

import Constant.Constant;
import Controller.InputReader;
import Model.Entity.Item;
import View.Button.BlueButton;
import View.Button.Button;
import View.Label.FancyLabel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomizeScene extends MultiPlayerScene
{
    public final static CustomizeScene CUSTOMIZE_SCENE=new CustomizeScene();

    @Override
    public void init()
    {

        try
        {
            initBackground();
            initStartButtons();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void initStartButtons() {
        super.initStartButtons();

        FancyLabel buyCostLabel=new FancyLabel("Buy Cost",20,200,60);
        root.getChildren().add(buyCostLabel.getNode());
        FancyLabel sellCostLabel=new FancyLabel("Sell Cost",20,300,60);
        root.getChildren().add(sellCostLabel.getNode());

        ArrayList<Item> items=new ArrayList<>();
        ArrayList<TextField> buyCostFields=new ArrayList<>();
        ArrayList<TextField> sellCostFields=new ArrayList<>();
        items.add(Constant.getItemByType("egg"));
        items.add(Constant.getItemByType("flour"));
        items.add(Constant.getItemByType("cake"));
        items.add(Constant.getItemByType("flourycake"));
        items.add(Constant.getItemByType("wool"));
        items.add(Constant.getItemByType("sewing"));
        items.add(Constant.getItemByType("adornment"));
        items.add(Constant.getItemByType("fabric"));
        items.add(Constant.getItemByType("milk"));
        int y=120;
        for(Item item:items)
        {
            ImageView imageView=item.getImageView();
            imageView.relocate(100,y-7);
            root.getChildren().add(imageView);
            TextField buyCostField=new TextField();
            buyCostField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(5));
            buyCostField.setText(String.valueOf(InputReader.getServer().getBuyCost(item)));
            buyCostField.relocate(200,y);
            buyCostField.setMaxHeight(30);
            buyCostField.setMinHeight(30);
            buyCostField.setMaxWidth(85);
            buyCostField.setAlignment(Pos.CENTER);
            buyCostFields.add(buyCostField);
            root.getChildren().add(buyCostField);
            TextField sellCostField=new TextField();
            sellCostField.addEventFilter(KeyEvent.KEY_TYPED , numeric_Validation(5));
            sellCostField.setText(String.valueOf(InputReader.getServer().getSellCost(item)));
            sellCostField.relocate(300,y);
            sellCostField.setMaxHeight(30);
            sellCostField.setMinHeight(30);
            sellCostField.setMaxWidth(85);
            sellCostField.setAlignment(Pos.CENTER);
            sellCostFields.add(sellCostField);
            root.getChildren().add(sellCostField);
            y+=60;
        }
        Button applyButton=new BlueButton("Apply",50,200,400,130);
        applyButton.getNode().setOnMouseClicked(event ->
        {
            HashMap<Item,Integer> buyCostsMap=new HashMap<>();
            for(int i=0;i<items.size();i++)
            {
                buyCostsMap.put(items.get(i),new Integer(buyCostFields.get(i).getText()));
            }
            InputReader.getServer().setBuyCosts(buyCostsMap);
            HashMap<Item,Integer> sellCostsMap=new HashMap<>();
            for(int i=0;i<items.size();i++)
            {
                sellCostsMap.put(items.get(i),new Integer(sellCostFields.get(i).getText()));
            }
            InputReader.getServer().setSellCosts(sellCostsMap);
        });
        root.getChildren().add(applyButton.getNode());
        Button setToDefaultButton=new BlueButton("Set To Default",50,200,400,200);
        setToDefaultButton.getNode().setOnMouseClicked(event ->
        {
            for(int i=0;i<items.size();i++)
            {
                Item item=items.get(i);
                TextField buyCostField=buyCostFields.get(i);
                TextField sellCostField=sellCostFields.get(i);
                buyCostField.setText(String.valueOf(Constant.getItemByType(item.getName()).getBuyCost()));
                sellCostField.setText(String.valueOf(Constant.getItemByType(item.getName()).getSellCost()));
            }
        });
        root.getChildren().add(setToDefaultButton.getNode());

    }

    private EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
        return e -> {
            TextField txt_TextField = (TextField) e.getSource();
            if (txt_TextField.getText().length() >= max_Lengh) {
                e.consume();
            }
            if(e.getCharacter().matches("[0-9.]")){
                if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                    e.consume();
                }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                    e.consume();
                }
            }else{
                e.consume();
            }
        };
    }
}
