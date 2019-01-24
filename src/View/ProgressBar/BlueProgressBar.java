package View.ProgressBar;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BlueProgressBar extends ProgressBar{
    public BlueProgressBar(double x, double y){
        super(x,y);
    }
    public BlueProgressBar(){

    }

    @Override
    public Image getFillImage() {
        try {
            return new Image(new FileInputStream("Textures/UI/Progress/blue.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
