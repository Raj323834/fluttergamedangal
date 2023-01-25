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

/**
 * Created by GridLogic on 25/1/18.
 */

public class RummyTourneyResultsAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private List<RummyGamePlayer> results;

    public RummyTourneyResultsAdapter(Context context, List<RummyGamePlayer> results)
    {
        this.context = context;
        this.results = results;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.results.size();
    }

    @Override
    public RummyGamePlayer getItem(int position) {
        return (RummyGamePlayer) this.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;
        if (convertView == null) {
            v = this.inflater.inflate(R.layout.tourney_results_adapter_item, parent, false);
        } else {
            v = convertView;
        }
        final RummyGamePlayer resultItem = getItem(position);

        TextView nickName_tv = (TextView) v.findViewById(R.id.nickName_tv);
        TextView prize_tv = (TextView) v.findViewById(R.id.prize_tv);

        nickName_tv.setText(resultItem.getNick_name());
        prize_tv.setText(resultItem.getPrize_amount());

        return v;
    }



    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
