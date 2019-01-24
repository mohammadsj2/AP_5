package View.Button;

import View.View;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BlueButton extends Button
{


    public BlueButton(String text, int height, int width, double x, double y)
    {
        super(text,height,width,x,y);
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
