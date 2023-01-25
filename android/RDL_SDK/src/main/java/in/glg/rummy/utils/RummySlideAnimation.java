package in.glg.rummy.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by GridLogic on 19/1/18.
 */

public class RummySlideAnimation extends Animation {

    int mFromHeight;
    int mToHeight;
    View mView;

    public RummySlideAnimation(View view, int fromHeight, int toHeight) {
        this.mView = view;
        this.mFromHeight = fromHeight;
        this.mToHeight = toHeight;
        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
        
            }
    
            @Override
            public void onAnimationEnd(Animation animation) {
                if (mView.getHeight() != mToHeight) {
                    mView.getLayoutParams().height = toHeight;
                    mView.requestLayout();
                }
            }
    
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        int newHeight;

        if (mView.getHeight() != mToHeight) {
            newHeight = (int) (mFromHeight + ((mToHeight - mFromHeight) * interpolatedTime));
            mView.getLayoutParams().height = newHeight;
            mView.requestLayout();
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
    
    @Override
    public boolean willChangeBounds() {
        return true;
    }
}