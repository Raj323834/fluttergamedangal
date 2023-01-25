package in.glg.rummy.actionmenu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.utils.RummyGameRoomCustomScreenLess700;
import in.glg.rummy.utils.RummyGameRoomCustomScreenMore700;
import in.glg.rummy.utils.RummyUtils;

public class RummyQuickAction extends RummyPopupWindows implements PopupWindow.OnDismissListener {
    public static final int ANIM_AUTO = 5;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_REFLECT = 4;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private List<RummyActionItem> actionItems;
    private int mAnimStyle;
    private ImageView mArrowDown;
    private ImageView mArrowUp;
    private int mChildPos;
    private boolean mDidAction;
    private OnDismissListener mDismissListener;
    private LayoutInflater mInflater;
    private int mInsertPos;
    private OnActionItemClickListener mItemClickListener;
    private int mOrientation;
    private View mRootView;
    private ScrollView mScroller;
    private ViewGroup mTrack;
    private int rootWidth;

    public RummyQuickAction(Context context) {
        this(context, 1);
    }

    public RummyQuickAction(Context context, int orientation) {
        super(context);
        this.actionItems = new ArrayList();
        this.rootWidth = 0;
        this.mOrientation = orientation;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (this.mOrientation == HORIZONTAL) {
            setRootViewId(R.layout.rummy_popup_horizontal);
        } else {
            setRootViewId(R.layout.rummy_popup_vertical);
        }
        this.mAnimStyle = ANIM_AUTO;
        this.mChildPos = 0;
    }

    private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
        int i = R.style.Animations_PopUpMenu_Left;
        int i2 = R.style.Animations_PopUpMenu_Center;
        int i3 = R.style.Animations_PopDownMenu_Right;
        int arrowPos = requestedX - (this.mArrowUp.getMeasuredWidth() / 2);
        PopupWindow popupWindow;
        switch (this.mAnimStyle) {
            case 1:
                popupWindow = this.mWindow;
                if (!onTop) {
                    i = R.style.Animations_PopDownMenu_Left;
                }
                popupWindow.setAnimationStyle(i);
                return;
            case 2:
                this.mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                return;
            case 3:
                this.mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                return;
            case 4:
                this.mWindow.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
                return;
            case 5:
                if (requestedX <= screenWidth / 4) {
                    popupWindow = this.mWindow;
                    if (!onTop) {
                        i = R.style.Animations_PopDownMenu_Left;
                    }
                    popupWindow.setAnimationStyle(i);
                    return;
                } else if (arrowPos <= screenWidth / 4 || arrowPos >= (screenWidth / 4) * 3) {
                    popupWindow = this.mWindow;
                    if (onTop) {
                        i3 = R.style.Animations_PopUpMenu_Right;
                    }
                    popupWindow.setAnimationStyle(i3);
                    return;
                } else {
                    popupWindow = this.mWindow;
                    if (!onTop) {
                        i2 = R.style.Animations_PopDownMenu_Center;
                    }
                    popupWindow.setAnimationStyle(i2);
                    return;
                }
        }

    }

    private void showArrow(int whichArrow, int requestedX) {
        View showArrow = whichArrow == R.id.arrow_up ? this.mArrowUp : this.mArrowDown;
        View hideArrow = whichArrow == R.id.arrow_up ? this.mArrowDown : this.mArrowUp;
        int arrowWidth = this.mArrowUp.getMeasuredWidth();
        showArrow.setVisibility(View.VISIBLE);
        ((MarginLayoutParams) showArrow.getLayoutParams()).leftMargin = requestedX - (arrowWidth / 2);
        hideArrow.setVisibility(View.INVISIBLE);
    }

    public void addActionItem(RummyActionItem action) {
        View container;
        this.actionItems.add(action);
        String title = action.getTitle();
        Drawable icon = action.getIcon();
        if (this.mOrientation == 0) {
            container = this.mInflater.inflate(R.layout.rummy_action_item_horizontal, null);
        } else {
            container = this.mInflater.inflate(R.layout.rummy_action_item_vertical, null);
        }
        ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
        TextView text = (TextView) container.findViewById(R.id.tv_title);
        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }
        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }
        ImageView discardIv = (ImageView) this.mTrack.findViewById(R.id.discard_iv);


        if(!RummyUtils.isTablet(mContext))
        {
            RummyUtils.setViewWidth(discardIv, RummyGameRoomCustomScreenLess700.stackCardWidth);
        }else
        {
            RummyUtils.setViewWidth(discardIv, RummyGameRoomCustomScreenMore700.stackCardWidth);
        }


        if (title.equalsIgnoreCase("discard")) {
            discardIv.setImageResource(R.drawable.rummy_discard);
        } else {
            discardIv.setImageResource(R.drawable.rummy_group);
        }
        final int pos = this.mChildPos;
        final int actionId = action.getActionId();
        this.mTrack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RummyQuickAction.this.mItemClickListener != null) {
                    RummyQuickAction.this.mItemClickListener.onItemClick(RummyQuickAction.this, pos, actionId);
                }
                if (!RummyQuickAction.this.getActionItem(pos).isSticky()) {
                    RummyQuickAction.this.mDidAction = true;
                    RummyQuickAction.this.dismiss();
                }
            }
        });
        container.setFocusable(true);
        container.setClickable(true);
        if (this.mOrientation == 0 && this.mChildPos != 0) {
            View separator = this.mInflater.inflate(R.layout.rummy_horiz_separator, null);
            separator.setLayoutParams(new LayoutParams(-2, -1));
            separator.setPadding(5, 0, 5, 0);
            this.mInsertPos++;
        }
        this.mChildPos++;
        this.mInsertPos++;
    }

    public RummyActionItem getActionItem(int index) {
        return (RummyActionItem) this.actionItems.get(index);
    }

    public void onDismiss() {
        if (!this.mDidAction && this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }
    }

    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.setOnDismissListener(this);
        this.mDismissListener = listener;
    }

    public void setRootViewId(int id) {
        this.mRootView = (ViewGroup) this.mInflater.inflate(id, null);
        this.mTrack = (ViewGroup) this.mRootView.findViewById(R.id.tracks);
        this.mArrowDown = (ImageView) this.mRootView.findViewById(R.id.arrow_down);
        this.mArrowUp = (ImageView) this.mRootView.findViewById(R.id.arrow_up);
        this.mScroller = (ScrollView) this.mRootView.findViewById(R.id.scroller);
        this.mRootView.setLayoutParams(new LayoutParams(-2, -2));
        setContentView(this.mRootView);
    }

    public void show(View anchor) {
        int xPos;
        int yPos;
        preShow();
        this.mDidAction = false;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
        this.mRootView.measure(-2, -2);
        int rootHeight = this.mRootView.getMeasuredHeight();
        if (this.rootWidth == 0) {
            this.rootWidth = this.mRootView.getMeasuredWidth();
        }
        int screenWidth = this.mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = this.mWindowManager.getDefaultDisplay().getHeight();
        int arrowPos;
        if (anchorRect.left + this.rootWidth > screenWidth) {
            xPos = anchorRect.left - (this.rootWidth - anchor.getWidth());
            if (xPos < 0) {
                xPos = 0;
            }
            arrowPos = anchorRect.centerX() - xPos;
        } else {
            if (anchor.getWidth() > this.rootWidth) {
                Log.e("gopal","root width lesser than get width");
                xPos = anchorRect.centerX() - (this.rootWidth / 2);
            } else {
                xPos = anchorRect.left;
                Log.e("gopal","root width greater than get width else condition");
                //xPos = (int)anchor.getX() + (anchor.getWidth()/3);
            }
            arrowPos = anchorRect.centerX() - xPos;
        }
        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;
        boolean onTop = dyTop > dyBottom;
        if (!onTop) {
            yPos = anchorRect.bottom;
            if (rootHeight > dyBottom) {
                this.mScroller.getLayoutParams().height = dyBottom;
            }
        } else if (rootHeight > dyTop) {
            yPos = 15;
            this.mScroller.getLayoutParams().height = dyTop - anchor.getHeight();
        } else {
            yPos = anchorRect.top - rootHeight;
        }
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        this.mWindow.showAtLocation(anchor, 0, xPos, yPos);
    }

    public interface OnActionItemClickListener {
        void onItemClick(RummyQuickAction quickAction, int var2, int var3);
    }

    public interface OnDismissListener {
        void onDismiss();
    }


}
