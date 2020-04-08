package com.frontiertechnologypartners.mykyat_topup.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;

import org.mmtextview.MMFontUtils;

import androidx.annotation.Nullable;

public class MMTextInputLayout extends TextInputLayout {
    public MMTextInputLayout(Context context) {
        super(context);
    }

    public MMTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setHint(@Nullable CharSequence hint) {
        if (hint != null)
            if (!MMFontUtils.isSupportUnicode(getContext())) {
                super.setHint(Html.fromHtml(MMFontUtils.uni2zg(hint.toString())));
            } else {
                super.setHint(Html.fromHtml(hint.toString()));
            }
    }
}
