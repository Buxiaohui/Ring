package com.buxiaohui.example.ring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.buxiaohui.annotation.LightNaviService;
import com.buxiaohui.example.sort.Model;
import com.buxiaohui.example.transfer.Transfer;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

@LightNaviService
public class MainActivity extends AppCompatActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        top = findViewById(R.id.top);
        bottom = findViewById(R.id.bottom);
        bottomContainer = findViewById(R.id.bottom_container);
        mText = findViewById(R.id.text);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim(true);
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim(false);
            }
        });
        ImageView imageView1 = findViewById(R.id.image1);
        ImageView imageView2 = findViewById(R.id.image2);
        setImage(imageView1, true);
        setImage(imageView2, false);

        testSortMap();
        testColor();


        Transfer transfer = new Transfer(Transfer.createGoodsList(10),3);
        transfer.transfer();
    }

    private void setImage(ImageView imageView, boolean isSelect) {
        if (isSelect) {
            imageView.setColorFilter(Color.parseColor("#3385FF"));
        } else {
            imageView.setColorFilter(Color.parseColor("#646D82"));
        }
        imageView.setImageResource(R.drawable.guanfangshijian);
    }

    private ValueAnimator mValueAnimator;
    private ValueAnimator mBottomValueAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private ValueAnimator.AnimatorUpdateListener mBottomAnimatorUpdateListener;
    private View top;
    private View bottom;
    private View bottomContainer;
    private final static String TAG = "MainActivity";

    private ValueAnimator getValueAnimator() {
        if (mValueAnimator == null) {
            long duration = 2_000L;
            int repeatCount = 3;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, (float) (Math.PI * 2 * repeatCount + Math.PI / 4));
            valueAnimator.setDuration(duration);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.addUpdateListener(getAnimatorUpdateListener());
            mValueAnimator = valueAnimator;
        }
        return mValueAnimator;
    }

    private ValueAnimator getBottomValueAnimator() {
        if (mBottomValueAnimator == null) {
            long duration = 2_000L;
            int repeatCount = 3;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, (float) (Math.PI * 2 * repeatCount + Math.PI / 4));
            valueAnimator.setDuration(duration);
            valueAnimator.setStartDelay(2);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.addUpdateListener(getAnimatorUpdateListener());
            mBottomValueAnimator = valueAnimator;
        }
        return mBottomValueAnimator;
    }

    private ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener() {
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = (float) animation.getAnimatedValue();
                    float edgeTop = 30f;
                    float rotateTop = (float) (Math.sin(offset) * edgeTop);
                    if (top != null) {
                        top.setPivotY(0);
                        top.setRotation(rotateTop);
                    }
                }
            };
        }
        return mAnimatorUpdateListener;

    }

    private ValueAnimator.AnimatorUpdateListener getBottomAnimatorUpdateListener() {
        if (mBottomAnimatorUpdateListener == null) {
            mBottomAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = (float) animation.getAnimatedValue();
                    float edgeBottom = 15f;
                    float rotateBottom = (float) (Math.sin(offset - Math.PI / 4) * edgeBottom);
                    if (bottomContainer != null) {
                        bottomContainer.setPivotY(0);
                        bottomContainer.setRotation(rotateBottom);
                    }

                }
            };
        }
        return mBottomAnimatorUpdateListener;
    }

    private void startAnim(boolean start) {
        if (start) {

            getValueAnimator().start();
        } else {
            getValueAnimator().cancel();
        }
    }

    public void testSortMap() {
        HashMap<Integer, Model> arrayMap = new HashMap<>();
        arrayMap.put(0, new Model(4));
        arrayMap.put(1, new Model(3));
        arrayMap.put(2, new Model(2));
        arrayMap.put(3, new Model(1));
        arrayMap.put(4, new Model(0));
        List<Map.Entry<Integer, Model>> list =
                new ArrayList<Map.Entry<Integer, Model>>(arrayMap.entrySet());
        System.out.println(TAG + "---原始---");
        System.out.println(TAG + "--" + arrayMap);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Model>>() {
            @Override
            public int compare(Map.Entry<Integer, Model> o1, Map.Entry<Integer, Model> o2) {
                if (o1.getValue().index > o2.getValue().index) {
                    return 1;
                } else if (o1.getValue().index < o2.getValue().index) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println(TAG + "---人工干预后---");
        System.out.println(TAG + "--" + arrayMap);
        System.out.println(TAG + "--" + list);
    }

    private void testColor(){
        String  str = "试一下新朋友熟路模式吧";
        SpannableString strSan = new SpannableString(str);
        setTextColor(strSan, Color.parseColor("#3385FF"), str.indexOf("熟"), str.indexOf("吧"));
        mText.setText(strSan);
    }

    private void setTextColor(SpannableString spanStr, int color, int start, int end) {
        if (start != -1 && end != -1 && end >= start) {
            spanStr.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}