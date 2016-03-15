package de.hddesign.androidutils.androidutils.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import de.hddesign.androidutils.androidutils.R;

public class ColorView extends View implements OnTouchListener {

    public static long VALUE_SATURATION = 0;
    public static long HUE = 1;
    public static long SATURATION = 2;
    public static long VALUE = 3;
    public static long RED = 4;
    public static long GREEN = 5;
    public static long BLUE = 6;

    private float size, hueCircleStrokeWidth, indicatorRadius;

    private Paint hueCirclePaint, hueIndicatorPaint, trianglePaint, indicatorPaint, indicatorCirclePaint;
    private Path triangle;
    private RadialGradient saturationGradient, valueGradient;
    private PointF A, B, C, CENTER;

    private float red, green, blue;
    private float[] hsv;
    private ColorViewCallback colorViewCallback;

    public interface ColorViewCallback {
        void rgbChanged(int r, int g, int b);

        void hsvChanged(float hue, float saturation, float value);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorView, 0, 0);

        try {
            size = a.getDimension(R.styleable.ColorView_size, 200);
            hueCircleStrokeWidth = a.getDimension(R.styleable.ColorView_hueCircleStrokeWidth, 40);
            indicatorRadius = a.getDimension(R.styleable.ColorView_indicatorRadius, 20);
        } finally {
            a.recycle();
        }


        hsv = new float[] {0,0,0};

        hueCirclePaint = new Paint();
        hueCirclePaint.setStyle(Style.STROKE);
        hueCirclePaint.setAntiAlias(true);
        hueCirclePaint.setStrokeWidth(hueCircleStrokeWidth);

        hueIndicatorPaint = new Paint();
        hueIndicatorPaint.setStyle(Style.STROKE);
        hueIndicatorPaint.setAntiAlias(true);
        hueIndicatorPaint.setStrokeWidth(5);
        hueIndicatorPaint.setColor(0xFFFFFFFF);

        trianglePaint = new Paint();
        trianglePaint.setStyle(Style.FILL);
        trianglePaint.setAntiAlias(true);

        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Style.FILL);
        indicatorPaint.setAntiAlias(true);

        indicatorCirclePaint = new Paint();
        indicatorCirclePaint.setStyle(Style.STROKE);
        indicatorCirclePaint.setAntiAlias(true);
        indicatorCirclePaint.setStrokeWidth(2);
        indicatorCirclePaint.setColor(0xFFFFFFFF);

        CENTER = new PointF(size / 2, size / 2);
        
        buildHuePaint();

        triangle = buildTriangle();

        this.setOnTouchListener(this);
    }

    /***********************************************/
    /******************* DRAWING *******************/
    /***********************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHueCircle(canvas);
        canvas.rotate(hsv[0], canvas.getWidth() / 2, canvas.getHeight() / 2);
        drawSelectionTriangle(canvas);
        drawCurrentHueIndicator(canvas);
        drawIndicatorCircle(canvas);
        canvas.rotate(360 - hsv[0], canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    private void drawHueCircle(Canvas canvas) {
        canvas.drawCircle(size / 2, size / 2, size / 2 - hueCircleStrokeWidth / 2, hueCirclePaint);
    }

    private void drawSelectionTriangle(Canvas canvas) {
        canvas.drawPath(triangle, buildTrianglePaint(SATURATION));
        canvas.drawPath(triangle, buildTrianglePaint(VALUE));
        canvas.drawPath(triangle, buildTrianglePaint(VALUE_SATURATION));
    }

    private void drawCurrentHueIndicator(Canvas canvas) {
        canvas.rotate(90, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawLine(size / 2, 0, size / 2, 0 + hueCircleStrokeWidth, hueIndicatorPaint);
        canvas.rotate(-90, canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    private void drawIndicatorCircle(Canvas canvas) {
        //calc two points with values from value
        PointF V1 = pointBetweenPoints(B, C, ((distanceBetweenPoints(A, C) / 100 * hsv[2] * 100)));
        PointF V2 = pointBetweenPoints(B, A, ((distanceBetweenPoints(A, C) / 100 * hsv[2] * 100)));

        //calc third point using saturation value
        PointF indicatorPoint = pointBetweenPoints(V2, V1, ((distanceBetweenPoints(V1, V2)) - (distanceBetweenPoints(V1, V2) / 100 * hsv[1] * 100)));

        indicatorPaint.setColor(Color.HSVToColor(hsv));

        canvas.drawCircle(indicatorPoint.x, indicatorPoint.y, indicatorRadius, indicatorPaint);
        canvas.drawCircle(indicatorPoint.x, indicatorPoint.y, indicatorRadius, indicatorCirclePaint);
    }

    private void buildHuePaint() {
        int[] colors = new int[7];
        colors[0] = 0xFFFF0000;
        colors[1] = 0xFFFFFF00;
        colors[2] = 0xFF00FF00;
        colors[3] = 0xFF00FFFF;
        colors[4] = 0xFF0000FF;
        colors[5] = 0xFFFF00FF;
        colors[6] = 0xFFFF0000;

        SweepGradient sweepGradient = new SweepGradient(size / 2, size / 2, colors, null);

        hueCirclePaint.setDither(true);
        hueCirclePaint.setShader(sweepGradient);
    }

    private Paint buildTrianglePaint(long type) {
        Paint tmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tmpPaint.setStyle(Style.FILL);
        tmpPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        tmpPaint.setDither(true);

        if (type == SATURATION) {
            tmpPaint.setShader(saturationGradient);
        } else if (type == VALUE) {
            tmpPaint.setShader(valueGradient);
        } else {
            RadialGradient saturationValueGradient = new RadialGradient(A.x, A.y, (float) ((size / 2 - hueCircleStrokeWidth) * 1.5), Color.HSVToColor(new float[]{hsv[0], 1, 1}), 0x00FFFFFF, TileMode.CLAMP);
            tmpPaint.setShader(saturationValueGradient);
        }

        return tmpPaint;
    }

    private Path buildTriangle() {
        A = new PointF();
        B = new PointF();
        C = new PointF();

        double cos120 = Math.cos(Math.toRadians(120));
        double sin120 = Math.sin(Math.toRadians(120));

        double cos240 = Math.cos(Math.toRadians(240));
        double sin240 = Math.sin(Math.toRadians(240));

        float r = size / 2 - hueCircleStrokeWidth;

        A = new PointF(r, 0);

        B.x = (float) (A.x * cos120 - A.y - sin120);
        B.y = (float) (A.x * sin120 - A.y - cos120);

        C.x = (float) (A.x * cos240 - A.y - sin240);
        C.y = (float) (A.x * sin240 - A.y - cos240);

        A = calcScreenCoordinates(CENTER, A);
        B = calcScreenCoordinates(CENTER, B);
        C = calcScreenCoordinates(CENTER, C);

        Path path = new Path();
        path.setFillType(FillType.EVEN_ODD);
        path.moveTo(A.x, A.y);
        path.lineTo(B.x, B.y);
        path.lineTo(C.x, C.y);
        path.lineTo(A.x, A.y);
        path.close();

        saturationGradient = new RadialGradient(C.x, C.y, r * 2, 0xFFFFFFFF, 0x00FFFFFF, TileMode.CLAMP);
        valueGradient = new RadialGradient(B.x, B.y, r * 2, 0xFF000000, 0x00000000, TileMode.CLAMP);

        return path;
    }

    /***********************************************/
    /************** COLOR OPERATIONS ***************/
    /***********************************************/

    private float[] calcCurrentHSV() {
        Color.RGBToHSV((int) red, (int) green, (int) blue, hsv);
        return hsv;
    }

    private void calcRGBfromCurrentHSV() {
        float hue = hsv[0];
        hue /= 360;

        float saturation = hsv[1];
        float value = hsv[2];

        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0:
            case 6:
                red = value;
                green = t;
                blue = p;
                break;
            case 1:
                red = q;
                green = value;
                blue = p;
                break;
            case 2:
                red = p;
                green = value;
                blue = t;
                break;
            case 3:
                red = p;
                green = q;
                blue = value;
                break;
            case 4:
                red = t;
                green = p;
                blue = value;
                break;
            case 5:
                red = value;
                green = p;
                blue = q;
                break;
        }

        red *= 255;
        green *= 255;
        blue *= 255;

        if (colorViewCallback != null)
            colorViewCallback.rgbChanged((int) red, (int) green, (int) blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = (int) size;
        int desiredHeight = (int) size;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF eventP = new PointF(event.getX(), event.getY());
        
        if(isPointInTriangle(eventP, A,B,C)) {
            double angle = Math.toRadians(360 - hsv[0]);

            float x1 = eventP.x;
            float y1 = eventP.y;

            float x1r = (float) ((x1 - CENTER.x) * Math.cos(angle) - (y1 - CENTER.y) * Math.sin(angle) + CENTER.x);
            float y1r = (float) ((x1 - CENTER.y) * Math.sin(angle) + (y1 - CENTER.y) * Math.cos(angle) + CENTER.y);

            PointF translatedEventP = new PointF(x1r, y1r);

            PointF pointInLongDistanceToEventP = new PointF();

            pointInLongDistanceToEventP.x = (float) (translatedEventP.x - (1000 / Math.sqrt((((B.x - translatedEventP.x) * ((B.x - translatedEventP.x))) + ((B.y - translatedEventP.y) * ((B.y - translatedEventP.y)))))) * (B.x - translatedEventP.x));
            pointInLongDistanceToEventP.y = (float) (translatedEventP.y - (1000 / Math.sqrt((((B.x - translatedEventP.x) * ((B.x - translatedEventP.x))) + ((B.y - translatedEventP.y) * ((B.y - translatedEventP.y)))))) * (B.y - translatedEventP.y));

            PointF saturationIntersect = getLineLineIntersection(B.x, B.y,pointInLongDistanceToEventP.x, pointInLongDistanceToEventP.y ,C.x, C.y, A.x, A.y);

            hsv[1] = distanceBetweenPoints(saturationIntersect, C) / distanceBetweenPoints(A, C);
            hsv[2] = distanceBetweenPoints(B, translatedEventP) / distanceBetweenPoints(B, saturationIntersect);

            if(colorViewCallback != null)
                colorViewCallback.hsvChanged(hsv[0], hsv[1] * 100, hsv[2] * 100);

            this.invalidate();
            return true;
        } else if (isPointOnHueWheel(eventP)){
            PointF a = eventP;
            PointF b = new PointF(a.x, 0);

            double sideA = distanceBetweenPoints(b, CENTER);
            double sideB = distanceBetweenPoints(a, CENTER);
            double sideC = distanceBetweenPoints(a, b);

            double angleAlpha = Math.toDegrees(Math.acos(((sideA * sideA) - (sideC * sideC) - (sideB * sideB)) / (-2 * (sideB * sideC))));

            float originXFromCenter = a.x - (size / 2);
            float originYFromCenter = a.y - (size / 2);

            if (originXFromCenter < 0 && originYFromCenter > 0)
                angleAlpha += 90;

            if (originXFromCenter < 0 && originYFromCenter < 0)
                angleAlpha = 270 + (angleAlpha - 180);

            if (originXFromCenter > 0 && originYFromCenter < 0)
                angleAlpha = 270 + (180 - angleAlpha);

            if (originXFromCenter > 0 && originYFromCenter > 0)
                angleAlpha = 90 - angleAlpha;

            hsv[0] = ((float) angleAlpha);

            if(colorViewCallback != null)
                colorViewCallback.hsvChanged(hsv[0], hsv[1] * 100, hsv[2] * 100);

            Log.d("ColorView", String.format("onTouch: %f", hsv[0]));

            this.invalidate();
            return true;
        }

        return false;
    }

    private boolean isPointOnHueWheel(PointF eventP) {
        return distanceBetweenPoints(eventP, CENTER) > size/2 - hueCircleStrokeWidth/2 && distanceBetweenPoints(eventP, CENTER) < size/2 + hueCircleStrokeWidth/2;
    }

    /***********************************************/
    /*************** POINT OPERATIONS **************/
    /***********************************************/

    public static PointF getLineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double det1And2 = det(x1, y1, x2, y2);
        double det3And4 = det(x3, y3, x4, y4);
        double x1LessX2 = x1 - x2;
        double y1LessY2 = y1 - y2;
        double x3LessX4 = x3 - x4;
        double y3LessY4 = y3 - y4;
        double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
        if (det1Less2And3Less4 == 0){
            // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
            return null;
        }
        double x = (det(det1And2, x1LessX2,
                det3And4, x3LessX4) /
                det1Less2And3Less4);
        double y = (det(det1And2, y1LessY2,
                det3And4, y3LessY4) /
                det1Less2And3Less4);
        return new PointF(((float) x), ((float) y));
    }

    protected static double det(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    private PointF calcScreenCoordinates(PointF center, PointF point) {
        return new PointF(point.x + center.x, center.y - point.y);
    }

    public float distanceBetweenPoints(PointF p1, PointF p2) {
        return (float) Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y)));
    }

    private PointF pointBetweenPoints(PointF a, PointF b, float distance) {
        float x;
        float y;

        double angleAB = Math.atan2((b.x - a.x), (b.y - a.y));
        double delta_x_AC = distance * Math.sin(angleAB);
        double delta_y_AC = distance * Math.cos(angleAB);

        x = (float) (a.x + delta_x_AC);
        y = (float) (a.y + delta_y_AC);

        return new PointF(x, y);
    }

    private boolean isPointInTriangle(PointF p, PointF p0, PointF p1, PointF p2) {
        double angle = Math.toRadians(hsv[0]);

        float x1 = p0.x;
        float y1 = p0.y;

        float x2 = p1.x;
        float y2 = p1.y;

        float x3 = p2.x;
        float y3 = p2.y;

        float x1r = (float) ((x1 - CENTER.x) * Math.cos(angle) - (y1 - CENTER.y) * Math.sin(angle) + CENTER.x);
        float y1r = (float) ((x1 - CENTER.x) * Math.sin(angle) + (y1 - CENTER.y) * Math.cos(angle) + CENTER.y);

        float x2r = (float) ((x2 - CENTER.x) * Math.cos(angle) - (y2 - CENTER.y) * Math.sin(angle) + CENTER.x);
        float y2r = (float) ((x2 - CENTER.x) * Math.sin(angle) + (y2 - CENTER.y) * Math.cos(angle) + CENTER.y);

        float x3r = (float) ((x3 - CENTER.x) * Math.cos(angle) - (y3 - CENTER.y) * Math.sin(angle) + CENTER.x);
        float y3r = (float) ((x3 - CENTER.x) * Math.sin(angle) + (y3 - CENTER.y) * Math.cos(angle) + CENTER.y);

        float dX = p.x - x3r;
        float dY = p.y - y3r;
        float dX21 = x3r - x2r;
        float dY12 = y2r - y3r;
        float D = dY12 * (x1r - x3r) + dX21 * (y1r - y3r);
        float s = dY12 * dX + dX21 * dY;
        float t = (y3r- y1r) * dX + (x1r - x3r) * dY;
        if (D < 0) return s <= 0 && t <= 0 && s + t >= D;
        return s >= 0 && t >= 0 && s + t <= D;
    }

    /***********************************************/
    /*************** GETTER & SETTER ***************/
    /***********************************************/

    public void setColorViewCallback(ColorViewCallback colorViewCallback) {
        this.colorViewCallback = colorViewCallback;
    }

    public int getCurrentColor() {
        return Color.HSVToColor(hsv);
    }

    public void setHSV(int hue, int saturation, int value) {
        this.hsv[0] = hue;
        this.hsv[1] = saturation / 100f;
        this.hsv[2] = value / 100f;
        calcRGBfromCurrentHSV();
        this.invalidate();
    }

    public void setRed(int red) {
        this.red = red;
        calcCurrentHSV();
        this.invalidate();
        if (colorViewCallback != null)
            colorViewCallback.hsvChanged(hsv[0], hsv[1] * 100, hsv[2] * 100);
    }

    public void setGreen(int green) {
        this.green = green;
        calcCurrentHSV();
        this.invalidate();
        if (colorViewCallback != null)
            colorViewCallback.hsvChanged(hsv[0], hsv[1] * 100, hsv[2] * 100);
    }

    public void setBlue(int blue) {
        this.blue = blue;
        calcCurrentHSV();
        this.invalidate();
        if (colorViewCallback != null)
            colorViewCallback.hsvChanged(hsv[0], hsv[1] * 100, hsv[2] * 100);
    }
}
