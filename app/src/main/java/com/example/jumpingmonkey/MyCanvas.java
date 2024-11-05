package com.example.jumpingmonkey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class MyCanvas extends View {
    private final Paint m_brush;
    private final ArrayList<CanvasComponent> m_components;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_brush = new Paint();
        m_brush.setColor(Color.BLUE);
        m_components = new ArrayList<CanvasComponent>();
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < m_components.size(); i++) {
            m_components.get(i).draw();
        }
    }
}
