package View;

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

public class Button
{
    StackPane pane=new StackPane();
    Text insideText=new Text();
    ImageView imageView;

    public Button(String text, int height, int width, double x, double y)
    {
        Image image = getRelaxImage();
        imageView=new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        insideText.setText(text);
        insideText.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: "+(int)(0.4*Math.min(height,width)));
        insideText.setFill(Color.WHITE);
        pane.getChildren().add(imageView);
        pane.getChildren().add(insideText);
        pane.setAlignment(Pos.CENTER);
        pane.relocate(x,y);
        pane.setOnMouseEntered(event -> imageView.setImage(getPushedImage()));
        pane.setOnMouseExited(event -> imageView.setImage(getRelaxImage()));
    }

    private Image getRelaxImage() {
        Image image=null;
        try
        {
            image = new Image(new FileInputStream("Textures/button.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    private Image getPushedImage() {
        Image image=null;
        try
        {
            image = new Image(new FileInputStream("Textures/PushedButton.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return image;
    }


    public Pane getNode()
    {
        return pane;
    }
    public Text getTextLabel(){return insideText;}
    public void setText(String text)
    {
        insideText.setText(text);
    }
}
