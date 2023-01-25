package in.glg.rummy.view;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.fragments.RummyTablesFragment;
import in.glg.rummy.models.RummyCard;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.utils.RummyGameRoomCustomScreenLess700;
import in.glg.rummy.utils.RummyGameRoomCustomScreenMore700;
import io.github.douglasjunior.androidSimpleTooltip.ArrowDrawable;
import io.github.douglasjunior.androidSimpleTooltip.BuildConfig;

import static android.view.DragEvent.ACTION_DRAG_ENDED;

public class RummyView extends LinearLayout {
    private static final String TAG = RummyView.class.getName();
    private int CARD_SPACE = 32;
    private int STACK_WIDTH = 60;
    private int EMPTY_CARD_SPACE = 5;

    private int CARD_SPACE_FIXED = 32;
    private int STACK_WIDTH_FIXED = 60;
    private int EMPTY_CARD_SPACE_FIXED = 5;

    private String MAIN_HOLDER = "MAIN_HOLDER";
    private String TARGET_TAG = "CARD_HOLDER";
    private List<RummyCard> cards;
    private LayoutInflater inflater;
    private int initialX = 0;
    private Context mContext;
    private RummyTablesFragment mTableFragment;
    private RelativeLayout mainHolder;

    class MyDragListener implements OnDragListener {
        boolean insideOfMe = false;
        Drawable normalBorder = null;

        MyDragListener() {
        }

        @TargetApi(16)
        public boolean onDrag(View parentView, DragEvent event) {
            try {
                String tag = (String) parentView.getTag();
                View view = (View) event.getLocalState();
                int drag;

                view.setVisibility(INVISIBLE);

                switch (event.getAction())
                {
                    case ArrowDrawable.TOP /*1*/:
                        this.normalBorder = parentView.getBackground();
                        RummyView.this.initialX = (int) view.getX();
                        if (RummyView.this.mTableFragment != null) {
                            Log.e("gopal","top drag if");
                            RummyView.this.mTableFragment.dismissQuickMenu();
                            break;
                        }
                        Log.e("gopal","top drag");
                        break;
                    case ArrowDrawable.RIGHT /*2*/:
                        Log.e("gopal","right drag");
                        drag = (int) event.getX();
                        break;
                    case ArrowDrawable.BOTTOM /*3*/:
                        ArrayList<ArrayList<RummyPlayingCard>> groupList = new ArrayList();
                        drag = (int) event.getX();
                        Log.e("gopal","bottom drag");
                        if (tag != null)
                        {
                            Log.e("gopal","bottom drag tag!=null");
                            String viewTag = tag;
                            ViewGroup owner;
                            RelativeLayout.LayoutParams params;
                            if (this.insideOfMe && viewTag.equalsIgnoreCase(RummyView.this.TARGET_TAG))
                            {
                                Log.e("gopal","Target TAg inside of me");
                                owner = (ViewGroup) view.getParent();
                                ViewGroup target = (RelativeLayout) parentView;
                                params = new RelativeLayout.LayoutParams(view.getLayoutParams());
                                if (owner != target)
                                {
                                    owner.removeView(view);
                                    params.leftMargin = target.getChildCount() * RummyView.this.CARD_SPACE;
                                    target.addView(view, params);
                                    view.setVisibility(View.VISIBLE);
                                }
                                else if (owner == target)
                                {
                                    Log.e("gopal","target=owner tag else if");
                                    View childView;
                                    int leftMargin;
                                    RelativeLayout.LayoutParams childParams;
                                    params.leftMargin = (owner.getChildCount() + 1) * RummyView.this.CARD_SPACE;
                                    int targetIndex = 0;
                                    int dragPosition = drag - (view.getWidth() / 2);
                                    int i = 0;
                                    while (i < owner.getChildCount())
                                    {
                                        childView = owner.getChildAt(i);
                                        if (((float) dragPosition) <= childView.getX() || ((float) dragPosition) >= childView.getX() + ((float) RummyView.this.CARD_SPACE))
                                        {
                                            i++;
                                        }
                                        else
                                        {
                                            if (RummyView.this.initialX - drag > 0) {
                                                targetIndex = owner.indexOfChild(childView) + 1;
                                            } else if (RummyView.this.initialX - drag < 0) {
                                                targetIndex = owner.indexOfChild(childView);
                                            }
                                            if (targetIndex >= owner.getChildCount()) {
                                                targetIndex = owner.getChildCount() - 1;
                                            }
                                            if (targetIndex < 0) {
                                                targetIndex = 0;
                                            }
                                            owner.removeView(view);
                                            target.addView(view, targetIndex, params);
                                            leftMargin = 0;
                                            for (i = 0; i < target.getChildCount(); i++)
                                            {
                                                childView = target.getChildAt(i);
                                                childParams = new RelativeLayout.LayoutParams(childView.getLayoutParams());
                                                childParams.leftMargin = leftMargin;

                                                if(childView.getTag() == null)
                                                {
                                                    leftMargin += (RummyView.this.CARD_SPACE+RummyView.this.EMPTY_CARD_SPACE);
                                                }
                                                else
                                                {
                                                    leftMargin += RummyView.this.CARD_SPACE;
                                                }
                                               // leftMargin += RummyView.this.CARD_SPACE;
                                                childView.setLayoutParams(childParams);
                                            }
                                        }
                                    }

                                    owner.removeView(view);
                                    target.addView(view, targetIndex, params);
                                    leftMargin = 0;
                                    for (i = 0; i < target.getChildCount(); i++) {
                                        childView = target.getChildAt(i);
                                        childParams = new RelativeLayout.LayoutParams(childView.getLayoutParams());
                                        childParams.leftMargin = leftMargin;

                                        if(childView.getTag() == null)
                                        {
                                            leftMargin += (RummyView.this.CARD_SPACE+RummyView.this.EMPTY_CARD_SPACE);
                                        }
                                        else
                                        {
                                            leftMargin += RummyView.this.CARD_SPACE;
                                        }

                                      //  leftMargin += RummyView.this.CARD_SPACE;
                                        childView.setLayoutParams(childParams);
                                    }
                                }
                                if (owner.getChildCount() == 0)
                                {
                                    RummyView.this.mainHolder.removeView(owner);
                                }
                                RummyView.this.mainHolder.invalidate();

                                if (RummyView.this.mTableFragment != null) // change latest
                                {
                                    RummyView.this.mTableFragment.arrangeSelectedCards(RummyView.this.mTableFragment.getTag().toString());
                                    RummyView.this.mTableFragment.updateCardsGroup(RummyView.this.getUpdatedCardsGroup());
                                }
                            }
                            else if (this.insideOfMe && viewTag.equalsIgnoreCase(RummyView.this.MAIN_HOLDER))
                            {
                                Log.e("gopal","inside me Main holder");
                                /*RelativeLayout l1 = (RelativeLayout) RummyView.this.inflater.inflate(R.layout.rummy_card_group_layout, RummyView.this.mainHolder, false);
                                l1.setTag(RummyView.this.TARGET_TAG);
                                owner = (ViewGroup) view.getParent();
                                owner.removeView(view);
                                owner.invalidate();
                                params = new RelativeLayout.LayoutParams(view.getLayoutParams());
                                params.leftMargin = 10;
                                l1.addView(view, params);
                                l1.setOnDragListener(new MyDragListener());
                                view.setVisibility(View.VISIBLE);
                                RummyView.this.mainHolder.addView(l1);*/
                            }

                            if (RummyView.this.mTableFragment != null)
                            {
                                Log.e("gopal","card rummy_group updated");
                              //  RummyView.this.mTableFragment.updateCardsGroup(RummyView.this.getUpdatedCardsGroup());
                                RummyView.this.mTableFragment.arrangeSelectedCards(RummyView.this.mTableFragment.getTag().toString());
                                RummyView.this.mTableFragment.updateCardsGroup(RummyView.this.getUpdatedCardsGroup());
                            }
                        }
                        view.setVisibility(View.VISIBLE);
                        break;
                    case BuildConfig.VERSION_CODE /*4*/:
                        Log.e("drag","case version code");
                        if (this.normalBorder != null) {
                            parentView.setBackground(this.normalBorder);
                        } else {
                            parentView.setBackground(null);
                        }
                        view.setVisibility(View.VISIBLE);
                        view.clearAnimation();
                        if (RummyView.this.mTableFragment != null) {
                            RummyView.this.mTableFragment.sendCardsSlots();
                        }
                        if (RummyView.this.mTableFragment != null) {
                          //  RummyView.this.mTableFragment.arrangeSelectedCards(RummyView.this.mTableFragment.getTag().toString());
                            RummyView.this.mTableFragment.arrangeSelectedCards(RummyView.this.mTableFragment.getTag().toString());
                            RummyView.this.mTableFragment.updateCardsGroup(RummyView.this.getUpdatedCardsGroup());
                            break;
                        }
                        break;
                    case RummyCorouselView.PAGES /*5*/:
                        Log.e("drag","case Pages");
                        this.insideOfMe = true;
                        break;
                    case ACTION_DRAG_ENDED:
                        Log.e("drag","drag ended");
                        view.setVisibility(VISIBLE);
                        break;
                    //case R.styleable.Toolbar_contentInsetEnd /*6*/:
                    case 6:
                        Log.e("drag","case 6");
                        this.insideOfMe = false;
                        RummyView.this.initialX = 0;
                        parentView.setBackground(this.normalBorder);
                        if (!(tag == null || tag.equalsIgnoreCase(RummyView.this.TARGET_TAG))) {
                            view.setVisibility(View.VISIBLE);
                            break;
                        }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                parentView.invalidate();
                Log.e(TAG, e + "");
            }
            return true;
        }


    }

    private final class MyTouchListener implements OnTouchListener {
        private static final int MAX_CLICK_DISTANCE = 10;
        private static final int MAX_CLICK_DURATION = 180;
        long pressDuration;
        private long pressStartTime;
        private float pressedX;
        private float pressedY;
        private boolean stayedWithinClickDistance;

        private MyTouchListener() {
        }

        public boolean onTouch(View view, MotionEvent ev) {
            switch (ev.getAction() & BallSpinFadeLoaderIndicator.ALPHA) {
                case ArrowDrawable.LEFT /*0*/:
                    this.pressStartTime = System.currentTimeMillis();
                    this.pressedX = ev.getX();
                    this.pressedY = ev.getY();
                    this.stayedWithinClickDistance = true;
                    break;
                case ArrowDrawable.TOP /*1*/:
                    this.pressDuration = System.currentTimeMillis() - this.pressStartTime;
                    if (this.pressDuration < MAX_CLICK_DURATION && this.stayedWithinClickDistance) {
                        this.stayedWithinClickDistance = false;
                    }
                    view.setVisibility(View.VISIBLE);
                    break;
                case ArrowDrawable.RIGHT /*2*/:
                    ImageView layerView = (ImageView) view.findViewById(R.id.cardImageViewSelected);
                    if (layerView != null && layerView.getVisibility() == View.VISIBLE)
                    {
                        view.clearAnimation();
                        view.invalidate();
                        layerView.setVisibility(View.VISIBLE);
                    }
                    this.pressDuration = System.currentTimeMillis() - this.pressStartTime;
                    if (this.pressDuration > MAX_CLICK_DURATION && this.stayedWithinClickDistance && RummyView.this.distance(this.pressedX, this.pressedY, ev.getX(), ev.getY()) > MAX_CLICK_DISTANCE)
                    {
                        this.stayedWithinClickDistance = false;
                        ClipData data = ClipData.newPlainText(BuildConfig.FLAVOR, BuildConfig.FLAVOR);
                        view.setAlpha(RummyCorouselView.BIG_SCALE);
                        DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
                        view.setAlpha(RummyCorouselView.BIG_SCALE);
                        view.startDrag(data, shadowBuilder, view, 0);
                        view.setVisibility(View.VISIBLE);
                        break;
                    }
            }
            return false;
        }
    }

    public RummyView(Context context) {
        super(context);
        init(context);
    }

    public RummyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelativeLayout getRootView() {
        return this.mainHolder;
    }

    private void init(Context context) {
        this.mContext = context;
        if (!isInEditMode()) {
            //this.CARD_SPACE = (int) (this.mContext.getResources().getDimension(R.dimen.card_margin) / this.mContext.getResources().getDisplayMetrics().density);
            //this.CARD_SPACE = (int) (convertDpToPixel(22.0f, getContext()));



            if(!isTablet(context))
            {
                this.STACK_WIDTH = (int) (convertDpToPixel(RummyGameRoomCustomScreenLess700.stackCardWidth, getContext()));
                this.CARD_SPACE = (int) (convertDpToPixel(RummyGameRoomCustomScreenLess700.cardGroupSpacing, getContext()));
                this.EMPTY_CARD_SPACE = (int)(convertDpToPixel(RummyGameRoomCustomScreenLess700.emptyCardSpace, getContext()));

                this.CARD_SPACE_FIXED =  (int) (convertDpToPixel(RummyGameRoomCustomScreenLess700.cardGroupSpacing_fixed, getContext()));
                this.STACK_WIDTH_FIXED =(int) (convertDpToPixel(RummyGameRoomCustomScreenLess700.stackCardWidth_fixed, getContext()));
                this.EMPTY_CARD_SPACE_FIXED = (int)(convertDpToPixel(RummyGameRoomCustomScreenLess700.emptyCardSpace_fixed, getContext()));
            } else
            {
                this.STACK_WIDTH = (int) (convertDpToPixel(RummyGameRoomCustomScreenMore700.stackCardWidth, getContext()));
                this.CARD_SPACE = (int) (convertDpToPixel(RummyGameRoomCustomScreenMore700.cardGroupSpacing, getContext()));
                this.EMPTY_CARD_SPACE = (int)(convertDpToPixel(RummyGameRoomCustomScreenMore700.emptyCardSpace, getContext()));

                this.CARD_SPACE_FIXED =  (int) (convertDpToPixel(RummyGameRoomCustomScreenMore700.cardGroupSpacing_fixed, getContext()));
                this.STACK_WIDTH_FIXED =(int) (convertDpToPixel(RummyGameRoomCustomScreenMore700.stackCardWidth_fixed, getContext()));
                this.EMPTY_CARD_SPACE_FIXED = (int)(convertDpToPixel(RummyGameRoomCustomScreenMore700.emptyCardSpace_fixed, getContext()));
            }


         //   this.CARD_SPACE = (int) (convertDpToPixel(28.0f, getContext()));
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.cards = new ArrayList();
            this.mainHolder = (RelativeLayout) this.inflater.inflate(R.layout.rummy_card_group_layout, this, false);
            this.mainHolder.removeAllViews();
            this.mainHolder.invalidate();
            this.mainHolder.setTag(this.TARGET_TAG);
            this.mainHolder.setOnDragListener(new MyDragListener());
            addView(this.mainHolder);
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void setTableFragment(RummyTablesFragment tableFragment) {
        this.mTableFragment = tableFragment;
    }

    /*public void addCard(LinearLayout card)
    {
        try
        {
            int empty_card_size = this.getCurrentEmptyView();

            //  RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams((int) convertDpToPixel(50.0f, getContext()), ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(this.STACK_WIDTH_FIXED, ViewGroup.LayoutParams.WRAP_CONTENT);

            //lp2.setMargins((this.mainHolder.getChildCount() * ((int) pxToDp(this.mContext.getResources().getDimension(R.dimen.card_margin))))+10, 0, 0, 0);
            //lp2.setMargins((this.mainHolder.getChildCount() * (int)(convertDpToPixel(22.0f, getContext())))+10, 0, 0, 0);
          //  lp2.setMargins((this.mainHolder.getChildCount() * (int)(convertDpToPixel(25.0f, getContext())))+10, 0, 0, 0);
            lp2.setMargins((this.mainHolder.getChildCount() * this.CARD_SPACE_FIXED)+this.EMPTY_CARD_SPACE_FIXED*empty_card_size+this.EMPTY_CARD_SPACE_FIXED, 0, 0, 0);

            card.setLayoutParams(lp2);

            this.mainHolder.addView(card);
            this.mainHolder.setGravity(RelativeLayout.END_OF);
            card.setOnTouchListener(new MyTouchListener());

            *//*LayoutParams params = new LayoutParams(card.getLayoutParams());
            params.leftMargin = (this.mainHolder.getChildCount() * ((int) (this.mContext.getResources().getDimension(R.dimen.card_margin) / this.mContext.getResources().getDisplayMetrics().density))) + 10;
            this.mainHolder.addView(card, params);
            this.mainHolder.setGravity(17);
            card.setOnTouchListener(new MyTouchListener());*//*
        }
        catch (Exception e){
            Log.e("flow", "EXP: RummyView > addCard -->> "+e.toString());
        }
    }*/

    public void addCard(LinearLayout card,boolean isfixed)
    {
        try
        {
            if(isfixed)
            {
                if(card.getTag() == null)
                {
                    int empty_card_size = this.getCurrentEmptyView();
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(this.STACK_WIDTH_FIXED, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //lp2.setMargins((this.mainHolder.getChildCount() * ((int) pxToDp(this.mContext.getResources().getDimension(R.dimen.card_margin))))+10, 0, 0, 0);
                    lp2.setMargins((this.mainHolder.getChildCount() * this.CARD_SPACE_FIXED)+this.EMPTY_CARD_SPACE_FIXED*empty_card_size+this.EMPTY_CARD_SPACE_FIXED, 0, 0, 0);
                    card.setLayoutParams(lp2);

                    this.mainHolder.addView(card);
                    this.mainHolder.setGravity(RelativeLayout.END_OF);
                    card.setOnTouchListener(new MyTouchListener());

            /*LayoutParams params = new LayoutParams(card.getLayoutParams());
            params.leftMargin = (this.mainHolder.getChildCount() * ((int) (this.mContext.getResources().getDimension(R.dimen.card_margin) / this.mContext.getResources().getDisplayMetrics().density))) + 10;
            this.mainHolder.addView(card, params);
            this.mainHolder.setGravity(17);
            card.setOnTouchListener(new MyTouchListener());*/
                }
                else
                {
                    int empty_card_size = this.getCurrentEmptyView();
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(this.STACK_WIDTH_FIXED, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //lp2.setMargins((this.mainHolder.getChildCount() * ((int) pxToDp(this.mContext.getResources().getDimension(R.dimen.card_margin))))+10, 0, 0, 0);
                    lp2.setMargins((this.mainHolder.getChildCount() * this.CARD_SPACE_FIXED)+this.EMPTY_CARD_SPACE_FIXED*empty_card_size, 0, 0, 0);
                    card.setLayoutParams(lp2);

                    this.mainHolder.addView(card);
                    this.mainHolder.setGravity(RelativeLayout.END_OF);
                    card.setOnTouchListener(new MyTouchListener());

            /*LayoutParams params = new LayoutParams(card.getLayoutParams());
            params.leftMargin = (this.mainHolder.getChildCount() * ((int) (this.mContext.getResources().getDimension(R.dimen.card_margin) / this.mContext.getResources().getDisplayMetrics().density))) + 10;
            this.mainHolder.addView(card, params);
            this.mainHolder.setGravity(17);
            card.setOnTouchListener(new MyTouchListener());*/
                }
            }
            else
            {
                if(card.getTag() == null)
                {
                    int empty_card_size = this.getCurrentEmptyView();
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(this.STACK_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins((this.mainHolder.getChildCount() * this.CARD_SPACE)+this.EMPTY_CARD_SPACE*empty_card_size+this.EMPTY_CARD_SPACE, 0, 0, 0);
                    card.setLayoutParams(lp2);

                    this.mainHolder.addView(card);
                    this.mainHolder.setGravity(RelativeLayout.END_OF);
                    card.setOnTouchListener(new MyTouchListener());

                }
                else
                {
                    int empty_card_size = this.getCurrentEmptyView();
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(this.STACK_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins((this.mainHolder.getChildCount() * this.CARD_SPACE)+this.EMPTY_CARD_SPACE*empty_card_size, 0, 0, 0);
                    card.setLayoutParams(lp2);

                    this.mainHolder.addView(card);
                    this.mainHolder.setGravity(RelativeLayout.END_OF);
                    card.setOnTouchListener(new MyTouchListener());

                }
            }



        }
        catch (Exception e){
            Log.e("flow", "EXP: RummyView > addCard -->> "+e.toString());
        }
    }

    public void removeViews() {
        this.mainHolder.removeAllViews();
        this.mainHolder.invalidate();
    }

    public LinearLayout getCard() {
        return (LinearLayout) this.inflater.inflate(R.layout.rummy_card_view, this, false);
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        return px;
    }


    public LinearLayout getEmptyCard() {
        return (LinearLayout) this.inflater.inflate(R.layout.rummy_empty_card_view, this, false);
    }

    public LinearLayout getGameResultCard() {
        return (LinearLayout) this.inflater.inflate(R.layout.rummy_game_results_rummy_item, this, false);
    }

    public LinearLayout getPlayerDiscardCard() {
        return (LinearLayout) this.inflater.inflate(R.layout.rummy_player_discard_cards_item, this, false);
    }

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return pxToDp((float) Math.sqrt((double) ((dx * dx) + (dy * dy))));
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    public void addGameResultCard(LinearLayout card)
    {
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams((int) convertDpToPixel(20.0f, getContext()), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(((this.mainHolder.getChildCount() * ((int) (this.mContext.getResources().getDimension(R.dimen.game_results_card_margin) / this.mContext.getResources().getDisplayMetrics().density))) + 10), 0, 0, 0);
        card.setLayoutParams(lp2);
        this.mainHolder.addView(card);

        /*LayoutParams params = new LayoutParams(card.getLayoutParams());
        params.leftMargin = (this.mainHolder.getChildCount() * ((int) (this.mContext.getResources().getDimension(R.dimen.game_results_card_margin) / this.mContext.getResources().getDisplayMetrics().density))) + 10;
        this.mainHolder.addView(card, params);*/
    }

    public void addDiscardCard(LinearLayout card)
    {
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams((int) convertDpToPixel(50.0f, getContext()), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(((this.mainHolder.getChildCount() * ((int) this.mContext.getResources().getDimension(R.dimen.discard_cards_card_margin))) + 10), 0, 0, 0);
        card.setLayoutParams(lp2);
        this.mainHolder.addView(card);


       /* LayoutParams params = new LayoutParams(card.getLayoutParams());
        params.leftMargin = (this.mainHolder.getChildCount() * ((int) this.mContext.getResources().getDimension(R.dimen.discard_cards_card_margin))) + 10;
        this.mainHolder.addView(card, params);*/
    }

    public ArrayList<RummyPlayingCard> getUpdateCardsSlots()
    {
        ArrayList<RummyPlayingCard> cardsList = new ArrayList();
        for (int i = 0; i < this.mainHolder.getChildCount(); i++) {
            View childView = this.mainHolder.getChildAt(i);
            if (childView.getTag() != null) {
                String[] cardsTag = childView.getTag().toString().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                RummyPlayingCard playingCard = new RummyPlayingCard();
                playingCard.setSuit(cardsTag[0]);
                playingCard.setFace(cardsTag[1]);
                playingCard.setSlot(String.valueOf(i));
                cardsList.add(playingCard);
            }
        }
        return cardsList;
    }

    public int getCurrentEmptyView()
    {
        ArrayList<RummyPlayingCard> cardsList = new ArrayList();
        for (int i = 0; i < this.mainHolder.getChildCount(); i++) {
            View childView = this.mainHolder.getChildAt(i);
            if (childView.getTag() == null) {
                RummyPlayingCard playingCard = new RummyPlayingCard();
                cardsList.add(playingCard);
            }
        }
        return cardsList.size();
    }

    public ArrayList<ArrayList<Integer>> getGroupStartEndMargin()
    {
        ArrayList<ArrayList<Integer>> final_array = new ArrayList();
        ArrayList<Integer> margin_array = new ArrayList<>();
        boolean isgroupstart = true;
        for (int i = 0; i < this.mainHolder.getChildCount(); i++) {
            View childView = this.mainHolder.getChildAt(i);
            if(isgroupstart && childView.getTag() != null)
            {
                margin_array = new ArrayList<>();
                margin_array.add(((RelativeLayout.LayoutParams)childView.getLayoutParams()).leftMargin);
                isgroupstart = false;
            }
            if (childView.getTag() == null) {
                if(i > 0)
                {
                    margin_array.add(((RelativeLayout.LayoutParams)this.mainHolder.getChildAt(i-1).getLayoutParams()).leftMargin);
                    // margin_array.add(this.mainHolder.getChildAt(i-1).getLeft());
                    isgroupstart = true;
                    final_array.add(margin_array);
                }

            }

            if(i == this.mainHolder.getChildCount()-1 && childView.getTag() != null)
            {

                margin_array.add(((RelativeLayout.LayoutParams)childView.getLayoutParams()).leftMargin);
                // margin_array.add(this.mainHolder.getChildAt(i-1).getLeft());
                isgroupstart = true;
                final_array.add(margin_array);

            }
        }
        return final_array;
    }

    public ArrayList<RummyPlayingCard> getUpdateCardsSlotsToShow(RummyPlayingCard temp)
    {
        ArrayList<RummyPlayingCard> cardsList = new ArrayList();
        for (int i = 0; i < this.mainHolder.getChildCount(); i++) {
            View childView = this.mainHolder.getChildAt(i);
            if (childView.getTag() != null) {
                String[] cardsTag = childView.getTag().toString().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                RummyPlayingCard playingCard = new RummyPlayingCard();
                playingCard.setSuit(cardsTag[0]);
                playingCard.setFace(cardsTag[1]);
                playingCard.setSlot(String.valueOf(i));

                if(!(playingCard.getFace()+playingCard.getSuit()).equalsIgnoreCase(temp.getFace()+temp.getSuit()))
                {
                    cardsList.add(playingCard);
                }
                else
                {
                        Log.d("local", "Card: " + playingCard.getFace() + playingCard.getSuit());
                        Log.d("local", "Card not added");
                }
            }
        }
        return cardsList;
    }

    public ArrayList<ArrayList<RummyPlayingCard>> getUpdatedCardsGroup()
    {
        int i;
        ArrayList<ArrayList<RummyPlayingCard>> groupList = new ArrayList();
        int groupCount = 1;
        ArrayList<String> tagsList = new ArrayList();

        for (i = 0; i < this.mainHolder.getChildCount(); i++)
        {
            String childTag;
            View childView = this.mainHolder.getChildAt(i);
            childView.clearAnimation();
            if (childView.getTag() == null) {
                groupCount++;
                childTag = BuildConfig.FLAVOR;
            } else {
                childTag = childView.getTag().toString();
            }
            tagsList.add(childTag);


        }
        int index = 0;

        for (i = 0; i < groupCount; i++)
        {
            ArrayList<String> groupTags = new ArrayList();
            ArrayList<RummyPlayingCard> cardsList = new ArrayList();
            for (int j = index; j < tagsList.size(); j++) {
                index++;
                String str = (String) tagsList.get(j);
                if (str.length() <= 0) {
                    break;
                }
                groupTags.add(str);
                String[] cardsTag = str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                RummyPlayingCard playingCard = new RummyPlayingCard();
                playingCard.setSuit(cardsTag[0]);
                playingCard.setFace(cardsTag[1]);
                playingCard.setIndex(String.valueOf(this.mainHolder.findViewWithTag(str).getId()));
                cardsList.add(playingCard);
            }
            if (cardsList.size() > 0) {
                groupList.add(cardsList);
            }
        }
        return groupList;
    }

    public LinearLayout getSmartCorrectionCard() {
        return (LinearLayout) this.inflater.inflate(R.layout.sc_card_view, this, false);
    }
}
