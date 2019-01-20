package View;

import Controller.InputReader;
import javafx.animation.AnimationTimer;

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
            Random random = new Random();
            if (random.nextInt(130) == 1) {
                String wildName = (random.nextInt(2) == 0 ? "bear" : "lion");
                for (int j = 0; j < 2; ++j)
                    InputReader.buy(wildName);
            }
            InputReader.nextTurn(1);
        }
    }
    public void setTime(int time){this.time=time;}
}
