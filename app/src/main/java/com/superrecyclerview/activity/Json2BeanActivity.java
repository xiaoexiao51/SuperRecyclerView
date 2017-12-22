package com.superrecyclerview.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.decoration.DividerDecoration;
import com.superrecyclerview.expandable.bean.RecyclerViewData;
import com.superrecyclerview.expandable.sample.Json2Bean;
import com.superrecyclerview.expandable.sample.Json2BeanAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/22.
 */
public class Json2BeanActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.ll_controller)
    LinearLayout mLinearLayout;

    private List<RecyclerViewData> mDatas = new ArrayList<>();
    private Json2BeanAdapter mJson2BeanAdapter;

    @Override
    protected int getViewId() {
        return R.layout.activity_json_bean;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showSuccessStateLayout();
        //获取数据
        initJsons();
        initRecyclerView();
    }

    private void initJsons() {
        // 原始json字符串
        String jsonData = "{\"白马寺\":[{\"name\":\"释永信\",\"bytalkjid\":\"1510108537217507904\",\"simiao\":\"白马寺\",\"address\":\"河南省洛阳市老城以东12公里\",\"phoneno\":\"18211993372\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171219/9025213caa410f4b01261cb580e79fae.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 19, 2017 10:21:55 AM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 19, 2017 10:21:55 AM\",\"pageSize\":20},{\"name\":\"印乐\",\"bytalkjid\":\"1510047259167507904\",\"simiao\":\"白马寺\",\"address\":\"河南省洛阳市老城以东12公里\",\"phoneno\":\"18211993371\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171108/91f25858a194b90fcdf4de7b1cea17a7.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 19, 2017 10:21:55 AM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 19, 2017 10:21:55 AM\",\"pageSize\":20},{\"name\":\"法海\",\"bytalkjid\":\"1513330325581507904\",\"simiao\":\"白马寺\",\"address\":\"河南省洛阳市老城以东12公里\",\"phoneno\":\"18501592222\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 19, 2017 10:21:55 AM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 19, 2017 10:21:55 AM\",\"pageSize\":20},{\"name\":\"释小龙\",\"bytalkjid\":\"1511942700462507904\",\"simiao\":\"白马寺\",\"address\":\"河南省洛阳市老城以东12公里\",\"phoneno\":\"18211993377\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171218/e702fac3dd4800abb014c42c1f28a44c.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 19, 2017 10:21:55 AM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 19, 2017 10:21:55 AM\",\"pageSize\":20}],\"灵隐寺\":[{\"name\":\"经他\",\"bytalkjid\":\"1512110427103507904\",\"simiao\":\"灵隐寺\",\"address\":\"浙江省杭州市\",\"phoneno\":\"18501592670\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171201/cb7e0ae07c9c06abc85b6af8c36c893c.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 19, 2017 10:21:55 AM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 19, 2017 10:21:55 AM\",\"pageSize\":20}]}";

        // 对json进行加工
        jsonData = jsonData.replaceAll("\\\"[\\u4e00-\\u9fa5]{1,9}\\\"\\:", "\"fashi\":");
        jsonData = jsonData.replaceAll("\\]\\,", "]},{");
        jsonData = "[" + jsonData + "]";

        // 封装javabean
        List<Json2Bean> json2Bean = new Gson().fromJson(jsonData, new TypeToken<List<Json2Bean>>() {
        }.getType());
        // 初始化数据Beans
        initJson2Beans(json2Bean);
    }

    private void initJson2Beans(List<Json2Bean> json2Bean) {

        for (int i = 0; i < json2Bean.size(); i++) {

            List<Json2Bean.FashiBean> fashi = json2Bean.get(i).getFashi();
            List<Json2Bean.FashiBean> bean1 = new ArrayList<>();
            String simiao = "";
            for (int j = 0; j < fashi.size(); j++) {
                simiao = fashi.get(j).getSimiao();
                bean1.add(fashi.get(j));
            }
            mDatas.add(new RecyclerViewData(simiao, bean1, true));
        }
    }

    private void initRecyclerView() {
        // 1、创建管理器和适配器
        LinearLayoutManager manager = new LinearLayoutManager(this);
//        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mJson2BeanAdapter = new Json2BeanAdapter(this, mDatas);
//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//
//                int itemViewType = mBookAdapter.getItemViewType(position);
//                if (itemViewType == BaseViewHolder.VIEW_TYPE_PARENT) {
//                    return 2;// 如果是标题，则占两个单元格
//                }
//                return 1;// 如果是内容，则占一个单元格
//            }
//        });
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mJson2BeanAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // 3、设置分割线
//        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(this, 1));
//        decoration.setPaddingStart(true);
//        decoration.setPaddingEdgeSide(true);
//        decoration.setPaddingHeaderFooter(true);
//        decoration.isGroupRecyclerView(true);
//        mRecyclerView.addItemDecoration(decoration);

        DividerDecoration decoration1 = new DividerDecoration(ContextCompat.getColor(this, R.color.deep_line), 2);
        decoration1.setDrawLastItem(false);
        decoration1.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(decoration1);

//        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.HORIZONTAL,
//                dip2px(this, 1), ContextCompat.getColor(this, R.color.color_bg)));

        // 4、设置监听事件
        mJson2BeanAdapter.setOnLongClickListener(new Json2BeanAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View itemView, int position) {
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getMasterjids(View view) {
        String masterjids = mJson2BeanAdapter.getMasterjids();
        ((TextView) findViewById(R.id.tv_ids)).setText(masterjids);
    }

    public void cancelSelect(View view) {
        mJson2BeanAdapter.setMultiCheckMode(false);
        mLinearLayout.setVisibility(View.GONE);
    }

    public void allSelect(View view) {
        mJson2BeanAdapter.setMultiCheckMode(true);
    }
}
