package com.inspur.dengxj.savepic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by deng.xj on 2017/10/31.
 */

public class CustomPopupWindow extends PopupWindow implements View.OnClickListener {
    private Button savePic;
    private View mPopView;
    private OnItemClickListener mListener;
    public CustomPopupWindow(Context context){
        super(context);
        init(context);
        setPopupWindow();
        savePic.setOnClickListener(this);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.popuplayout,null);
        savePic = mPopView.findViewById(R.id.savePic);
    }
    @SuppressLint("InlineApi")
    private void setPopupWindow(){
        this.setContentView(mPopView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int)motionEvent.getY();
                if(motionEvent.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public interface  OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void onClick(View v){
        if(mListener != null){
            mListener.setOnItemClick(v);
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }
}
