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
import in.glg.rummy.models.RummyGamePlayer;

/**
 * Created by GridLogic on 31/1/18.
 */

public class RummyWinnersAdapter extends RecyclerView.Adapter<RummyWinnersAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentDetailsFragment mTournamentDetailsFragment;
    private List<RummyGamePlayer> winners;

    public RummyWinnersAdapter(Context context, List<RummyGamePlayer> winners, RummyTournamentDetailsFragment tournamentDetailsFragment)
    {
        this.context = context;
        this.winners = winners;
        this.mTournamentDetailsFragment = tournamentDetailsFragment;
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
    public RummyWinnersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.rummy_prize_list_adapter_item, parent, false);

        return new RummyWinnersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyWinnersAdapter.MyViewHolder holder, final int position) {

        final RummyGamePlayer winner = winners.get(position);

        holder.position_tv.setText(winner.getRank());
        holder.prizeAmount_tv.setText(winner.getPrize_amount());
        holder.player_tv.setText(winner.getNick_name());

        holder.player_tv.setVisibility(View.VISIBLE);

        if(position%2==0)
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_grey));
        else
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_very_light_grey));
    }

    @Override
    public int getItemCount() {
        return winners.size();
    }


}
