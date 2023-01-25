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
import in.glg.rummy.models.RummyLevels;
import in.glg.rummy.utils.RummyUtils;

/**
 * Created by GridLogic on 12/12/17.
 */

public class RummyLevelsAdapter extends RecyclerView.Adapter<RummyLevelsAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentDetailsFragment mTournamentDetailsFragment;
    private List<RummyLevels> levels;
    private String status;


    public RummyLevelsAdapter(Context context, List<RummyLevels> levels, RummyTournamentDetailsFragment tournamentDetailsFragment)
    {
        this.context = context;
        this.levels = levels;
        this.mTournamentDetailsFragment = tournamentDetailsFragment;
        this.status = status;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView level_tv ;
        TextView scheduleTime_tv;
        TextView bet_tv ;
        TextView qualify_tv ;
        TextView rebuy_tv ;
        LinearLayout llLevMain ;


        public MyViewHolder(View v) {
            super(v);

            level_tv = (TextView) v.findViewById(R.id.level_tv);
            scheduleTime_tv = (TextView) v.findViewById(R.id.scheduleTime_tv);
            bet_tv = (TextView) v.findViewById(R.id.bet_tv);
            qualify_tv = (TextView) v.findViewById(R.id.qualify_tv);
            rebuy_tv = (TextView) v.findViewById(R.id.rebuy_tv);
            llLevMain = (LinearLayout) v.findViewById(R.id.llLevMain);
        }
    }

    @Override
    public RummyLevelsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.tourney_levels_adapter_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyLevelsAdapter.MyViewHolder holder, final int position) {

        final RummyLevels level = levels.get(position);

        holder.level_tv.setText(String.valueOf(position+1));
        holder.scheduleTime_tv.setText(RummyUtils.convertTimeStampToAnyDateFormat(level.getStart_time(), "hh:mm") + " to "+
                RummyUtils.addSecondsToDate(level.getStart_time(), "hh:mm", level.getTime_duration()));
        holder.bet_tv.setText(level.getBet());
        holder.qualify_tv.setText(level.getQualifying_points());

        if(level.getLevel_buying().equalsIgnoreCase("0.00") || level.getLevel_buying().equalsIgnoreCase("0.0") ||
                level.getLevel_buying().equalsIgnoreCase("0"))
            holder.rebuy_tv.setText("NA");
        else
            holder.rebuy_tv.setText(level.getLevel_buying());

        if(position%2==0)
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_grey));
        else
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_very_light_grey));
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }
}
