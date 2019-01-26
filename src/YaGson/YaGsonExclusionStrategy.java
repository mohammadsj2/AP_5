package YaGson;

import View.ProgressBar.BlueProgressBar;
import View.ProgressBar.ProgressBar;
import com.gilecode.yagson.com.google.gson.ExclusionStrategy;
import com.gilecode.yagson.com.google.gson.FieldAttributes;
import javafx.animation.Animation;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class YaGsonExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return (fieldAttributes.getDeclaredClass()== ImageView.class ||
                fieldAttributes.getDeclaredClass()== Animation.class ||
                fieldAttributes.getDeclaredClass()== ProgressBar.class ||
                fieldAttributes.getDeclaredClass()== BlueProgressBar.class ||
                fieldAttributes.getDeclaredClass()== Label.class);
    }
}
