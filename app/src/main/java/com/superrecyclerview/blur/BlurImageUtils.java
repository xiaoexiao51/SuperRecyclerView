package com.superrecyclerview.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

/**
 * Created by PandaQ on 2016/9/7.
 * email : 767807368@qq.com
 * 模糊处理图像的工具类
 */
public class BlurImageUtils {

    /**
     * @param imageView   待处理的imageView
     * @param scaleFactor 模糊处理之前的进行采样缩小被处理图片大小的系数
     * @param blurf       模糊系数
     * @return 处理后的模糊图像
     */
    public static Bitmap blur(ImageView imageView, float scaleFactor, int blurf) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        return blur(bitmap, scaleFactor, blurf);
    }

    public static Bitmap blur(Bitmap bitmap, float scaleFactor, int blurf) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //创建一个长宽等比缩小的Bitmap
        Bitmap overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        //将canvas按照bitmap等量缩放，模糊处理的图片才能显示正常
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //对采样后的bitmap进行模糊处理，缩放采样后的图片处理比原图处理要省很多时间和内存开销
        overlay = FastBlur.doBlur(overlay, blurf, false);
        return overlay;
    }

    /**
     * 高斯模糊
     * 建议模糊度(在0.0到25.0之间)
     */
    private static final int BLUR_RADIUS = 20;
    private static final int SCALED_WIDTH = 100;
    private static final int SCALED_HEIGHT = 100;

    public static void blur(ImageView imageView, Bitmap bitmap) {
        blur(imageView, bitmap, BLUR_RADIUS);
    }

    public static void blur(ImageView imageView, Bitmap bitmap, int radius) {
        imageView.setImageBitmap(getBlurBitmap(imageView.getContext(), bitmap, radius));
    }

    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap) {
        return getBlurBitmap(context, bitmap, BLUR_RADIUS);
    }

    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, int radius) {
        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(radius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
