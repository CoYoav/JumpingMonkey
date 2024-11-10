package com.example.jumpingmonkey;

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

public class MyCanvas extends View {

    // Add a Bitmap field for the background image
    private Bitmap backgroundBitmap;
    private final Paint m_brush;
    private final Paint m_Textbrush;
    private final ArrayList<BranchPoint> brunches = new ArrayList<>();
    private double meterToPixels;
    private double screenTopMeters;
    private final Monkey george = new Monkey(null);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int FRAME_RATE = 16; // 20ms per frame for 50 FPS
    private int maxHeight = 0;
    private final Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            maxHeight = Math.max(maxHeight, (int) george.getY());
            for (int i = 0; i < brunches.size(); i++) {
                brunches.get(i).dutyCycle(FRAME_RATE / 1000.0);
                george.catchPoint(brunches.get(i));
            }
            george.dutyCycle(FRAME_RATE / 1000.0);
            invalidate(); // Redraw the view
            handler.postDelayed(this, FRAME_RATE); // Schedule the next frame
        }
    };

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background2);

        brunches.add(new BranchPoint(2 * WIDTH_METERS / 9, 2.3));
        brunches.add(new BranchPoint(7 * WIDTH_METERS / 9, 2.3));
        brunches.add(new BranchPoint(WIDTH_METERS / 2, 2.6));
        brunches.add(new VinePoint(2, 2, 4, 8));
        m_brush = new Paint();
        m_brush.setStrokeWidth(24);
        m_brush.setColor(Color.BLACK);
        m_Textbrush = new Paint();
        m_Textbrush.setColor(Color.WHITE);
        m_Textbrush.setTextSize(100);
        m_Textbrush.setAntiAlias(true);
        m_Textbrush.setTextAlign(Paint.Align.RIGHT);
        m_Textbrush.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        startGameLoop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        meterToPixels = getWidth() / WIDTH_METERS;
        screenTopMeters = getHeight() / meterToPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (backgroundBitmap != null) {
            Rect destRect = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(backgroundBitmap, null, destRect, null);
        }
        canvas.drawText("" + (int) george.getY(),getWidth() -100, 200, m_Textbrush);

        // Draw branch points
        for (int i = 0; i < brunches.size(); i++) {
            BranchPoint brunch = brunches.get(i);
            m_brush.setColor(Color.rgb(brunch.r, brunch.g, brunch.b));
            Path brunchPath = new Path();
            ArrayList<Vector2D> brunchPoints = brunch.getDrawnPoints();
            brunchPath.moveTo(getXPixels(brunchPoints.get(0).getX()), getYPixels(brunchPoints.get(0).getY()));
            for (int j = 1; j < brunchPoints.size(); j++) {
                Vector2D point = brunchPoints.get(j);
                brunchPath.lineTo(getXPixels(point.getX()), getYPixels(point.getY()));
            }
            brunchPath.close();
            canvas.drawPath(brunchPath, m_brush);
        }
        m_brush.setColor(Color.rgb(41, 29, 5));
        double[] armPoints = george.findTangents();
        if (armPoints.length == 4) {
//            Log.d("MyCanvas", "arms x1 = "+ armPoints[0].getX()+ " y1 = "+armPoints[0].getY()+" x2 = "+armPoints[2].getX()+" y2 = "+armPoints[1].getY());
//            canvas.drawLine(getXPixels(george.getX()), getYPixels(george.getY()), getXPixels(george.getCurrentPoint().getX()), getYPixels(george.getCurrentPoint().getY()), m_brush);
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
        for (int i = 0; i < brunches.size(); i++) {
            BranchPoint brunch = brunches.get(i);
            if (!brunch.isCollapsed()) {
                m_brush.setColor(Color.rgb(94, 41, 158));
                canvas.drawCircle(getXPixels(brunch.getX()), getYPixels(brunch.getY()), (int) (0.16 * meterToPixels), m_brush);
                m_brush.setColor(Color.rgb(230, 247, 42));
                canvas.drawCircle(getXPixels(brunch.getX()), getYPixels(brunch.getY()), (int) (0.07 * meterToPixels), m_brush);
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
                george.drag(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (george.onDrag()) george.setPosition(x, y);
                break;
            case MotionEvent.ACTION_UP:
                george.jump();
                break;

        }
        return true;
    }
}
