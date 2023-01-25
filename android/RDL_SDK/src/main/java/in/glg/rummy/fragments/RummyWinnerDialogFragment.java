package in.glg.rummy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyEvent;

public class RummyWinnerDialogFragment extends DialogFragment
{
   private void removeDialog(RummyEvent event)
   {
      this.getDialog().dismiss();
      FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
      Fragment fragment = this.getFragmentManager().findFragmentByTag("winner" + event.getTableId());
      if(fragment != null) {
         ft.remove(fragment);
      }
   }

   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.rummy_dialog_winner, container, false);
      final RummyEvent event = (RummyEvent)this.getArguments().getSerializable("winnerEvent");

      TextView winnerTv = (TextView) view.findViewById(R.id.winner_name);
      TextView prizeMoneyTv = (TextView) view.findViewById(R.id.prize_money_tv);

      ((ImageView)view.findViewById(R.id.popUpCloseBtn)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            RummyWinnerDialogFragment.this.removeDialog(event);
         }
      });
      winnerTv.setText(event.getWinnerNickName());
      String rupeeSymbol = getActivity().getString(R.string.rupee_text);
      prizeMoneyTv.setText(String.format("%s  %s", new Object[]{rupeeSymbol, event.getPrizeMoney()}));
      return view;
   }

}
