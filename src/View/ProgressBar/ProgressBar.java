package View.ProgressBar;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProgressBar {
    public static final double EPSILON = 0.000001;
    Pane pane=new Pane();
    double percentage=1;
    ImageView progressView=new ImageView(),fillView=new ImageView();

    public ProgressBar(double x, double y){
        pane.getChildren().addAll(progressView,fillView);
        progressView.setX(x);
        progressView.setY(y);
        fillView.setX(x);
        fillView.setY(y);
        progressView.setImage(getBackGroundImage());
        fillView.setImage(getFillImage());
    }
    public ProgressBar(){
        pane.getChildren().addAll(progressView,fillView);
    }
    public Image getBackGroundImage(){
        try {
            return new Image(new FileInputStream("Textures/UI/Progress/prodhouse_sub.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Image getFillImage(){
        try {
            return new Image(new FileInputStream("Textures/UI/Progress/prodhouse.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void refresh(){
        Image image=getFillImage();
        double y=image.getHeight()*(1.0-percentage);
        if(y==image.getHeight()){
            y= image.getHeight()-EPSILON;
        }
        fillView.setX(progressView.getX());
        fillView.setY(progressView.getY()+y);
        fillView.setViewport(new Rectangle2D(0,y,image.getWidth(),image.getHeight()-y));
    }
    public void setPercentage(double percentage){
        this.percentage=percentage;
        refresh();
    }
    public void setXY(double x,double y){
       progressView.setX(x);
       progressView.setY(y);
       refresh();
    }

    public Node getNode(){
        return pane;
    }
}
