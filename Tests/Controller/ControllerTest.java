package Controller;

import Model.Entity.Entity;
import org.junit.Assert;
import Exception.*;
import org.junit.Test;
import Exception.NotEnoughMoneyException;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerTest {
    @Test
    public void moneyHandlingTest(){
        try {
            InputReader.currentController=new Controller(1000,new ArrayList<>());
            Controller controller=InputReader.getCurrentController();
            controller.increaseMoney(100);
            controller.nextTurn();
            controller.addAnimal("Chicken");
            ArrayList <Entity> entities=controller.getMap().getEntities();
            for(Entity entity:entities)
            {
                System.out.println(entity.getCell().getPositionX()+" "+entity.getCell().getPositionY());
            }
            controller.nextTurn();
            entities=controller.getMap().getEntities();
            for(Entity entity:entities)
            {
                System.out.println(entity.getCell().getPositionX()+" "+entity.getCell().getPositionY());
            }

        }catch (WinningMessage e)
        {
            System.out.println("HOOOOORRRRRAAAAAYYYY!!!!");
            return ;
        } catch (NotEnoughMoneyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   Assert.fail();
    }



}
