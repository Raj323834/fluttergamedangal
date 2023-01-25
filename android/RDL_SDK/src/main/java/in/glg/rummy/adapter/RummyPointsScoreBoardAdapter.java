package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyEvent;
import in.glg.rummy.models.RummyGamePlayer;

public class RummyPointsScoreBoardAdapter extends BaseAdapter {
   private Context mContext;
   private List<RummyEvent> mGameResultList;
   private LayoutInflater mLayoutInflater;
   private String mUserId;

   public RummyPointsScoreBoardAdapter(Context context, List<RummyEvent> gamePlayersList, String mUserId) {
      this.mContext = context;
      this.mGameResultList = gamePlayersList;
      this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
      this.mUserId = mUserId;
   }

   public int getCount() {
      return this.mGameResultList.size();
   }

   public Object getItem(int position) {
      return null;
   }

   public long getItemId(int position) {
      return 0L;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View v;
      if (convertView == null) {
         v = this.mLayoutInflater.inflate(R.layout.rummy_points_sb_list_item, parent, false);
      } else {
         v = convertView;
      }
      TextView gameId = (TextView) v.findViewById(R.id.sb_game_id_tv);
      TextView resultTv = (TextView) v.findViewById(R.id.sb_result_tv);
      TextView countTv = (TextView) v.findViewById(R.id.sb_count_tv);
      TextView scoreTv = (TextView) v.findViewById(R.id.sb_score_tv);
      ((TextView) v.findViewById(R.id.sb_round_tv)).setText(String.valueOf(position + 1));
      RummyEvent event = (RummyEvent) this.mGameResultList.get(position);
      gameId.setText(event.getGameId());
      for (RummyGamePlayer player : event.getPlayer()) {
         if (this.mUserId.equalsIgnoreCase(player.getUser_id())) {
            String score = player.getScore();
            if (player.getResult().equalsIgnoreCase("winner")) {
               score = String.format("%s%s", new Object[]{"+", score});
            } else {
               score = String.format("%s%s", new Object[]{"-", score});
            }
            String result = player.getResult();
            if (result != null) {
               if (result.equalsIgnoreCase("eliminate")) {
                  result = "Wrong Show";
               } else if (result.equalsIgnoreCase("meld_timeout") || result.equalsIgnoreCase("table_leave") || result.equalsIgnoreCase("meld")) {
                  result = "Lost";
               } else if (result.equalsIgnoreCase("drop") || result.equalsIgnoreCase("timeout")) {
                  result = "Dropped";
               }
               resultTv.setText(WordUtils.capitalize(result));
            }
            countTv.setText(player.getPoints());
            scoreTv.setText(score);
         }
      }
      return v;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }
}
