package Model;

import Constant.Constant;
import Controller.Controller;
import Controller.InputReader;
import Model.Entity.Entity;
import Model.Entity.Item;
import com.gilecode.yagson.YaGson;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Exception.*;

public class WorkShopTest {
    @Test
    public void maxLevelCanDoWithItemTest(){
        YaGson yaGson=new YaGson();
        try {
            WorkShop workShop=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/EggPowderPlant.json"), WorkShop.class);
            workShop.upgrade();
            ArrayList<Item> inputItems=new ArrayList<>();
            inputItems.add(Constant.getItemByType("egg"));
            inputItems.add(Constant.getItemByType("egg"));

            int usedLevel = workShop.maxLevelCanDoWithItems(inputItems);
            workShop.startByLevel(usedLevel);
            Assert.assertEquals(usedLevel,1);
        } catch (FileNotFoundException | CantUpgradeException | StartBusyProducerException e) {
            Assert.fail();
        }

    }
}


















