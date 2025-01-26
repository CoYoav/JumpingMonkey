package com.example.jumpingmonkey;


import java.util.Random;

public class VinePoint extends BranchPoint {
    private final double cycleTime;
    private double timeCount;
    private Vector2D secondPoint;
    private Vector2D firstPoint;
    private double top;

    public VinePoint(double x1, double y1, double x2, double y2, double t) {
        super((x1+x2)/2, (y1+y2)/2);
        Random random = new Random();
        cycleTime = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2))/2;
        timeCount = t*cycleTime;
        secondPoint = new Vector2D(x2, y2);
        firstPoint = new Vector2D(x1, y1);
        top = Math.max(y1, y2);
        r = 36 + random.nextInt(40) - 20;
        g = 199 + random.nextInt(40) - 20;
        b = 139 + random.nextInt(40) - 20;
    }

    private void updateCurrentPoint() {
        super.setMainPoint(Vector2D.lerp(this.firstPoint, this.secondPoint, timeCount / cycleTime));
    }

    @Override
    public void dutyCycle(double time) {
        if (timeCount >= cycleTime) {
            timeCount = 0;
            Vector2D holder = firstPoint;
            firstPoint = secondPoint;
            secondPoint = holder;
        }
        updateCurrentPoint();
        super.dutyCycle(time);
        timeCount += time;
    }
    @Override
    public double getTop(){
        return this.top;
    }

}
