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
import in.glg.rummy.models.RummyWaitingPlayers;

/**
 * Created by GridLogic on 28/12/17.
 */

public class RummyWaitingPlayersAdapter extends RecyclerView.Adapter<RummyWaitingPlayersAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentDetailsFragment mTournamentDetailsFragment;
    private List<RummyWaitingPlayers> waitingPlayers;


    public RummyWaitingPlayersAdapter(Context context, List<RummyWaitingPlayers> waitingPlayers, RummyTournamentDetailsFragment tournamentDetailsFragment)
    {
        this.context = context;
        this.waitingPlayers = waitingPlayers;
        this.mTournamentDetailsFragment = tournamentDetailsFragment;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView serialNo_tv ;
        TextView nickName_tv ;
        LinearLayout llLevMain;


        public MyViewHolder(View v) {
            super(v);

            serialNo_tv = (TextView) v.findViewById(R.id.serialNo_tv);
            nickName_tv = (TextView) v.findViewById(R.id.nickName_tv);
            llLevMain = (LinearLayout) v.findViewById(R.id.llLevMain);
        }
    }
    @Override
    public RummyWaitingPlayersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.rummy_joined_players_adapter_item, parent, false);

        return new RummyWaitingPlayersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyWaitingPlayersAdapter.MyViewHolder holder, final int position) {

        final RummyWaitingPlayers waitingPlayer = waitingPlayers.get(position);

        holder.serialNo_tv.setText(String.valueOf(position+1));
        holder.nickName_tv.setText(waitingPlayer.getNick_name());

        if(position%2==0)
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_grey));
        else
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_very_light_grey));
    }

    @Override
    public int getItemCount() {
        return waitingPlayers.size();
    }

}
