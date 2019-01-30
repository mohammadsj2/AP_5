package YaGson;

import View.ProgressBar.BlueProgressBar;
import View.ProgressBar.ProgressBar;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.ExclusionStrategy;
import com.gilecode.yagson.com.google.gson.FieldAttributes;
import javafx.animation.Animation;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class YaGsonExclusionStrategyForServer implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return (fieldAttributes.getDeclaredClass()== Formatter.class ||
                fieldAttributes.getDeclaredClass()== Scanner.class ||
                fieldAttributes.getDeclaredClass()== Socket.class ||
                fieldAttributes.getDeclaredClass()== YaGson.class ||
                fieldAttributes.getDeclaredClass()== ImageView.class ||
                fieldAttributes.getDeclaredClass()== Animation.class ||
                fieldAttributes.getDeclaredClass()== ProgressBar.class ||
                fieldAttributes.getDeclaredClass()== BlueProgressBar.class ||
                fieldAttributes.getDeclaredClass()== Label.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
