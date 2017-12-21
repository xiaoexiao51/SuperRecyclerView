package com.superrecyclerview.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 常用工具类
 * <p>
 * Created by lilei on 2017/5/5.
 */
public class CommonUtils {

    private CommonUtils() {
        throw new RuntimeException("CommonUtils cannot be initialized!");
    }

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    //1、在子线程中执行任务
    public static void runInThread(Runnable task) {
        threadPool.execute(task);
    }

    //2、创建handler对象,  Looper.getMainLooper()获取主线程里面的Looper对象
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return handler;
    }

    //3、在主线程执行任务
    public static void runOnUIThread(Runnable task) {
        handler.post(task);
    }

    //4、浮点数的估值
    public static Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    //5、颜色估值方法
    public static int evaluateArgb(float fraction, Object startValue, Object endValue) {

        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }

    //6、颜色估值方法
    public static Object evaluateColor(float fraction, Object startValue, Object endValue) {

        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }


    public static int getTextColor(int generateRandomColor) {

        int red = Color.red(generateRandomColor);
        int green = Color.green(generateRandomColor);
        int blue = Color.blue(generateRandomColor);
        int argColor = (red + green + blue) / 3;
        //背景颜色很浅
        if (argColor > 255 * 0.5) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }

    }

    // 当背景颜色很深的时候，TextColor：白色
    private static Random mRandom = new Random();

    // 获取随机色
    public static int getRandomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    // 生成漂亮的颜色
    public static int getBeautifulColor() {
        Random random = new Random();
        //为了让生成的颜色不至于太黑或者太白，所以对3个颜色的值进行限定
        int red = random.nextInt(150) + 50;//50-200
        int green = random.nextInt(150) + 50;//50-200
        int blue = random.nextInt(150) + 50;//50-200
        return Color.rgb(red, green, blue);//使用r,g,b混合生成一种新的颜色
    }

    // 常用单位换算
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 检查SD卡状态
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(flag);
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 检查网络状态
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 网络类型
     */
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 补间动画
     *
     * @param view
     */
    public static void startAnimation(View view) {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(0.5f, 1, 0.5f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);
        as.addAnimation(sa);
        as.setDuration(500);
        view.startAnimation(as);
    }

    public static void startSearchAnim(View view) {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);
        as.addAnimation(sa);
        as.setDuration(500);
        view.startAnimation(as);
    }

    public static void startAnim2Right(View view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationX", CommonUtils.dp2px(view.getContext(), 40));
        oa.setInterpolator(new OvershootInterpolator());
        oa.setDuration(200);
        oa.start();
    }

    public static void startAnim2Left(View view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationX", CommonUtils.dp2px(view.getContext(), 0));
        oa.setInterpolator(new OvershootInterpolator());
        oa.setDuration(200);
        oa.start();
    }

    public static void startAnim2Top(View view, int translationY) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationY", -CommonUtils.dp2px(view.getContext(), translationY));
        oa.setDuration(200);
        oa.start();
    }

    public static void startAnim2Bottom(View view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationY", CommonUtils.dp2px(view.getContext(), 0));
        oa.setDuration(200);
        oa.start();
    }

    /**
     * 收藏图标动画
     *
     * @param view
     */
    public static void startColectScale(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1.2f, 1f, 0.5f, 0.7f, 1f);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1.2f, 1f, 0.5f, 0.7f, 1f);
        set.play(animator_x).with(animator_y);
        set.setDuration(500);
        set.start();
    }

    /**
     * webview操作栏动画
     *
     * @param view
     */
    public static void startWebBar2Bottom(View view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationY", CommonUtils.dp2px(view.getContext(), 100));
        oa.setDuration(200);
        oa.start();
    }

    public static void startWebBar2Up(View view) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "translationY", CommonUtils.dp2px(view.getContext(), 0));
        oa.setDuration(200);
        oa.start();
    }

    /**
     * 录音泡泡条动画
     *
     * @param view
     */
    public static void startRecBubble(View view) {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);
        as.addAnimation(sa);
        as.setDuration(200);
        view.startAnimation(as);
    }

    /**
     * 小视频播放器动画
     *
     * @param view
     */
    public static void startVideoPlayOut(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.2f, 1f, 0.5f, 0.0f);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.2f, 1f, 0.5f, 0.0f);
        set.play(animator_x).with(animator_y);
        set.setDuration(200);
        set.start();
    }

    public static void startVideoPlayIn(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 0.5f, 1f, 1.2f, 1.0f);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 0.5f, 1f, 1.2f, 1.0f);
        set.play(animator_x).with(animator_y);
        set.setDuration(200);
        set.start();
    }

    /**
     * 格式化金钱，保留小数点后两位
     *
     * @param value
     * @return
     */
    public static String formatMoney(String value) {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        return formatter.format(value);
    }

    /**
     * 格式化数字，保留小数点后两位
     *
     * @param value
     * @return
     */
    public static String formatValue(double value) {
        DecimalFormat formatter = new DecimalFormat("###.##");
        return formatter.format(value);
    }

    /**
     * 设置ListView的最大高度
     */
//    private void setListViewHeight(ListView listView, List<Object> contacts, int size, int height) {
//        if (contacts.size() > size) {
//            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
//            layoutParams.height = CommonUtils.dip2px(context, height);
//            listView.setLayoutParams(layoutParams);
//        }
//    }
    public static String formatWeek(String week) {
        if (!StringUtils.isNull(week)) {
            if (StringUtils.isEqual(week, "1234567")) {
                return "每天";
            } else if (StringUtils.isEqual(week, "12345")) {
                return "工作日";
            } else if (StringUtils.isEqual(week, "67")) {
                return "周末";
            } else {
                return String.format("周%s", week);
            }
        }
        return null;
    }


//    public static void copy(Context context, String text) {
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
//            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            clipboard.setText(text);
//        } else {
//            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            android.content.ClipData clip = android.content.ClipData.newPlainText(
//                    text, text);
//            clipboard.setPrimaryClip(clip);
//        }
//    }

    public static void setCopyable(final Context context, final TextView textView) {
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CommonUtils.copy(context, textView.getText().toString().trim());
                MessageUtils.showInfo(context, "复制成功");

//                String[] chooseList = new String[]{"复制"};
//                MessageUtils.showChoose(context, chooseList, new ChooseDialog.OnChooseResultListener() {
//                    @Override
//                    public void onChooseResult(int position, String choose) {
//                        if (choose == "复制") {
//                            Utils.copy(context, textView.getText().toString().trim());
//                            MessageUtils.showInfo(context, "复制成功");
//                        }
//                    }
//                });
                return true;
            }
        });
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(Context context, String content) {
        // 获取剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        // 开启手机震动
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 200}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        long durationTime = currentTime - lastClickTime;
        lastClickTime = currentTime;
        return durationTime <= 1000;
    }

    public static String hanZiToPinyin(String hanZi) {
        StringBuilder sb = new StringBuilder();
        HanyuPinyinOutputFormat format = null;
        //如果该汉字为空
        if (StringUtils.isNull(hanZi)) {
            sb.append("#");
            return sb.toString();
        }
        //取出字符串里面的每一个字符
        char[] chars = hanZi.toCharArray();
        if (format == null) {
            //设置输出的一个配置
            format = new HanyuPinyinOutputFormat();
            //设置大写
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            //取消声调
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        }
        for (char aChar : chars) {
            //去掉空格
            if (Character.isWhitespace(aChar)) {
                continue;
            }
            if (Character.toString(aChar).matches("[\\u4E00-\\u9FA5]")) {
                //是中文
                try {
                    //把汉字转换成拼音
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, format)[0];
                    sb.append(pinyin);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            } else {
                //不是文字
                if (Character.isLetter(aChar)) {
                    //是字母
                    sb.append(Character.toUpperCase(aChar));
                } else {
                    //非法字符，看不懂：#$%$%#^$
                    sb.append("#");
                }
            }
        }
        return sb.toString();
    }
}
