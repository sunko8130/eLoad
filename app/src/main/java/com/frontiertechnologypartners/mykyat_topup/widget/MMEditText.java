package com.frontiertechnologypartners.mykyat_topup.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.EditText;

import org.mmtextview.MMFontUtils;
import org.mmtextview.components.MMTextView;

import androidx.appcompat.widget.AppCompatEditText;

public class MMEditText extends AppCompatEditText {

    public MMEditText(Context context) {
        super(context);
    }

    public MMEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!MMFontUtils.isSupportUnicode(getContext())) {
            super.setText(Html.fromHtml(MMFontUtils.uni2zg(text.toString())), type);
        } else {
            super.setText(Html.fromHtml(text.toString()), type);
        }
    }

    @Override
    public void setError(CharSequence error) {
        if (!MMFontUtils.isSupportUnicode(getContext())) {
            super.setError(Html.fromHtml(MMFontUtils.uni2zg(error.toString())));
        } else {
            super.setError(Html.fromHtml(error.toString()));
        }
    }

}
