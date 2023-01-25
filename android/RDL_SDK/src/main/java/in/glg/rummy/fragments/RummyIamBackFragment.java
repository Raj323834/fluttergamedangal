package in.glg.rummy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.GameRoom.RummyTableActivity;
import in.glg.rummy.R;
import in.glg.rummy.RummyApplication;
import in.glg.rummy.adapter.RummyIamBackAdapter;
import in.glg.rummy.api.RummyOnResponseListener;
import in.glg.rummy.api.requests.RummyTableDetailsRequest;
import in.glg.rummy.api.response.RummyJoinTableResponse;
import in.glg.rummy.engine.RummyGameEngine;
import in.glg.rummy.exceptions.RummyGameEngineNotRunning;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyJoinedTable;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.utils.RummyConstants;
import in.glg.rummy.utils.RummyPrefManager;
import in.glg.rummy.utils.RummyTLog;
import in.glg.rummy.utils.RummyUtils;

public class RummyIamBackFragment extends RummyBaseFragment {
   private static final String TAG = RummyTablesFragment.class.getSimpleName();
   private RummyIamBackAdapter adapter;
   private List<RummyPlayingCard> cards;
   private Button iamBackBtn;
   private RummyOnResponseListener iamBackListner = new RummyOnResponseListener(RummyJoinTableResponse.class) {
      public void onResponse(Object response) {
         if (((RummyTableActivity) RummyIamBackFragment.this.getActivity()) != null) {
            ((RummyTableActivity) RummyIamBackFragment.this.getActivity()).setIamBackFlag();
            ((RummyTableActivity) RummyIamBackFragment.this.getActivity()).showGameTablesLayoutOnImaBack();
         }
      }
   };
   private List<RummyJoinedTable> mJoinedTableIds = new ArrayList();
   private RummyPlayingCard mJokerCard;
   private RecyclerView mRecyclerView;
   private TextView mSubTitle;
   private TextView mTitle;

   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);
      Log.w("testing", "I am Back fragment Started");
      View v = inflater.inflate(R.layout.rummy_fragment_iam_back, container, false);
      this.cards = new ArrayList();
      this.mRecyclerView = (RecyclerView) v.findViewById(R.id.cards_rv);
      this.mTitle = (TextView) v.findViewById(R.id.iam_back_title);
      this.mSubTitle = (TextView) v.findViewById(R.id.iam_back_sub_title);
      v.setFocusable(true);
      v.setFocusableInTouchMode(true);
      this.iamBackBtn = (Button) v.findViewById(R.id.iam_back_btn);
      this.iamBackBtn.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
            RummyIamBackFragment.this.sendIamBack();
            if(!RummyUtils.temp_last_auto_drop_card.equalsIgnoreCase("") && getActivity() != null)
            {
               RummyPrefManager.saveString(getActivity(), RummyConstants.PREFS_Last_Auto_discarded_card, RummyUtils.temp_last_auto_drop_card);
            }
         }
      });
      setPlayerDiscards();
      this.mJoinedTableIds = (RummyApplication.getInstance()).getJoinedTableIds();
      return v;
   }

   @SuppressLint("WrongConstant")
   private void setPlayerDiscards()
   {
      this.adapter = new RummyIamBackAdapter(this.cards, getContext(), this.mJokerCard);

      this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
      this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
      this.mRecyclerView.setAdapter(this.adapter);

      clearDiscardedCards();
   }

   public void showAutoPlayCards(RummyPlayingCard card, RummyEvent event)
   {
      this.cards.add(card);
      this.adapter.notifyDataSetChanged();
      if (this.cards != null && this.cards.size() > 0) {
         this.mTitle.setText("Your game was set to Auto Play and " + this.cards.size() + "/" + 5 + " turns have been played and below are the discarded cards.Please click on the I am back button to continue with your game.");
      }
   }

   public void disableIamBackButton() {
      this.iamBackBtn.setEnabled(false);
      this.iamBackBtn.setClickable(false);
      this.mSubTitle.setText(R.string.iam_back_error_info);
   }

   public void enableIamBackButton() {
      this.iamBackBtn.setEnabled(true);
      this.iamBackBtn.setClickable(true);
      this.mSubTitle.setText(getString(R.string.info_iam_back));
   }

   private void popFragment() {
      getActivity().getSupportFragmentManager().popBackStack(RummyIamBackFragment.class.getName(), 1);
   }

   private void sendIamBack() {
      RummyApplication app = RummyApplication.getInstance();
      RummyTableDetailsRequest request = new RummyTableDetailsRequest();
      request.setCommand("autoplaystatus");
      request.setUuid(RummyUtils.generateUuid());
      request.setUserId(String.valueOf(app.getUserData().getUserId()));
      try {
         RummyGameEngine.getInstance();
         RummyGameEngine.sendRequestToEngine(getActivity().getApplicationContext(), RummyUtils.getObjXMl(request), this.iamBackListner);
      } catch (RummyGameEngineNotRunning gameEngineNotRunning) {
         RummyTLog.d(TAG, "sendIamBack" + gameEngineNotRunning.getLocalizedMessage());
      }
   }

   public void onSaveInstanceState(Bundle outState) {
   }

   public void clearDiscardedCards() {
      if (this.cards != null) {
         this.cards.clear();
         this.adapter.notifyDataSetChanged();
      }
      this.iamBackBtn.setEnabled(true);
      this.iamBackBtn.setClickable(true);
      this.mSubTitle.setText(getString(R.string.info_iam_back));
      this.mTitle.setText(getString(R.string.info_msg_iam_back));
   }

   public void resetAutoPlayCards(RummyGamePlayer gamePlayer) {
      String autoPlay = gamePlayer.getAutoplay();
      String autoPlayCountStr = gamePlayer.getAutoplay_count();
      if (autoPlay != null && autoPlay.equalsIgnoreCase("True") && autoPlayCountStr != null && Integer.parseInt(autoPlayCountStr) <= 1) {
         clearDiscardedCards();
      }
   }

   public void setJokerCard(RummyPlayingCard jokerCard) {
      this.mJokerCard = jokerCard;
      if (this.adapter != null) {
         this.adapter.setJokerCard(jokerCard);
      }
      this.adapter.notifyDataSetChanged();
   }
}
