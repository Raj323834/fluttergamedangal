package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyCard;
import in.glg.rummy.models.RummyPlayingCard;
import in.glg.rummy.view.RummyView;

public class RummyPlayerDiscardCardsAdapter extends BaseAdapter {
   private Context context;
   private LayoutInflater inflater;
   private HashMap<String, List<RummyPlayingCard>> mPlayersMap;
   private String[] mUserNames;

   public RummyPlayerDiscardCardsAdapter(Context ctx, HashMap<String, List<RummyPlayingCard>> playersMap) {
      this.context = ctx;
      this.mPlayersMap = playersMap;
      this.inflater = (LayoutInflater) ctx.getSystemService("layout_inflater");
      this.mUserNames = (String[]) playersMap.keySet().toArray(new String[playersMap.size()]);
   }

   private void addCardToRummyView(RummyPlayingCard playerCard, RummyView view) {
      RummyCard c = RummyCard.getSampleCard();
      LinearLayout card = view.getPlayerDiscardCard();
      ImageView cardImg = (ImageView) card.findViewById(R.id.cardImageView);
      int imgId = this.context.getResources().getIdentifier(String.format("%s%s", new Object[]{playerCard.getSuit(), playerCard.getFace()}), "drawable", this.context.getPackageName());
      cardImg.setVisibility(View.VISIBLE);
      cardImg.setImageResource(imgId);
      view.addDiscardCard(card);
   }

   public int getCount() {
      return this.mPlayersMap.size();
   }

   public List<RummyPlayingCard> getItem(int position) {
      return (List)this.mPlayersMap.get(this.mUserNames[position]);
   }

   public long getItemId(int position) {
      return (long)position;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View v;
      List<RummyPlayingCard> playerCards = getItem(position);
      if (convertView == null) {
         v = this.inflater.inflate(R.layout.rummy_player_discard_grid_item, parent, false);
      } else {
         v = convertView;
      }
      RummyView discardsView = (RummyView) v.findViewById(R.id.discard_player_cards_view);
      ((TextView) v.findViewById(R.id.discard_player_name_tv)).setText(this.mUserNames[position]);
      discardsView.removeViews();
      if (playerCards != null && playerCards.size() > 0) {
         for (int i = playerCards.size() - 1; i >= 0; i--) {
            addCardToRummyView((RummyPlayingCard) playerCards.get(i), discardsView);
         }
         discardsView.addGameResultCard(discardsView.getGameResultCard());
      }
      return v;
   }
}
