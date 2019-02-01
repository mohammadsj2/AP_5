package View.ProgressBar;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    Property<Double> percentageProperty;

    public ProgressBar(double x, double y){
        pane.getChildren().addAll(progressView,fillView);
        pane.setLayoutX(x);
        pane.setLayoutY(y);
        progressView.setImage(getBackGroundImage());
        fillView.setImage(getFillImage());
        percentageProperty=new Property<Double>() {
            @Override
            public void bind(ObservableValue<? extends Double> observable) {
            }

            @Override
            public void unbind() {
                System.out.println("unbine");
            }

            @Override
            public boolean isBound() {
                return false;
            }

            @Override
            public void bindBidirectional(Property<Double> other) {
            }

            @Override
            public void unbindBidirectional(Property<Double> other) {
            }

            @Override
            public Object getBean() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void addListener(ChangeListener<? super Double> listener) {
                System.out.println("addListener");
            }

            @Override
            public void removeListener(ChangeListener<? super Double> listener) {
            }

            @Override
            public Double getValue() {
                return getPercentage();
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {
            }

            @Override
            public void setValue(Double value) {
                setPercentage(value);
            }
        };
    }

    public Property<Double> getPercentageProperty() {
        return percentageProperty;
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

    public double getPercentage() {
        return percentage;
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
       pane.setLayoutX(x);
       pane.setLayoutY(y);
       refresh();
    }

    public Node getNode(){
        return pane;
    }
}
