package View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FancyButton
{
    StackPane pane=new StackPane();
    Text insideText=new Text();

    public FancyButton(String text, int height, int width,double x,double y)
    {
        Image image = null;
        try
        {
            image = new Image(new FileInputStream("Textures/button.png"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        ImageView imageView=new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        insideText.setText(text);
        insideText.setStyle("-fx-font-family: 'Comic Sans MS';");
        insideText.setFill(Color.WHITE);
        pane.getChildren().add(imageView);
        pane.getChildren().add(insideText);
        pane.setAlignment(Pos.CENTER);
        pane.relocate(x,y);

    }
    public Pane getNode()
    {
        return pane;
    }

    public void setText(String text)
    {
        insideText.setText(text);
    }
}
