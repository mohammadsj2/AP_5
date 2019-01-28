package View.Label;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class FancyLabel
{
    Label label;
    public FancyLabel(String text, int fontSize, double x, double y)
    {
        label=new Label(text);
        label.setStyle("-fx-text-fill: Yellow;-fx-font-size: "+fontSize+";-fx-font-family: 'Comic Sans MS'");

        label.relocate(x,y);
    }

    public Node getNode()
    {
        return label;
    }
}
