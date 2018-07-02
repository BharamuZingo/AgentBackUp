package app.zingo.com.agentapp.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import app.zingo.com.agentapp.R;

/**
 * Created by ZingoHotels.com on 2/9/2018.
 */

public class CustomEditextTTF extends AppCompatEditText {




    private Context context;
    private AttributeSet attrs;
    private int defStyle;
    String customFont;

    public CustomEditextTTF(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public CustomEditextTTF(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        init();
    }

    public CustomEditextTTF(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.attrs=attrs;
        //this.defStyle=defStyle;
        init();
    }

    private void init() {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.custom_edittext);
        int cf = a.getInteger(R.styleable.custom_edittext_editfontName, 0);
        System.out.println("cf = "+cf);
        int fontName = 0;
        switch (cf)
        {

                /*case 1:
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
                    break;*/
            case 3:
                fontName = R.string.arial;
                break;


                default:
                    fontName = R.string.arial;
                    break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }
        /*@Override
        public void setTypeface(Typeface tf, int style) {
            tf=Typeface.createFromAsset(getContext().getAssets(), "font/Yeysk.ttf");
            super.setTypeface(tf, style);
        }

        @Override
        public void setTypeface(Typeface tf) {
            tf=Typeface.createFromAsset(getContext().getAssets(), "font/Yeysk.ttf");
            super.setTypeface(tf);
        }*/
}
