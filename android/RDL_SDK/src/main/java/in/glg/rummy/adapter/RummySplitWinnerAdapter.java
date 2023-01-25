package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyGamePlayer;
import in.glg.rummy.utils.RummyUtils;

public class RummySplitWinnerAdapter extends BaseAdapter {
   private Context context;
   private List<RummyGamePlayer> gamePlayers;
   private LayoutInflater inflater;
   private boolean isFreeTable = false;

   public RummySplitWinnerAdapter(Context ctx, List<RummyGamePlayer> players, boolean isFreeTable) {
      this.context = ctx;
      this.gamePlayers = players;
      this.isFreeTable = isFreeTable;
      this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

   public int getCount() {
      return this.gamePlayers.size();
   }

   public RummyGamePlayer getItem(int position) {
      return (RummyGamePlayer)this.gamePlayers.get(position);
   }

   public long getItemId(int position) {
      return (long)position;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View winnerView;
      String prizeMoney;
      if (convertView == null) {
         winnerView = this.inflater.inflate(R.layout.split_winner_item, parent, false);
      } else {
         winnerView = convertView;
      }
      TextView prizeMoneyTv = (TextView) winnerView.findViewById(R.id.prize_money_tv);
      RummyGamePlayer gamePlayer = (RummyGamePlayer) this.gamePlayers.get(position);
      ((TextView) winnerView.findViewById(R.id.winner_name)).setText(gamePlayer.getNick_name());
      if (this.isFreeTable) {
         String str = "%s %s";
         Object[] objArr = new Object[2];
         objArr[0] = RummyUtils.formatPrizeMoney(gamePlayer.getAmount());
         objArr[1] = getCount() == 1 ? "(FREE)" : "";
         prizeMoney = String.format(str, objArr);
      } else {
         String rupeeSymbol = "";
         prizeMoney = String.format("%s  %s", new Object[]{rupeeSymbol, RummyUtils.formatPrizeMoney(gamePlayer.getAmount())});
      }
      prizeMoneyTv.setText(prizeMoney);
      return winnerView;
   }
}
