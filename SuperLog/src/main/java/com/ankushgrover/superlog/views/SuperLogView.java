package com.ankushgrover.superlog.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.SuperLogActivity;


/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLogView extends AppCompatTextView implements View.OnClickListener {

    private Context mContext;

    // used in view creation programmatically
    public SuperLogView(Context context) {
        super(context);
        init(context, null, 0);
    }

    // used in XML layout file
    public SuperLogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SuperLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        this.mContext = context;

        int backgroundColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
        String text = "Logs";
        int gravity = Gravity.CENTER;
        int visibility = SuperLog.getBuilder().isSuperLogViewVisible() ? VISIBLE : GONE;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperLogView);


            if (a.hasValue(R.styleable.SuperLogView_android_text))
                text = a.getString(R.styleable.SuperLogView_android_text);

            if (a.hasValue(R.styleable.SuperLogView_android_background))
                backgroundColor = a.getInt(R.styleable.SuperLogView_android_background, backgroundColor);


            if (visibility == VISIBLE && a.hasValue(R.styleable.SuperLogView_android_visibility)) {
                int index = a.getInt(R.styleable.SuperLogView_android_visibility, visibility);

                switch (index) {
                    case 0:
                        visibility = VISIBLE;
                        break;
                    case 1:
                    case 2:
                        visibility = GONE;
                        break;
                }

            }

            a.recycle();
        }


        setText(text);
        setText(text);
        setBackgroundColor(backgroundColor);
        setGravity(gravity);
        setTypeface(getTypeface(), Typeface.BOLD);
        setVisibility(visibility);

        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, SuperLogActivity.class);
        mContext.startActivity(intent);
    }
}
