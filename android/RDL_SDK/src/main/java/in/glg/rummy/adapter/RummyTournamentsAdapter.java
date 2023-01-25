package in.glg.rummy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.fragments.RummyTournamentDetailsFragment;
import in.glg.rummy.fragments.RummyTournamentsFragment;
import in.glg.rummy.models.RummyTournament;
import in.glg.rummy.utils.RummyUtils;


/**
 * Created by GridLogic on 1/12/17.
 */

public class RummyTournamentsAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentsFragment mTourneyFragment;
    private List<RummyTournament> tournaments;
    private List<RummyTournament> myFilteredList=null;  //

    public RummyTournamentsAdapter(Context context, List<RummyTournament> tournaments, RummyTournamentsFragment tournamentsFragment)
    {
        this.context = context;
        this.tournaments = tournaments;
        this.mTourneyFragment = tournamentsFragment;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.tournaments.size();
    }

    @Override
    public RummyTournament getItem(int position) {
        return (RummyTournament) this.tournaments.get(position);
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
            v = this.inflater.inflate(R.layout.tournament_adapter_item_new, parent, false);
        } else {
            v = convertView;
        }
        final RummyTournament tourney = getItem(position);

     //   LinearLayout llMainLayout = (LinearLayout) v.findViewById(R.id.llMainLayout);
        TextView entry_tv = (TextView) v.findViewById(R.id.entry_tv);
        TextView cashPrize_tv = (TextView) v.findViewById(R.id.cashPrize_tv);
        TextView startTime_tv = (TextView) v.findViewById(R.id.startTime_tv);
        //TextView status_tv = (TextView) v.findViewById(R.id.status_tv);
        TextView players_tv = (TextView) v.findViewById(R.id.players_tv);
        TextView join_tv = (TextView) v.findViewById(R.id.join_tv);

        if(tourney.getEntry().equalsIgnoreCase("0") || tourney.getEntry().equalsIgnoreCase("0.0"))
            entry_tv.setText("Free");
        else
            entry_tv.setText(tourney.getEntry());

        cashPrize_tv.setText(context.getResources().getString(R.string.rupee_symbol)+tourney.getCashPrize());
        startTime_tv.setText(RummyUtils.convertTimeStampToAnyDateFormat(tourney.getStartDate(), "dd MMM yy, hh:mm aa"));
        //status_tv.setText(Utils.toTitleCase(tourney.getStatus()));
        players_tv.setText(tourney.getPlayers());
        //join_tv.setTextColor(ContextCompat.getColor(this.context, R.color.rummy_colorAccent));

        if(tourney.getStatus().equalsIgnoreCase("running") || tourney.getStatus().equalsIgnoreCase("completed")
                || tourney.getStatus().equalsIgnoreCase("canceled") || tourney.getStatus().equalsIgnoreCase("registrations closed")) {
            join_tv.setText("VIEW");
            join_tv.setTextColor(context.getResources().getColor(R.color.rummy_white));

            setViewBackgroundWithoutResettingPadding(join_tv,context.getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_less_round));
          //  join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_curve_edges_filled_app_blue_light_less_round));
        }
        else {
            join_tv.setText("JOIN");
            join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));

            setViewBackgroundWithoutResettingPadding(join_tv,context.getResources().getDrawable(R.drawable.rummy_curve_edges_filled_orange_less_round));
           // join_tv.setBackground();
        }

      /*  if(tourney.getReg_status()!=null && tourney.getReg_status().equalsIgnoreCase("True"))
            llMainLayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.rummy_transparent));
        else
            llMainLayout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.rummy_transparent));*/

        join_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                RummyTournamentDetailsFragment tdf = new RummyTournamentDetailsFragment();
                Bundle args = new Bundle();
                args.putString("tourneyID", tourney.getTourneyId());
                tdf.setArguments(args);
                RummyTournamentsAdapter.this.mTourneyFragment.launchTDFragment(tdf, RummyTournamentDetailsFragment.class.getName());
            }
        });

        return v;
    }

    public void setViewBackgroundWithoutResettingPadding(final View v, final Drawable backgroundResId) {
        final int paddingBottom = v.getPaddingBottom(), paddingLeft = v.getPaddingLeft();
        final int paddingRight = v.getPaddingRight(), paddingTop = v.getPaddingTop();
        v.setBackground(backgroundResId);
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void addNewItem(RummyTournament tournament){
        //this.tournaments.add(tournament);
        notifyDataSetChanged();
    }

    public void filter(String filterText, List<RummyTournament> actualList)
    {
        List<RummyTournament> tempList = new ArrayList<>();
        myFilteredList = actualList;

        if (filterText.equalsIgnoreCase("ALL")) {
            tournaments = actualList;
        }
        else if(filterText.equalsIgnoreCase("FREE"))
        {
            for (RummyTournament tournament : myFilteredList) {
                if (tournament.getEntry().equalsIgnoreCase("0")) {
                    tempList.add(tournament);
                }
            }
            tournaments = tempList;
        }
        else if(filterText.equalsIgnoreCase("CASH"))
        {
            for (RummyTournament tournament : myFilteredList) {
                if (!tournament.getEntry().equalsIgnoreCase("0")) {
                    tempList.add(tournament);

                }
            }
            tournaments = tempList;
        }
        else if(filterText.equalsIgnoreCase("JOINED"))
        {
            for (RummyTournament tournament : myFilteredList) {
                if (tournament.getReg_status().equalsIgnoreCase("TRUE")) {
                    tempList.add(tournament);

                }
            }
            tournaments = tempList;
        }
        notifyDataSetChanged();
    }
}
