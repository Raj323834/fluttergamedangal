package in.glg.rummy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.fragments.RummyTournamentDetailsFragment;
import in.glg.rummy.fragments.RummyTournamentsFragment;
import in.glg.rummy.models.RummyTournament;
import in.glg.rummy.utils.RummyUtils;


/**
 * Created by GridLogic on 1/12/17.
 */

public class RummyJoinedTournamentsAdapter extends RecyclerView.Adapter<RummyJoinedTournamentsAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private RummyTournamentsFragment mTourneyFragment;
    private List<RummyTournament> tournaments;
    private List<RummyTournament> myFilteredList = null;  //
    CountDownTimer tournamentStartTimer;

    public RummyJoinedTournamentsAdapter(Context context, List<RummyTournament> tournaments, RummyTournamentsFragment tournamentsFragment) {
        this.context = context;
        this.tournaments = tournaments;
        this.mTourneyFragment = tournamentsFragment;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llMainLayout;
        LinearLayout llDetails;
        TextView entry_tv;
        TextView cashPrize_tv;
        TextView startTime_tv;
        //TextView status_tv ;
        TextView players_tv;
        TextView join_tv;
        TextView tid_tv;
        TextView tourney_name_tv;
        TextView entry_rate_tv;
        LinearLayout llJoin;


        public MyViewHolder(View v) {
            super(v);

            llMainLayout = (LinearLayout) v.findViewById(R.id.llMainLayout);
            llDetails = (LinearLayout) v.findViewById(R.id.llDetails);
            entry_tv = (TextView) v.findViewById(R.id.entry_tv);
            cashPrize_tv = (TextView) v.findViewById(R.id.cashPrize_tv);
            startTime_tv = (TextView) v.findViewById(R.id.startTime_tv);
            //TextView status_tv = (TextView) v.findViewById(R.id.status_tv);
            players_tv = (TextView) v.findViewById(R.id.players_tv);
            join_tv = (TextView) v.findViewById(R.id.join_tv);
            tid_tv = (TextView) v.findViewById(R.id.tvTid);
            tourney_name_tv = (TextView) v.findViewById(R.id.tvTourneyName);
            entry_rate_tv = (TextView) v.findViewById(R.id.ivEntryFee);
            llJoin = (LinearLayout) v.findViewById(R.id.llJoin);
        }
    }

    @Override
    public RummyJoinedTournamentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater
                .inflate(R.layout.tournament_adapter_item_new, parent, false);

        return new RummyJoinedTournamentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RummyJoinedTournamentsAdapter.MyViewHolder holder, final int position) {

        final RummyTournament tourney = tournaments.get(position);

        if (tourney.getEntry().equalsIgnoreCase("0") || tourney.getEntry().equalsIgnoreCase("0.0")) {
            holder.entry_tv.setText("Free");
            holder.entry_rate_tv.setText("");
            holder.cashPrize_tv.setText(tourney.getCashPrize());

        } else if (tourney.getTourneyCost()!=null &&tourney.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
            holder.entry_tv.setText("Loyalty");
            holder.entry_rate_tv.setText("");
            if (tourney.getEntry().equalsIgnoreCase("0") || tourney.getEntry().equalsIgnoreCase("0.0"))
                holder.cashPrize_tv.setText(tourney.getCashPrize());
            else
                holder.cashPrize_tv.setText(context.getResources().getString(R.string.rupee_symbol) + tourney.getCashPrize());
        } else {
            holder.entry_tv.setText(tourney.getEntry());
            holder.entry_rate_tv.setText("");
            holder.cashPrize_tv.setText(context.getResources().getString(R.string.rupee_symbol) + tourney.getCashPrize());

        }

        holder.tid_tv.setText(tourney.getTourneyId());
        //status_tv.setText(Utils.toTitleCase(tourney.getStatus()));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy hh:mm:ss aa");

            Date start = new Date();
            start = sdf.parse(sdf.format(start));
            Date end = sdf.parse(RummyUtils.convertTimeStampToAnyDateFormat(tourney.getStartDate(), "dd MM yyyy hh:mm:ss aa"));

            long millis = end.getTime() - start.getTime();

            if(millis>0 && millis<=3600000)  //lest than one hour
            {
                startTimer(holder.startTime_tv,millis);
                holder.startTime_tv.setTextColor(context.getResources().getColor(R.color.rummy_red));
            }
            else
            {
                holder.startTime_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                holder.startTime_tv.setText(RummyUtils.convertTimeStampToAnyDateFormat(tourney.getStartDate(), "dd MMM yy, hh:mm aa").toUpperCase());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.players_tv.setText(tourney.getPlayers());
        if (tourney.getTournamentName() != null) {
            if (tourney.getTournamentName().contains("_")) {
                String[] value = tourney.getTournamentName().split("_");
                if (value.length == 2) {
                    holder.tourney_name_tv.setText(value[0]);
                } else
                    holder.tourney_name_tv.setText(tourney.getTournamentName());
            } else {
                holder.tourney_name_tv.setText(tourney.getTournamentName());
            }

        }

        String tStatus = tourney.getStatus();
        if (tStatus != null && tStatus.length() > 0) {
            Log.e("gopal", "tourney status " + tStatus);
            Log.e("gopal", "Tourney Reg Status " + tourney.getReg_status());

            if(tStatus.equalsIgnoreCase("registration open"))
            {
                holder.join_tv.setEnabled(true);
                if (tourney.getReg_status().equalsIgnoreCase("true")) {
                    holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_blue_squre_selector_bg));
                    holder.join_tv.setText("DEREGISTER");
                    holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_white));
                } else if (tourney.getReg_status().equalsIgnoreCase("false")) {
                    holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_yellow_square_bg));
                    holder.join_tv.setText("JOIN >>");
                    holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                }
            }
            else
            {
                holder.join_tv.setEnabled(false);
                // holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_yellow_square_bg));
                switch (tStatus) {
                    case "registrations closed":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_blue_squre_selector_bg));
                        holder.join_tv.setText("JOINED");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_white));
                        break;
                    case "running":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_running_tourney));
                        holder.join_tv.setText("RUNNING");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                        break;
                    case "completed":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_running_tourney));
                        holder.join_tv.setText("COMPLETED");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                        break;
                    case "canceled":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_running_tourney));
                        holder.join_tv.setText("CANCELED");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                        break;
                    case "registrations":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_upcoming_tourney));
                        holder.join_tv.setText("UPCOMING");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_white));
                        break;
                    case "announced":
                        holder.join_tv.setBackground(context.getResources().getDrawable(R.drawable.rummy_btn_running_tourney));
                        holder.join_tv.setText("COMPLETED");
                        holder.join_tv.setTextColor(context.getResources().getColor(R.color.rummy_black));
                        break;

                }
            }

        } else {
            holder.join_tv.setEnabled(false);
        }

        holder.join_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tourney.getReg_status().equalsIgnoreCase("false")) {
                    RummyJoinedTournamentsAdapter.this.mTourneyFragment.openRegisterDeRegisterConfirmDialog(tourney, "register", "Are you sure you want\nto join this tournament?", position);
                } else {
                    RummyJoinedTournamentsAdapter.this.mTourneyFragment.openRegisterDeRegisterConfirmDialog(tourney, "deregister", "Are you sure you want to withdraw\nfrom this tournament?", position);
                }


            }
        });

        holder.llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RummyTournamentDetailsFragment tdf = new RummyTournamentDetailsFragment();
                Bundle args = new Bundle();
                args.putString("tourneyID", tourney.getTourneyId());
                tdf.setArguments(args);
                RummyJoinedTournamentsAdapter.this.mTourneyFragment.launchTDFragment(tdf, RummyTournamentDetailsFragment.class.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tournaments.size();
    }


    public void addNewItem() {
        notifyDataSetChanged();
    }

    private void startTimer(final TextView textView, long millis)
    {

        if(millis>0) {
            this.tournamentStartTimer = new CountDownTimer(millis, 1000) {

                public void onTick(long millisUntilFinished) {

                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;

                    String dayVal = "00", hourVal = "00", minVal = "00", secVal = "00";
                    if (days + "".length() == 1)
                        dayVal = "0" + days;
                    else
                        dayVal = days + "";
                    if ((hours % 24) + "".length() == 1)
                        hourVal = "0" + (hours % 24);
                    else
                        hourVal = (hours % 24) + "";
                    if ((minutes % 60) + "".length() == 1)
                        minVal = "0" + (minutes % 60);
                    else
                        minVal = (minutes % 60) + "";
                    if ((seconds % 60) + "".length() == 1)
                        secVal = "0" + (seconds % 60);
                    else
                        secVal = (seconds % 60) + "";

                    textView.setText(minVal+"m:"+secVal+"s");
                }

                public void onFinish() {

                }

            }.start();
        }
    }


    public void filter(String filterText, List<RummyTournament> actualList) {
        List<RummyTournament> tempList = new ArrayList<>();
        myFilteredList = actualList;
        if (filterText != null && filterText.length() > 0) {

            if (filterText.equalsIgnoreCase("ALL")) {
                tournaments = actualList;
            }  else if (filterText.equalsIgnoreCase("CASH")) {
                for (RummyTournament tournament : myFilteredList) {
                    if (!tournament.getEntry().equalsIgnoreCase("0") &&
                            !tournament.getEntry().equalsIgnoreCase("0.0") &&
                            !tournament.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
                        tempList.add(tournament);
                    }
                }
                tournaments = tempList;
            } else {
                for (RummyTournament tournament : myFilteredList) {
                    if (tournament.getEntry().equalsIgnoreCase("0") ||
                            tournament.getEntry().equalsIgnoreCase("0.0") ||
                            tournament.getTourneyCost().equalsIgnoreCase("LOYALTYPOINTS_CASH")) {
                        tempList.add(tournament);
                    }
                }
                tournaments = tempList;
            }
            notifyDataSetChanged();

        }
    }

}
