package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.glg.rummy.R;
import in.glg.rummy.models.RummyPlayingCard;

public class RummyIamBackAdapter extends RecyclerView.Adapter<RummyIamBackAdapter.PickCardViewHolder> {
   private List<RummyPlayingCard> mCardsList;
   private Context mContext;
   private RummyPlayingCard mJokerCard;

   public RummyIamBackAdapter(List<RummyPlayingCard> cardsList, Context context, RummyPlayingCard jokerCard) {
      this.mCardsList = cardsList;
      this.mContext = context;
      this.mJokerCard = jokerCard;
   }

   public int getItemCount() {
      return this.mCardsList.size();
   }

   public void onBindViewHolder(PickCardViewHolder holder, int position) {
      RummyPlayingCard card = (RummyPlayingCard) this.mCardsList.get(position);
      int imgId = this.mContext.getResources().getIdentifier(String.format("%s%s", new Object[]{card.getSuit(), card.getFace()}), "drawable", this.mContext.getPackageName());
      holder.cardImage.setVisibility(View.VISIBLE);
      if (this.mJokerCard != null) {
         if (card != null && card.getFace().equalsIgnoreCase(this.mJokerCard.getFace())) {
            holder.jokerIv.setVisibility(View.VISIBLE);
         } else if (card == null || !card.getFace().equalsIgnoreCase("1")) {
            holder.jokerIv.setVisibility(View.GONE);
         } else if (this.mJokerCard.getFace().equalsIgnoreCase("0")) {
            holder.jokerIv.setVisibility(View.VISIBLE);
         } else {
            holder.jokerIv.setVisibility(View.GONE);
         }
      }
      holder.autoPlayIv.setVisibility(View.VISIBLE);
      holder.cardImage.setImageResource(imgId);
   }

   public PickCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new PickCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rummy_card_view, parent, false));
   }

   public void setJokerCard(RummyPlayingCard jokerCard) {
      this.mJokerCard = jokerCard;
   }

   public class PickCardViewHolder extends RecyclerView.ViewHolder {
      public ImageView autoPlayIv;
      public ImageView cardImage;
      public ImageView jokerIv;

      public PickCardViewHolder(View view) {
         super(view);
         this.cardImage = (ImageView) view.findViewById(R.id.cardImageView);
         this.autoPlayIv = (ImageView) view.findViewById(R.id.autoPlayIv);
         this.jokerIv = (ImageView) view.findViewById(R.id.jokerCardImg);
      }
   }
}
