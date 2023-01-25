package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.utils.RummyGamePlayerComparator;

public class RummyScoreBoardAdapter extends BaseAdapter {
   private Context mContext;
   private List<RummyEvent> mGameResultList;
   private LayoutInflater mLayoutInflater;

   public RummyScoreBoardAdapter(Context context, List<RummyEvent> gamePlayersList) {
      this.mContext = context;
      this.mGameResultList = gamePlayersList;
      this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
   }

   public int getCount() {
      return this.mGameResultList.size();
   }

   public Object getItem(int posiiton) {
      return null;
   }

   public long getItemId(int posiiton) {
      return 0L;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View v;
      if (convertView == null) {
         v = this.mLayoutInflater.inflate(R.layout.score_board_list_item, parent, false);
      } else {
         v = convertView;
      }
      TextView roundTv = (TextView) v.findViewById(R.id.sb_game_no_tv);
      TextView player1 = (TextView) v.findViewById(R.id.sb_player_1_tv);
      TextView player2 = (TextView) v.findViewById(R.id.sb_player_2_tv);
      TextView player3 = (TextView) v.findViewById(R.id.sb_player_3_tv);
      TextView player4 = (TextView) v.findViewById(R.id.sb_player_4_tv);
      TextView player5 = (TextView) v.findViewById(R.id.sb_player_5_tv);
      TextView player6 = (TextView) v.findViewById(R.id.sb_player_6_tv);
      roundTv.setText(String.valueOf(position + 1));
      RummyEvent event = (RummyEvent) this.mGameResultList.get(position);
      roundTv.setText(String.valueOf(position + 1));
      int playerCoount = event.getPlayer().size();
      List<RummyGamePlayer> gamePlayerList = event.getPlayer();
      Collections.sort(gamePlayerList, new RummyGamePlayerComparator());
      switch (playerCoount) {
         case 1:
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            break;
         case 2:
            RummyGamePlayer gamePlayer2 = (RummyGamePlayer) gamePlayerList.get(1);
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            player2.setText(WordUtils.capitalize(gamePlayer2.getScore()));
            break;
         case 3:
            RummyGamePlayer p2 = (RummyGamePlayer) gamePlayerList.get(1);
            RummyGamePlayer p3 = (RummyGamePlayer) gamePlayerList.get(2);
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            player2.setText(WordUtils.capitalize(p2.getScore()));
            player3.setText(WordUtils.capitalize(p3.getScore()));
            break;
         case 4:
            RummyGamePlayer p24 = (RummyGamePlayer) gamePlayerList.get(1);
            RummyGamePlayer p34 = (RummyGamePlayer) gamePlayerList.get(2);
            RummyGamePlayer p44 = (RummyGamePlayer) gamePlayerList.get(3);
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            player2.setText(WordUtils.capitalize(p24.getScore()));
            player3.setText(WordUtils.capitalize(p34.getScore()));
            player4.setText(WordUtils.capitalize(p44.getScore()));
            break;
         case 5:
            RummyGamePlayer p25 = (RummyGamePlayer) gamePlayerList.get(1);
            RummyGamePlayer p35 = (RummyGamePlayer) gamePlayerList.get(2);
            RummyGamePlayer p45 = (RummyGamePlayer) gamePlayerList.get(3);
            RummyGamePlayer p55 = (RummyGamePlayer) gamePlayerList.get(4);
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            player2.setText(WordUtils.capitalize(p25.getScore()));
            player3.setText(WordUtils.capitalize(p35.getScore()));
            player4.setText(WordUtils.capitalize(p45.getScore()));
            player5.setText(WordUtils.capitalize(p55.getScore()));
            break;
         case 6:
            RummyGamePlayer p26 = (RummyGamePlayer) gamePlayerList.get(1);
            RummyGamePlayer p36 = (RummyGamePlayer) gamePlayerList.get(2);
            RummyGamePlayer p46 = (RummyGamePlayer) gamePlayerList.get(3);
            RummyGamePlayer p56 = (RummyGamePlayer) gamePlayerList.get(4);
            RummyGamePlayer p66 = (RummyGamePlayer) gamePlayerList.get(5);
            player1.setText(WordUtils.capitalize(((RummyGamePlayer) gamePlayerList.get(0)).getScore()));
            player2.setText(WordUtils.capitalize(p26.getScore()));
            player3.setText(WordUtils.capitalize(p36.getScore()));
            player4.setText(WordUtils.capitalize(p46.getScore()));
            player5.setText(WordUtils.capitalize(p56.getScore()));
            player6.setText(WordUtils.capitalize(p66.getScore()));
            break;
      }
      return v;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }
}
