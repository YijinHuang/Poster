package com.seeu.poster.mWidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.seeu.poster.R;

/**
 * @author Gotcha
 * @date 2018/3/3
 */

public class RoundImageView extends ImageView {
    private TypedArray attr;
    private int radius;
    private boolean isCropped;

    private float width, height;
    private float imageWidth, imageHeight;
    private Path path;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //Get attributions from xml
        attr = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        radius = attr.getInt(R.styleable.RoundImageView_radius, 12);
        isCropped = attr.getBoolean(R.styleable.RoundImageView_isCropped, false);

        path = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        imageWidth = getDrawable().getIntrinsicWidth();
        imageHeight = getDrawable().getIntrinsicHeight();
    }

    //Use Canvas and Path to clip a rounded cotainer for image
    @Override
    protected void onDraw(Canvas canvas) {

        float beginX, beginY, endX, endY;
        float scale;

        if (isCropped) {
            beginX = beginY = 0;
            endX = width;
            endY = height;
        } else {
            //When the image's height-width ratio is smaller than origin container, the image's
            //width will match container's maximum width. So set the beginX 0.
            if (height / width > imageHeight / imageWidth) {
                //When the pixel of image larger than origin container, scale image to fit container
                if ((scale = width / imageWidth) < 1) {
                    imageHeight *= scale;
                }

                beginY = (height - imageHeight) / 2;
                beginX = 0;
                endY = beginY + imageHeight;
                endX = width;
            } else {
                if ((scale = height / imageHeight) < 1) {
                    imageWidth *= scale;
                }

                beginY = 0;
                beginX = (width - imageWidth) / 2;
                endY = height;
                endX = beginX + imageWidth;
            }
        }

        //clip the container by the path
        if (imageWidth > radius && imageHeight > radius) {
            path.moveTo(beginX + radius, beginY);
            path.lineTo(endX - radius, beginY);
            path.quadTo(endX, beginY, endX, beginY + radius);
            path.lineTo(endX, endY - radius);
            path.quadTo(endX, endY, endX - radius, endY);
            path.lineTo(beginX + radius, endY);
            path.quadTo(beginX, endY, beginX, endY - radius);
            path.lineTo(beginX, beginY + radius);
            path.quadTo(beginX, beginY, beginX + radius, beginY);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
