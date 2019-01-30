package View.Label;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class FancyLabel
{
    Label label;
    public FancyLabel(String text, int fontSize, double x, double y)
    {
        label=new Label(text);
        label.setStyle("-fx-text-fill: Yellow;-fx-font-size: "+fontSize+";-fx-font-family: 'Comic Sans MS';" +
                "-fx-background-color: rgba(0, 0, 0, 0.5);-fx-padding: 2px;" +
                "-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;");

        label.relocate(x,y);
    }

    public Node getNode()
    {
        return label;
    }
}
