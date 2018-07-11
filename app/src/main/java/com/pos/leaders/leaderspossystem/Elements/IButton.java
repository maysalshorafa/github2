package com.pos.leaders.leaderspossystem.Elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;

/**
 * Created by KARAM on 08/07/2018.
 */

public class IButton extends LinearLayout {
    private LinearLayout layout;
    private ImageView image;
    private TextView text;

    public IButton(Context context) {
        this(context, null);
    }

    public IButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ibutton, this, true);

        layout = (LinearLayout) view.findViewById(R.id.btn_layout);

        image = (ImageView) view.findViewById(R.id.btn_icon);
        text = (TextView) view.findViewById(R.id.btn_text);

        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.IButtonStyle);

            Drawable drawable = attributes.getDrawable(R.styleable.IButtonStyle_button_icon);
            if(drawable != null) {
                image.setImageDrawable(drawable);
            }

            String str = attributes.getString(R.styleable.IButtonStyle_button_text);
            text.setText(str);

            attributes.recycle();
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(l);
        layout.setOnClickListener(l);
    }

    public void setDrawable(int resId) {
        image.setImageResource(resId);
    }

    @Override
    public void setEnabled(boolean b) {
        this.setClickable(b);
        layout.setEnabled(b);
        layout.setClickable(b);
    }
}