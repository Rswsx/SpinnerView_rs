package com.example.kanceyuanyaoganfenyuan.spinnerview_rs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanceyuanyaoganfenyuan on 18/4/2.
 */

public class SpinnerView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private boolean textCanEdit = false;  //标示spinner是否可以自由输入
    private ListViewAdap listViewAdap;
    private PopupWindow popupWindow;
     private List<String> listData = new ArrayList<>();
    private itemSelectedListener mItemSelectedListener;
    private int listHeight = 0;

    public EditText mSpinnerText;
    public ImageView mSpinnerImag;
    public View dropV;

    public SpinnerView(Context context) {
        super(context);
        this.context = context;
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null, true);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, layoutParams);
        mSpinnerText = (EditText) view.findViewById(R.id.spinnerTextV);
        mSpinnerImag = (ImageView) view.findViewById(R.id.spinnerImg);
        //在这里初始化下拉列表的layout，便于自定义修改其背景
        dropV =  LayoutInflater.from(context).inflate(R.layout.spinner_down_layout, null);
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setData(List<String> data) {
        listData = new ArrayList<>();
        listData.addAll(data);
        listViewAdap = new ListViewAdap(context, listData);
        //计算listview的高度
        View listLayout = LayoutInflater.from(context).inflate(R.layout.list_item_layout, null);
        View listV = listLayout.findViewById(R.id.listTextV);
        ViewGroup.LayoutParams layoutParams = listV.getLayoutParams();
        listHeight = layoutParams.height * data.size();
        //默认设置spinnerTextView不可输入
        mSpinnerText.setFocusable(false);
        mSpinnerText.setFocusableInTouchMode(false);
        mSpinnerText.setText(data.get(0));
        mSpinnerText.setOnClickListener(this);
        mSpinnerText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (textCanEdit)
                    //设置光标可见
                    mSpinnerText.setCursorVisible(true);
                else
                    mSpinnerText.setCursorVisible(false);
                return false;
            }
        });
        mSpinnerImag.setOnClickListener(this);

    }

    public void onClick(View v) {
        showSpinnerDropV(v);
    }

    //刷新listview的数据
    public void refreshListView(List<String> newData) {
        listData.clear();
        listData.addAll(newData);
        mSpinnerText.setText(listData.get(0));
        listViewAdap.notifyDataSetChanged();
    }


    //设置spinner的textview是否可以编辑
    public void setSpinnerTextFocusable(boolean focusable) {
        textCanEdit = focusable;
        if (focusable) {
            mSpinnerText.setFocusableInTouchMode(true);
            mSpinnerText.setFocusable(true);
            mSpinnerText.requestFocus();
            mSpinnerText.setOnClickListener(null);
        } else {
            mSpinnerText.setFocusable(false);
            mSpinnerText.setFocusableInTouchMode(false);
            mSpinnerText.setOnClickListener(this);
        }
    }
    //获得spinner的值
    public String getValue(){
        return mSpinnerText.getText().toString();
    }
    //直接设置spinner的值
    public void setValue(String value){
        mSpinnerText.setText(value);
    }

    //设置listview的高度
    public void setListViewH(int listHeight) {
        this.listHeight = listHeight;
    }
    //设置选中项
    public void setSelection(int index){
        if(index<listData.size()) {
            String selectValue = listData.get(index);
            mSpinnerText.setText(selectValue);
            if (textCanEdit) {
                mSpinnerText.setSelection(selectValue.length());    //将光标移至文字末尾
                mSpinnerText.setCursorVisible(false);
            } else
                mSpinnerText.setCursorVisible(false);

            if (mItemSelectedListener != null) {
                mItemSelectedListener.onItemSelected(selectValue);
            }
        }
    }
    public void setItemSelectedListener(itemSelectedListener listener) {
        this.mItemSelectedListener = listener;
    }

    //旋转箭头
    private void rotationJt(float startAngle, float endAngle) {
        //旋转箭头
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSpinnerImag, "rotation", startAngle, endAngle);
        animator.setDuration(150);
        animator.start();
    }

    public void showSpinnerDropV(View v) {
        if (popupWindow == null) {
            rotationJt(0f, 180f);
            // 实例化listView
            ListView   dropListV = (ListView) dropV.findViewById(R.id.spinnerList);
            // 设置listView的适配器
            dropListV.setAdapter(listViewAdap);
            // 实例化一个PopuWindow对象
            popupWindow = new PopupWindow(v);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rotationJt(180f, 0f);
                }
            });
            // 设置弹框的宽度为布局文件的宽
            popupWindow.setWidth(getWidth());
            // 高度设置
            popupWindow.setHeight(listHeight);
           popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));   //为PopupWindow设置透明背景.
            // 设置点击弹框外部，弹框消失
            popupWindow.setOutsideTouchable(true);
            // 设置焦点
            popupWindow.setFocusable(true);
            // 设置所在布局
            popupWindow.setContentView(dropV);

            popupWindow.setAnimationStyle(R.style.spinnerInOut);
            // 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度
            popupWindow.showAsDropDown((ViewGroup) v.getParent(), 0, 0);
            // listView的item点击事件
            dropListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String selectValue = listData.get(arg2);
                    mSpinnerText.setText(selectValue);
                    if (textCanEdit) {
                        mSpinnerText.setSelection(selectValue.length());    //将光标移至文字末尾
                        mSpinnerText.setCursorVisible(false);
                    } else
                        mSpinnerText.setCursorVisible(false);
                    // 弹框消失
                    popupWindow.dismiss();

                    if (mItemSelectedListener != null) {
                        mItemSelectedListener.onItemSelected(selectValue);
                    }

                }
            });
        } else {
            rotationJt(0f, 180f);
            popupWindow.setHeight(listHeight);
            popupWindow.showAsDropDown((ViewGroup) v.getParent(), 0, 0);
        }
    }


    public interface itemSelectedListener {
        void onItemSelected(String value);
    }
}
