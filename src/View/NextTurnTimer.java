package View;

import Controller.InputReader;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

import java.util.Random;

public class NextTurnTimer extends AnimationTimer {
    private long lastTime=0;
    private double time=0;
    private long second=800000000;
    @Override
    public void handle(long now) {
        if(lastTime==0)
            lastTime=now;
        if(now>lastTime+(second/5))
        {
            lastTime=now;
            time+=1;
            InputReader.nextTurn(1);
        }
    }
    public void setTime(int time){this.time=time;}
}
