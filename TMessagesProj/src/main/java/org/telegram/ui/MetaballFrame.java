package org.telegram.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.NonNull;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.Utilities;

public class MetaballFrame extends View {
    private Paint paint;
    private Path path;
    private Path circlePath;
    private float avatarOriginSize, avatarStartedScale, paddingTop;
    private float animProgress = 0;
    private Bitmap blurredBitmap;


    private float a = 1f, b = -1f;


    public MetaballFrame(Context context) {
        super(context);
    }

    public MetaballFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(float avatarOriginSize, float avatarStartedScale, float paddingTop) {
        this.avatarOriginSize = avatarOriginSize;
        this.avatarStartedScale = avatarStartedScale;
        this.paddingTop = paddingTop;


        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        circlePath = new Path();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setImageReceiver(ImageReceiver imageReceiver) {
        if (imageReceiver != null && imageReceiver.getBitmap() != null && !imageReceiver.getBitmap().isRecycled()) {
            blurredBitmap = Utilities.stackBlurBitmapMax(imageReceiver.getBitmap(), (int) (avatarOriginSize * avatarStartedScale), (int) (avatarOriginSize * avatarStartedScale), true);
        }
    }


    public void setAnimationProgress(float progress) {
        this.animProgress = progress;
        b = AndroidUtilities.lerp(-1.0f, 2f, customSpeed(animProgress, 0.15f));
        a = 1f;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float layoutWith = getWidth();

        float scale = avatarOriginSize / 2f;

        float offsetX = layoutWith / 2f;
        float offsetY = avatarOriginSize / 2f;

        float yMin = -2f;
        float yMax = 2f;
        float step = 0.05f;

        path.reset();
        boolean first = true;

        // Right half: x = +√(y³ + ay + b)
        for (float y = yMin; y <= yMax; y += step) {
            float x2 = y * y * y + a * y + b;
            if (x2 < 0) continue;
            float x = (float) Math.sqrt(x2);

            float drawX = offsetX + x * scale;
            float drawY = offsetY - y * scale;

            if (first) {
                path.moveTo(drawX, drawY);
                first = false;
            } else {
                path.lineTo(drawX, drawY);
            }
        }

        // Left half: x = -√(...)
        for (float y = yMax; y >= yMin; y -= step) {
            float x2 = y * y * y + a * y + b;
            if (x2 < 0) continue;
            float x = (float) -Math.sqrt(x2);

            float drawX = offsetX + x * scale;
            float drawY = offsetY - y * scale;

            path.lineTo(drawX, drawY);
        }
        path.close();
        paint.setColor(Color.BLACK);
        canvas.drawPath(path, paint);
        if (blurredBitmap != null && animProgress > 0.0001f && animProgress < 1.0001f) {
            float radius = AndroidUtilities.lerp(avatarOriginSize * avatarStartedScale / 2f, 0f, animProgress);
            float centerX = (layoutWith) / 2;
            float centerY = AndroidUtilities.lerp(paddingTop + radius, 0, animProgress);
            circlePath.addCircle(centerX, centerY, radius, Path.Direction.CW);
            canvas.save();
            canvas.clipPath(circlePath);
            if (radius < avatarOriginSize * 0.3f) {
                return;
            }
            Bitmap scaled = Bitmap.createScaledBitmap(blurredBitmap, (int) (radius * 2), (int) (radius * 2), true);
            canvas.drawBitmap(scaled, centerX - radius, centerY - radius, null);
            canvas.restore();
        }
    }

    public static float customSpeed(float progress, float firstPointChangeSpeed) {
        if (progress <= firstPointChangeSpeed) {
            return Math.min(1f, progress / firstPointChangeSpeed);
        } else {
            return Math.min(1f, 1f - ((progress - firstPointChangeSpeed) / (1f - firstPointChangeSpeed)));
        }
    }
}