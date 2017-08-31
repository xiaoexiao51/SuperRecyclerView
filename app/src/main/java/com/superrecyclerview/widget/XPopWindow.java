package com.superrecyclerview.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.utils.ScreenUtils;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 * Context传进来的上下文最好是Activity，才会有阴影效果。
 */

public class XPopWindow extends PopupWindow {

    private List<String> items;
    private View conentView;
    private Context context;
    private ListView listView;
    int[] location = new int[2];

//    private static XPopWindow sXPopWindow;
//
//    public static XPopWindow getInstance(Context context, List<String> items) {
//        if (sXPopWindow == null) {
//            synchronized (XPopWindow.class) {
//                if (sXPopWindow == null) {
//                    sXPopWindow = new XPopWindow(context.getApplicationContext(), items);
//                }
//            }
//        }
//        return sXPopWindow;
//    }

    public XPopWindow(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.pop_menu, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopWindowBotStyle);
        this.listView = (ListView) conentView.findViewById(R.id.list_view);

        //设置适配器
        this.listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Object getItem(int position) {
                return items.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.pop_menu_item, null);
                view.setText(items.get(position));
                return view;
            }
        });
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowing()) {
                    dismiss();
                }
                onItemListener.OnItemListener(position, items.get(position));
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    public void setPopWidth(int popWidth) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.width = popWidth;
        listView.setLayoutParams(params);
    }

    public void showPopupWindow(View parent, int xoff, int yoff) {
        //获取控件位置坐标
        parent.getLocationOnScreen(location);
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //使用背景模糊效果
//        backgroundAlpha(0.5f);
        if (location[1] > ScreenUtils.getScreenHeight(context) / 2) {
            this.setAnimationStyle(R.style.PopWindowTopStyle);
            int measuredHeight = conentView.getMeasuredHeight() * items.size();
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - xoff,
                    location[1] - measuredHeight - yoff);
        } else {
            //动画方式一：
            this.setAnimationStyle(R.style.PopWindowBotStyle);
            this.showAsDropDown(parent, xoff, yoff);
            //动画方式二：
//            this.showAsDropDown(parent, xoff, yoff);
//            int measuredHeight = conentView.getMeasuredHeight() * items.size()
//                    + CommonUtils.dip2px(context, 20);
//            AnimHelper.doMoveVertical(conentView, -measuredHeight, 0, 500);
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        try {
            ((Activity) context).getWindow().getDecorView().setAlpha(bgAlpha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnItemListener onItemListener;

    public interface OnItemListener {
        void OnItemListener(int position, String item);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
}