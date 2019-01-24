package View.Button;

import Controller.InputReader;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class Button
{
    StackPane pane = new StackPane();
    Text insideText = new Text();
    ImageView imageView;
    boolean isInGameScene;

    public Button(String text, int height, int width, double x, double y, boolean isInGameScene)
    {
        this.isInGameScene = isInGameScene;
        Image image = getRelaxImage();
        imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        insideText.setText(text);
        insideText.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: " + (int) (0.4 * Math.min(height, width)));
        insideText.setFill(Color.WHITE);
        pane.getChildren().add(imageView);
        pane.getChildren().add(insideText);
        pane.setAlignment(Pos.CENTER);
        pane.relocate(x, y);
        pane.setOnMouseEntered(event ->
        {
            if (isInGameScene && InputReader.getCurrentController().isGameFinished()) return;
            imageView.setImage(getPushedImage());
        });
        pane.setOnMouseExited(event ->
        {
            if (isInGameScene && InputReader.getCurrentController().isGameFinished()) return;
            imageView.setImage(getRelaxImage());
        });
    }

    abstract Image getRelaxImage();

    abstract Image getPushedImage();

    public Pane getNode()
    {
        return pane;
    }

    public Text getTextLabel()
    {
        return insideText;
    }

    public void setText(String text)
    {
        insideText.setText(text);
    }

    public void onlyShowTextOfButton()
    {
        imageView.setVisible(false);
    }
}
