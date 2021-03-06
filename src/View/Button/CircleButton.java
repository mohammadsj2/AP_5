package View.Button;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CircleButton extends Button
{
    public CircleButton(String text, int height, int width, double x, double y,boolean isInGameScene,boolean locked)
    {
        super(text,height,width,x,y,isInGameScene,locked);
        insideText.setFill(Color.BLACK);
    }

    @Override
    Image getRelaxImage() {
        Image image=null;
        try
        {
            if(locked) image = new Image(new FileInputStream("Textures/Button/CircleButton/CircleButtonLocked.png"));
            else image = new Image(new FileInputStream("Textures/Button/CircleButton/CircleButton.png"));
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
            if(locked) image = new Image(new FileInputStream("Textures/Button/CircleButton/CircleButtonLocked.png"));
            else image = new Image(new FileInputStream("Textures/Button/CircleButton/CircleButtonPushed.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }
}
