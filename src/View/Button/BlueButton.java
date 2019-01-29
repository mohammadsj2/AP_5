package View.Button;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BlueButton extends Button
{


    public BlueButton(String text, int height, int width, double x, double y,boolean isInGameScene)
    {
        super(text,height,width,x,y,isInGameScene,false);
    }
    public BlueButton(String text, int height, int width, double x, double y)
    {
        super(text,height,width,x,y,false,false);
    }

    @Override
    Image getRelaxImage() {
        Image image=null;
        try
        {
            image = new Image(new FileInputStream("Textures/Button/BlueButton/Button.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    @Override
    Image getPushedImage() {
        Image image=null;
        try
        {
            image = new Image(new FileInputStream("Textures/Button/BlueButton/PushedButton.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }



}
