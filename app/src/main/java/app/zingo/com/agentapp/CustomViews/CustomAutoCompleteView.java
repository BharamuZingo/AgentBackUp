package app.zingo.com.agentapp.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by ZingoHotels.com on 5/30/2018.
 */


    public class CustomAutoCompleteView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

        public CustomAutoCompleteView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public CustomAutoCompleteView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }

        // this is how to disable AutoCompleteTextView filter
        @Override
        protected void performFiltering(final CharSequence text, final int keyCode) {
            String filterText = "";
            super.performFiltering(filterText, keyCode);
        }
    }

