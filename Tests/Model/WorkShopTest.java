package Model;

import Controller.Controller;
import Controller.InputReader;
import Model.Entity.Entity;
import com.gilecode.yagson.YaGson;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WorkShopTest {
    @Test
    public void maxLevelCanDoWithItemTest(){
        YaGson yaGson=new YaGson();
        try {
            WorkShop workShop=yaGson.fromJson(new FileReader("./ResourcesRoot/WorkShops/EggPowderPlant.json"), WorkShop.class);
        } catch (FileNotFoundException e) {
            Assert.fail();
        }
    }
}
