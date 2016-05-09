package com.dzn.dzn.application.Utils;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by zhenya on 09.05.2016.
 */
public enum PFHandbookProTypeFaces {

    BLACK("PFHandbookPro-Black.ttf"),
    BLACK_ITALIC("PFHandbookPro-BlackItalic.ttf"),
    BOLD("PFHandbookPro-Bold.ttf"),
    BOLD_ITALIC("PFHandbookPro-BoldItalic.ttf"),
    EXTRA_THIN("PFHandbookPro-ExtraThin.ttf"),
    EXTRA_THIN_ITALIC("PFHandbookPro-ExtraThinItal.ttf"),
    ITALIC("PFHandbookPro-Italic.ttf"),
    MEDIUM_ITALIC("PFHandbookPro-MedItalic.ttf"),
    MEDIUM("PFHandbookPro-Medium.ttf"),
    REGULAR("PFHandbookPro-Regular.ttf"),
    THIN("PFHandbookPro-Thin.ttf"),
    THIN_ITALIC("PFHandbookPro-ThinItalic.ttf");

    private static final String TAG = "PFHandbookProTypeFaces";
    private static final String FONT_ASSETS_FOLDER = "fonts";
    private String mFontFileName;

    private PFHandbookProTypeFaces(String mFontFileName) {
        this.mFontFileName = mFontFileName;
    }

    public void apply(TextView view) {
        Typeface face = Typeface.createFromAsset(view.getContext().getAssets(), FONT_ASSETS_FOLDER + "/" + mFontFileName);
        view.setTypeface(face);
    }

    public void apply(Button view) {
        Typeface face = Typeface.createFromAsset(view.getContext().getAssets(), FONT_ASSETS_FOLDER + "/" + mFontFileName);
        view.setTypeface(face);
    }
}
