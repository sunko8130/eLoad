package com.frontiertechnologypartners.mykyat_topup.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import org.mmtextview.MMFontUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class MMToolbar extends Toolbar {
    public MMToolbar(Context context) {
        super(context);
    }

    public MMToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MMToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!MMFontUtils.isSupportUnicode(getContext())) {
            super.setTitle(Html.fromHtml(MMFontUtils.uni2zg(title.toString())));
        } else {
            super.setTitle(Html.fromHtml(title.toString()));
        }
    }
}
