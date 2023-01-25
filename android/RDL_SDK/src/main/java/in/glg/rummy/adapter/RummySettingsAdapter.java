package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.glg.rummy.R;

public class RummySettingsAdapter extends BaseAdapter {
   private int[] images;
   private String[] items;
   private Context mContext;
   private LayoutInflater mLayoutInflater;
   private String mTimerValue;

   public RummySettingsAdapter(Context context, int[] images, String[] items) {
      this.mContext = context;
      this.items = items;
      this.images = images;
      this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
   }


   public int getCount() {
      return this.items.length;
   }

   public Object getItem(int position) {
      return this.items[position];
   }

   public long getItemId(int position) {
      return 0;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View vrootView = convertView;
      vrootView = this.mLayoutInflater.inflate(R.layout.settings_list_item, null);
      ((TextView) vrootView.findViewById(R.id.settings_list_item_title)).setText(this.items[position]);
      ((ImageView) vrootView.findViewById(R.id.settings_list_item_iv)).setImageResource(this.images[position]);
      return vrootView;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }

   public void setTimerValue(String timerValue) {
      this.mTimerValue = timerValue;
   }
}
