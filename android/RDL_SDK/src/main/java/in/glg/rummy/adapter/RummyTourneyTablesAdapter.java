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
import in.glg.rummy.models.RummyTournamentTables;

/**
 * Created by GridLogic on 12/2/18.
 */

public class RummyTourneyTablesAdapter extends RecyclerView.Adapter<RummyTourneyTablesAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<RummyTournamentTables> results;

    public RummyTourneyTablesAdapter(Context context, List<RummyTournamentTables> results)
    {
        this.context = context;
        this.results = results;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tableId ;
        TextView nickName;
        TextView high ;
        TextView low ;
        LinearLayout llLevMain;


        public MyViewHolder(View v) {
            super(v);

            tableId = (TextView) v.findViewById(R.id.tableId);
            nickName = (TextView) v.findViewById(R.id.nickName);
            high = (TextView) v.findViewById(R.id.high);
            low = (TextView) v.findViewById(R.id.low);
            llLevMain = (LinearLayout) v.findViewById(R.id.llLevMain);
        }
    }

    @Override
    public RummyTourneyTablesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.tourney_tables_adapter_item, parent, false);

        return new RummyTourneyTablesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyTourneyTablesAdapter.MyViewHolder holder, final int position) {

        final RummyTournamentTables resultItem = results.get(position);

        holder.nickName.setText(String.valueOf(resultItem.getPlayers().size()));
        holder.tableId.setText(resultItem.getTable_id());
        holder.high.setText(resultItem.getHigh());
        holder.low.setText(resultItem.getLow());

        if(position%2==0)
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_grey));
        else
            holder.llLevMain.setBackgroundColor(context.getResources().getColor(R.color.rummy_very_light_grey));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

}
