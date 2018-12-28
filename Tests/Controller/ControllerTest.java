package Controller;

import org.junit.Assert;
import org.junit.Test;
import Exception.NotEnoughMoneyException;

public class ControllerTest {
    @Test
    public void moneyHandlingTest(){
        try {
            InputReader.getCurrentController().subtractMoney(50);
        } catch (NotEnoughMoneyException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }



}
