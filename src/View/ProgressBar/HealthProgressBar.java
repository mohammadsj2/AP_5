package View.ProgressBar;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HealthProgressBar extends ProgressBar{

    public HealthProgressBar(double x, double y) {
        super(x, y);
    }

    @Override
    public void refresh() {
        Image image=getFillImage();
        double x=image.getWidth()*percentage;
        if(x<EPSILON){
            x= EPSILON;
        }
        fillView.setViewport(new Rectangle2D(0,0,x,image.getHeight()));
    }
    @Override
    public Image getBackGroundImage(){
        return null;
    }

    @Override
    public Image getFillImage(){
        try {
            return new Image(new FileInputStream("Textures/UI/Progress/animals.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
