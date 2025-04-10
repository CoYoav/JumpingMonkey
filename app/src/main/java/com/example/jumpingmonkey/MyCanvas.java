package com.example.jumpingmonkey;

import static com.example.jumpingmonkey.Constants.FLOWER_INNER_RADIUS;
import static com.example.jumpingmonkey.Constants.FLOWER_OUTER_RADIUS;
import static com.example.jumpingmonkey.Constants.SCROLL_KP;
import static com.example.jumpingmonkey.Constants.STONE_RADIUS;
import static com.example.jumpingmonkey.Constants.WIDTH_METERS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Random;

public class MyCanvas extends View {

    // Add a Bitmap field for the background image
    private Bitmap backgroundBitmap, stoneBitmap, pauseBitmap, playBitmap, lavaBitmap;
    private final Paint m_brush;
    private final Paint m_Textbrush;
    private final ArrayList<Segment> brunches = new ArrayList<>();
    private double meterToPixels;
    private double screenTopMeters;
    private final Monkey george = new Monkey(null);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int FRAME_RATE = 16; // 20ms per frame for 50 FPS
    private int maxHeight = 0;
    private boolean gameOver = false;
    private boolean pause = false;
    private Rect pausePlayArea;
    private Paint pausePlayPaint;
    private final Runnable gameLoop;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        stoneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stone);
        lavaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lavastone);
        pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        playBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        brunches.add(Segment.generateSegment(-100, 1, 1));
        brunches.add(Segment.generateSegment(-100, 1, 2));
        brunches.add(Segment.generateSegment(-100, 1, 3));
        brunches.add(Segment.generateSegment(-100, 1, 4));
        brunches.add(Segment.generateSegment(2.3, 3, -1));

        pausePlayPaint = new Paint();
        pausePlayPaint.setAlpha(200);
        m_brush = new Paint();
        m_brush.setStrokeWidth(30);
        m_brush.setColor(Color.BLACK);
        m_Textbrush = new Paint();
        m_Textbrush.setColor(Color.WHITE);
        m_Textbrush.setTextSize(80);
        m_Textbrush.setAntiAlias(true);
        m_Textbrush.setTextAlign(Paint.Align.RIGHT);
        m_Textbrush.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        pausePlayArea = new Rect(getWidth() / 10, 100, getWidth() / 5, 100 + getWidth() / 10);
        updateBrunches();
        gameLoop = new Runnable() {
            @Override
            public void run() {
                gameIteration(this);
            }
        };
        startGameLoop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        meterToPixels = getWidth() / WIDTH_METERS;
        screenTopMeters = getHeight() / meterToPixels;
        pausePlayArea = new Rect(getWidth() / 10, 100, getWidth() / 5, 100 + getWidth() / 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (backgroundBitmap != null) {
            Rect destRect = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(backgroundBitmap, null, destRect, null);
        }
        // Draw branch points
        for (int i = 0; i < brunches.size() /*&& brunches.get(i).getBottom() < screenTopMeters*/; i++) {
            for (BranchPoint brunch : brunches.get(i).getBrunchPoints()) {
                m_brush.setColor(Color.rgb(brunch.r, brunch.g, brunch.b));
                Path brunchPath = new Path();
                ArrayList<Vector2D> baseForm = brunch.getBaseForm();
                brunchPath.moveTo(getXPixels(baseForm.get(0).getX()), getYPixels(baseForm.get(0).getY()));
                for (int j = 1; j < baseForm.size(); j++) {
                    brunchPath.lineTo(getXPixels(baseForm.get(j).getX()), getYPixels(baseForm.get(j).getY()));
                }
                ArrayList<Vector2D> firstCurve = brunch.getFirstCurve();
                brunchPath.quadTo(
                        getXPixels(firstCurve.get(0).getX()),
                        getYPixels(firstCurve.get(0).getY()),
                        getXPixels(firstCurve.get(1).getX()),
                        getYPixels(firstCurve.get(1).getY()));
                ArrayList<Vector2D> secondCurve = brunch.getSecondCurve();
                brunchPath.lineTo(
                        getXPixels(secondCurve.get(0).getX()),
                        getYPixels(secondCurve.get(0).getY()));

                brunchPath.quadTo(
                        getXPixels(secondCurve.get(1).getX()),
                        getYPixels(secondCurve.get(1).getY()),
                        getXPixels(secondCurve.get(2).getX()),
                        getYPixels(secondCurve.get(2).getY()));
                brunchPath.lineTo(
                        getXPixels(secondCurve.get(3).getX()),
                        getYPixels(secondCurve.get(3).getY()));
                brunchPath.close();
                canvas.drawPath(brunchPath, m_brush);
            }
        }
        m_brush.setColor(Color.rgb(41, 29, 5));
        double[] armPoints = george.findTangents();
        if (armPoints.length == 4) {
            canvas.drawLine(
                    getXPixels(armPoints[0]),
                    getYPixels(armPoints[1]),
                    getXPixels(george.getCurrentPoint().getX()),
                    getYPixels(george.getCurrentPoint().getY()),
                    m_brush);
            canvas.drawLine(
                    getXPixels(armPoints[2]),
                    getYPixels(armPoints[3]),
                    getXPixels(george.getCurrentPoint().getX()),
                    getYPixels(george.getCurrentPoint().getY()),
                    m_brush);
        }

        for (int i = 0; i < brunches.size() && brunches.get(i).getBottom() < screenTopMeters; i++) {
            for (BranchPoint brunch : brunches.get(i).getBrunchPoints()) {
                if (!brunch.isCollapsed()) {
                    m_brush.setColor(Color.rgb(94, 41, 158));
                    canvas.drawCircle(getXPixels(brunch.getX()), getYPixels(brunch.getY()), (int) (FLOWER_OUTER_RADIUS * meterToPixels), m_brush);
                    m_brush.setColor(Color.rgb(230, 247, 42));
                    canvas.drawCircle(getXPixels(brunch.getX()), getYPixels(brunch.getY()), (int) (FLOWER_INNER_RADIUS * meterToPixels), m_brush);
                }
            }
        }
        for (int i = 0; i < brunches.size() && brunches.get(i).getBottom() < screenTopMeters; i++) {
            for (Stone stone : brunches.get(i).getStonePoints()) {
                drawStone(stone, canvas);
            }
        }
        m_brush.setColor(Color.rgb(41, 29, 5));
        // Draw George with face details, rotated according to the velocity direction
        float x = getXPixels(george.getX());
        float y = getYPixels(george.getY());
        float radius = (float) (meterToPixels * george.getRadius());

        // Get the direction angle in radians and convert it to degrees
        double angleRadians = george.getAngleRad();
        float angleDegrees = (float) (-180 * angleRadians / Math.PI);

        // Save the current canvas state
        canvas.save();

        // Rotate the canvas around George's center
        canvas.rotate(angleDegrees, x, y);

        // Draw the outer face (brown color)
        canvas.drawCircle(x, y, radius, m_brush);

        // Draw the inner face (lighter brown), positioned slightly higher
        Paint innerFaceBrush = new Paint();
        innerFaceBrush.setColor(Color.rgb(205, 133, 63)); // Lighter brown color
        canvas.drawCircle(x, y - radius * 0.1f, radius * 0.7f, innerFaceBrush);

        // Draw eyes, positioned relative to the adjusted inner face
        Paint eyeBrush = new Paint();
        eyeBrush.setColor(Color.WHITE);
        float eyeRadius = radius * 0.2f;
        canvas.drawCircle(x - eyeRadius, y - radius * 0.4f, eyeRadius, eyeBrush); // Left eye
        canvas.drawCircle(x + eyeRadius, y - radius * 0.4f, eyeRadius, eyeBrush); // Right eye

        // Draw pupils
        Paint pupilBrush = new Paint();
        pupilBrush.setColor(Color.BLACK);
        float pupilRadius = eyeRadius * 0.5f;
        canvas.drawCircle(x - eyeRadius, y - radius * 0.4f, pupilRadius, pupilBrush); // Left pupil
        canvas.drawCircle(x + eyeRadius, y - radius * 0.4f, pupilRadius, pupilBrush); // Right pupil

        // Draw nose, positioned closer to the inner face top
        Paint noseBrush = new Paint();
        noseBrush.setColor(Color.BLACK);
        canvas.drawCircle(x, y - radius * 0.15f, pupilRadius * 0.6f, noseBrush);

        // Draw mouth (simple smile), positioned slightly lower than the nose
        Paint mouthBrush = new Paint();
        mouthBrush.setColor(Color.BLACK);
        mouthBrush.setStrokeWidth(3);
        mouthBrush.setStyle(Paint.Style.STROKE);
        canvas.drawArc(x - eyeRadius, y - radius * 0.1f, x + eyeRadius, y + eyeRadius * 0.3f, 0, 180, false, mouthBrush);

        // Restore the canvas to its original orientation
        canvas.restore();
        canvas.drawText("now: " + (int) george.getY(), getWidth() - 100, 100, m_Textbrush);
        canvas.drawText("max: " + maxHeight, getWidth() - 100, 200, m_Textbrush);
        if (pause) {
            if (playBitmap != null) {
                canvas.drawBitmap(playBitmap, null, pausePlayArea, pausePlayPaint);
            }
        } else {
            if (pauseBitmap != null) {
                canvas.drawBitmap(pauseBitmap, null, pausePlayArea, pausePlayPaint);
            }
        }
    }


    private int getXPixels(double xMeters) {
        return (int) (meterToPixels * xMeters);
    }

    private int getYPixels(double yMeters) {
        return (int) (meterToPixels * (screenTopMeters - yMeters));
    }

    private double getXMeters(double xPixels) {
        return xPixels / meterToPixels;
    }

    private double getYMeters(double yPixels) {
        return -yPixels / meterToPixels + screenTopMeters;
    }

    private void startGameLoop() {
        handler.postDelayed(gameLoop, FRAME_RATE);
    }

    public void stopGameLoop() {
        handler.removeCallbacksAndMessages(null); // Stops the game loop
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double x = getXMeters(event.getX());
        double y = getYMeters(event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!pause) george.drag(x, y);
                if (pausePlayArea.contains((int) event.getX(), (int) event.getY())) {
                    pause = !pause; // Toggle the pause state
                    return true;    // Consume the event
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (george.isOnDrag() && !pause) george.setPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (george.isOnDrag() && !pause) george.jump();
                break;

        }
        return true;
    }

    private void updateBrunches() {
        while (brunches.get(0).getTop() + getHeight() / meterToPixels < screenTopMeters - 3) {
//            Log.d("seg", "rm id = "+brunches.get(0).getId());
            brunches.remove(0);
            Random r = new Random();
            final double bottom = brunches.get(brunches.size() - 1).getTop();
            final double margin = 3;
            final int lastID = brunches.get(brunches.size() - 1).getId();
            int segmentID = lastID;
            while (Segment.areNumbersValid(lastID, segmentID)) segmentID = r.nextInt(20);
            brunches.add(Segment.generateSegment(bottom, margin, segmentID));
//            Log.d("seg", "new id = "+segmentID);
        }
    }

    private void drawStone(Stone stone, Canvas canvas) {
        Vector2D stonePoint = stone.position;
        Bitmap map = stone.type == Stone.Type.REGULAR_STONE ? stoneBitmap : lavaBitmap;
        if (map != null) {
            Rect destRect = new Rect(
                    getXPixels(stonePoint.getX() - STONE_RADIUS),
                    getYPixels(stonePoint.getY() + STONE_RADIUS),
                    getXPixels(stonePoint.getX() + STONE_RADIUS),
                    getYPixels(stonePoint.getY() - STONE_RADIUS)
            );
            canvas.drawBitmap(map, null, destRect, null);
        }
    }

    public void gameIteration(Runnable r) {
        if (!gameOver && !pause) {
            maxHeight = Math.max(maxHeight, (int) george.getY());
            if (george.hasBrunch()) {
                double error = george.getCurrentPoint().getY() + 0.7 * getHeight() / meterToPixels - screenTopMeters;
                screenTopMeters += SCROLL_KP * error;
            } else {
                double error = george.getY() + 0.7 * getHeight() / meterToPixels - screenTopMeters;
                screenTopMeters += SCROLL_KP * error;
            }
            for (int i = 0; i < brunches.size(); i++) {
                Segment segment = brunches.get(i);
                segment.dutyCycle(FRAME_RATE / 1000.0);
                george.catchPoint(segment.getBrunchPoints());
                george.collideWithStones(segment.getStonePoints());
            }
            george.dutyCycle(FRAME_RATE / 1000.0);
            if (george.getY() + getHeight() / meterToPixels < screenTopMeters)
                george.setAlive(false);
            updateBrunches();
            if (!george.isAlive()) gameOver = true;
        }
        invalidate(); // Redraw the view
        handler.postDelayed(r, FRAME_RATE); // Schedule the next frame
        Log.d("bot h", "" + brunches.get(0).getBottom());
    }
    public boolean isGameOver(){
        return gameOver;
    }
}