package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.fragments.RummyTournamentDetailsFragment;
import in.glg.rummy.models.RummyPrizeList;

/**
 * Created by GridLogic on 12/12/17.
 */

public class RummyPrizeListAdapter extends RecyclerView.Adapter<RummyPrizeListAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentDetailsFragment mTournamentDetailsFragment;
    private List<RummyPrizeList> prizeLists;
    private String status;

    public RummyPrizeListAdapter(Context context, List<RummyPrizeList> prizeLists, RummyTournamentDetailsFragment tournamentDetailsFragment, String status)
    {
        this.context = context;
        this.prizeLists = prizeLists;
        this.mTournamentDetailsFragment = tournamentDetailsFragment;
        this.status = status;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView position_tv ;
        TextView prizeAmount_tv;
        TextView player_tv;
        LinearLayout llLevMain;


        public MyViewHolder(View v) {
            super(v);

            position_tv = (TextView) v.findViewById(R.id.position_tv);
            prizeAmount_tv = (TextView) v.findViewById(R.id.prizeAmount_tv);
            player_tv = (TextView) v.findViewById(R.id.player_tv);
            llLevMain = (LinearLayout) v.findViewById(R.id.llLevMain);
        }
    }

    @Override
    public RummyPrizeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.rummy_prize_list_adapter_item, parent, false);

        return new RummyPrizeListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyPrizeListAdapter.MyViewHolder holder, final int position) {

        final RummyPrizeList prizeList = prizeLists.get(position);

        holder.position_tv.setText(prizeList.getRank());
        holder.prizeAmount_tv.setText(prizeList.getPrize_amount());

        if(status.equalsIgnoreCase("completed"))
        {

        }

        if(position%2==0)
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_grey));
        else
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_very_light_grey));
    }

    @Override
    public int getItemCount() {
        if(prizeLists != null)
        {
            return prizeLists.size();
        }
        else
        {
            return 0;
        }

    }

}
