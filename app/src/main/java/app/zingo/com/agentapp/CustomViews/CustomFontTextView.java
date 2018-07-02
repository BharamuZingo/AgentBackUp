package app.zingo.com.agentapp.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import app.zingo.com.agentapp.R;


/**
 * Created by CSC on 11/2/2017.
 */

public class CustomFontTextView extends AppCompatTextView {
    String customFont;

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_fontName, 0);
        System.out.println("cf = "+cf);
        int fontName = 0;
        switch (cf)
        {

            case 1:
                fontName = R.string.ColonnaMT;
                break;
            case 2:
                fontName = R.string.Leprosy;
                break;
            case 3:
                fontName = R.string.BomBardment;
                break;
            case 4:
                fontName = R.string.mina;
                break;

            case 6:
                fontName= R.string.arial;
                break;
            case 7:
                fontName= R.string.Mistral;
                break;
            case 8:
                fontName= R.string.arial_bold;
                break;
            case 9:
                fontName= R.string.OpenSans_Bold;
                break;
            case 10:
                fontName= R.string.OpenSans_Light;
                break;
            default:
                fontName = R.string.ColonnaMT;
                break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }
}
