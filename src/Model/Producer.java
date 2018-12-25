package Model;

import Exception.StartBusyProducerException;
import Model.Entity.Item;

import java.util.ArrayList;

public interface Producer {
    public void startProduction()throws StartBusyProducerException;
    public ArrayList<Item> getInPutItems();
    public ArrayList<Item> getOutPutItems();
    public boolean haveProduct();
    public void endProduction();
}
