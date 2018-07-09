package app.zingo.com.agentapp.CustomViews;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * Created by ZingoHotels Tech on 03-07-2018.
 */

public class RecyclerViewAnimator {

    private static final int INIT_DELAY = 1000;
    private static final int INIT_TENSION = 250;
    private static final int INIT_FRICTION = 18;
    private static final int SCROLL_TENSION = 300;
    private static final int SCROLL_FRICTION = 30;


    private int mHeight;
    private RecyclerView mRecyclerView;
    private SpringSystem mSpringSystem;

    private boolean mFirstViewInit = true;
    private int mLastPosition = -1;
    private int mStartDelay;

    public RecyclerViewAnimator(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mSpringSystem = SpringSystem.create();

        mHeight = mRecyclerView.getResources().getDisplayMetrics().heightPixels;

        mStartDelay = INIT_DELAY;
    }

    public void onCreateViewHolder(View item) {

        if (mFirstViewInit) {
            slideInBottom(item, mStartDelay, INIT_TENSION, INIT_FRICTION);
            mStartDelay += 70;
        }
    }

    public void onBindViewHolder(View item, int position) {

        if (!mFirstViewInit && position > mLastPosition) {
            slideInBottom(item, 0, SCROLL_TENSION, SCROLL_FRICTION);
            mLastPosition = position;
        }
    }

    private void slideInBottom(final View item,
                               final int delay,
                               final int tension,
                               final int friction) {

        item.setTranslationY(mHeight);

        Runnable startAnimation = new Runnable() {
            @Override
            public void run() {
                SpringConfig config = new SpringConfig(tension, friction);
                Spring spring = mSpringSystem.createSpring();
                spring.setSpringConfig(config);
                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {

                        float val = (float) (mHeight - spring.getCurrentValue());
                        item.setTranslationY(val);
                    }

                    @Override
                    public void onSpringEndStateChange(Spring spring) {
                        mFirstViewInit = false;
                    }
                });


                spring.setEndValue(mHeight);
            }
        };

        mRecyclerView.postDelayed(startAnimation, delay);
    }
}
