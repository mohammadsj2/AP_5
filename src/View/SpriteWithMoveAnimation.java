package View;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteWithMoveAnimation extends Transition
{
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private double moveX = 0;
    private double moveY = 0;
    private double startX;
    private double startY;
    private int lastIndex;
    public SpriteWithMoveAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height)
    {
        this.imageView = imageView;
        this.startX = imageView.getX();
        this.startY = imageView.getY();
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }
    public SpriteWithMoveAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height, double moveX, double moveY)
    {
        this(imageView, duration, count, columns, offsetX, offsetY, width, height);
        this.moveX = moveX;
        this.moveY = moveY;
    }



    public double getMoveX() {
        return moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    @Override
    protected void interpolate(double k)
    {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex)
        {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            double changeX = (count > 1 ? getMoveX() * (lastIndex - index) / (count - 1) : 0);
            double changeY = (count > 1 ? getMoveY() * (lastIndex - index) / (count - 1) : 0);
            double curX = imageView.getX() + changeX;
            double curY = imageView.getY() + changeY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
          //  imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}
