package in.glg.rummy.adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.models.RummyMeldBox;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.utils.RummyUtils;
import in.glg.rummy.view.RummyView;

public class RummyGameResultAdapter extends BaseAdapter {
   private Context context;
   private LayoutInflater inflater;
   private RummyEvent mEvent;
   private RummyPlayingCard mJokerCard;
   private ArrayList<RummyGamePlayer> mPlayersList;
   private boolean allUsersDropped = false;
   private boolean lostAny = false;
   private int dropCount = 0;
   private int timeoutCount = 0;
   private int dropTimeoutCount = 0;

   public RummyGameResultAdapter(Context ctx, RummyEvent event) {
      this.context = ctx;
      this.mPlayersList = (ArrayList) event.getPlayer();
      this.mEvent = event;
      this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      for(RummyGamePlayer gamePlayer : mPlayersList){
         if (gamePlayer.getResult().equalsIgnoreCase("drop")
                 || gamePlayer.getResult().equalsIgnoreCase("eliminate")
                 || gamePlayer.getResult().equalsIgnoreCase("table_leave") ) {
            dropCount++;
            dropTimeoutCount++;
         }
         else if(gamePlayer.getResult().equalsIgnoreCase("timeout")) {
            timeoutCount++;
            dropTimeoutCount++;
         }
         else if (gamePlayer.getResult().equalsIgnoreCase("meld_timeout")
                 ||gamePlayer.getResult().equalsIgnoreCase("meld"))
            lostAny = true;
      }
      if(dropCount==(mPlayersList.size()-1))
         allUsersDropped = true;

      setJokerCard(event);
   }

   private void addCardToRummyView(RummyPlayingCard playerCard, RummyView view) {
      LinearLayout card = view.getGameResultCard();
      ImageView cardImg = (ImageView) card.findViewById(R.id.cardImageView);
      ImageView jockerCardImg = (ImageView) card.findViewById(R.id.game_results_jokerCardImg);
      int imgId = this.context.getResources().getIdentifier(String.format("%s%s", new Object[]{playerCard.getSuit(), playerCard.getFace()}), "drawable", this.context.getPackageName());
      cardImg.setVisibility(View.VISIBLE);
      cardImg.setImageResource(imgId);
      if (this.mJokerCard != null) {
         if (this.mJokerCard.getFace().equalsIgnoreCase(playerCard.getFace())) {
            jockerCardImg.setVisibility(View.VISIBLE);
         } else if (playerCard == null || !playerCard.getFace().equalsIgnoreCase("1")) {
            jockerCardImg.setVisibility(View.GONE);
         } else if (this.mJokerCard.getFace().equalsIgnoreCase("0")) {
            jockerCardImg.setVisibility(View.VISIBLE);
         } else {
            jockerCardImg.setVisibility(View.GONE);
         }
      }
      view.addGameResultCard(card);
   }

   private void setJokerCard(RummyEvent event) {
      RummyPlayingCard jokerCard = new RummyPlayingCard();
      jokerCard.setFace(event.getFace());
      jokerCard.setSuit(event.getSuit());
      this.mJokerCard = jokerCard;
   }

   public int getCount() {
      return this.mPlayersList.size();
   }

   public RummyGamePlayer getItem(int position) {
      return (RummyGamePlayer)this.mPlayersList.get(position);
   }

   public long getItemId(int position) {
      return (long)position;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View v;
      RummyGamePlayer player = getItem(position);
      if (convertView == null) {
         v = this.inflater.inflate(R.layout.rummy_game_result_item, parent, false);
      } else {
         v = convertView;
      }

      LinearLayout llContainer = (LinearLayout) v.findViewById(R.id.llContainer);
      TextView playerName = (TextView) v.findViewById(R.id.player_name_tv);
      TextView playerStatus = (TextView) v.findViewById(R.id.player_status_tv);
      TextView totalScore = (TextView) v.findViewById(R.id.player_total_count_tv);
      TextView pointsTv = (TextView) v.findViewById(R.id.player_count_tv);
      TextView progressTv = (TextView) v.findViewById(R.id.game_results_waiting_tv);
      ImageView aiIv = (ImageView) v.findViewById(R.id.ai_iv);
      ImageView ivWinnerImg = (ImageView) v.findViewById(R.id.iv_winner_img);
      ImageView scIv = (ImageView) v.findViewById(R.id.sc_iv);
      playerName.setText(player.getNick_name());
      String result = player.getResult();
      String aiEnable = player.getAiEnable();
      String scUse = player.getScUse();
      if (scUse == null || !scUse.equalsIgnoreCase("True")) {
         scIv.setVisibility(View.GONE);
      } else {
         scIv.setVisibility(View.VISIBLE);
      }
      if (aiEnable.equalsIgnoreCase("True")) {
         aiIv.setVisibility(View.VISIBLE);
      } else {
         aiIv.setVisibility(View.GONE);
      }
      RummyView rummyView = (RummyView) v.findViewById(R.id.player_cards_view);
      rummyView.removeViews();
      rummyView.invalidate();
      if (result != null) {
         if (result.equalsIgnoreCase("drop") || result.equalsIgnoreCase("timeout")) {
            rummyView.setVisibility(View.INVISIBLE);
         } else {
            rummyView.setVisibility(View.VISIBLE);
         }

         if(result.equalsIgnoreCase("winner") && allUsersDropped==true)
            rummyView.setVisibility(View.INVISIBLE);


         if(result.equalsIgnoreCase("winner") && lostAny==true)
            rummyView.setVisibility(View.VISIBLE);

         if(timeoutCount==(mPlayersList.size()-1))
            rummyView.setVisibility(View.INVISIBLE);

         if(dropTimeoutCount==(mPlayersList.size()-1))
            rummyView.setVisibility(View.INVISIBLE);

         if (result.equalsIgnoreCase("eliminate")) {
            result = "Wrong Show";
         } else if (result.equalsIgnoreCase("meld_timeout") || result.equalsIgnoreCase("table_leave") || result.equalsIgnoreCase("meld")) {
            result = "Lost";
         } else if (result.equalsIgnoreCase("drop") || result.equalsIgnoreCase("timeout")) {
            result = "Dropped";
         }

         if (result.equalsIgnoreCase("winner")) {
            result = "winner";
            llContainer.setBackgroundColor(context.getResources().getColor(R.color.rummy_app_blue_dark));
            playerName.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_white));
            pointsTv.setTextColor(context.getResources().getColor(R.color.rummy_white));
            totalScore.setTextColor(context.getResources().getColor(R.color.rummy_white));
            ivWinnerImg.setVisibility(View.VISIBLE);
            playerStatus.setVisibility(View.GONE);

            playerStatus.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_highlight_font_color));

         } else {
            llContainer.setBackgroundColor(context.getResources().getColor(R.color.rummy_white));
            playerStatus.setText(WordUtils.capitalize(result));
            playerName.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_black));
            pointsTv.setTextColor(context.getResources().getColor(R.color.rummy_black));
            totalScore.setTextColor(context.getResources().getColor(R.color.rummy_black));

            ivWinnerImg.setVisibility(View.GONE);
            playerStatus.setVisibility(View.VISIBLE);

            playerStatus.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_black));
         }
         playerStatus.setMovementMethod(new ScrollingMovementMethod());
      }
      if (this.mEvent.getTableType().contains(RummyUtils.PR)) {
         String score = player.getScore();
         if (player.getResult().equalsIgnoreCase("winner")) {
            score = String.format("%s%s", new Object[]{"+", score});
         } else {
            score = String.format("%s%s", new Object[]{"-", score});
         }
         pointsTv.setText(player.getPoints());
         totalScore.setText(score);
      } else {
         String points = RummyUtils.formatString(player.getPoints());
         if (points != null) {
            pointsTv.setText(points);
         }
         if (player.getTotalScore() != null) {
            totalScore.setText(RummyUtils.formatString(player.getTotalScore()));
         }
      }
      List<RummyMeldBox> meldBox = player.getMeldList();
      if (meldBox != null) {
         progressTv.setVisibility(View.GONE);
         for (RummyMeldBox box : meldBox) {
            List<RummyPlayingCard> cards = box.getMeldBoxes();
            if (cards != null && cards.size() > 0) {
               for (RummyPlayingCard card : cards) {
                  addCardToRummyView(card, rummyView);
               }
               rummyView.addGameResultCard(rummyView.getGameResultCard());
            }
         }
      } else {
         progressTv.setVisibility(View.VISIBLE);
         playerStatus.setText("");
         pointsTv.setText("");
         totalScore.setText("");
      }
      return v;
   }
}
