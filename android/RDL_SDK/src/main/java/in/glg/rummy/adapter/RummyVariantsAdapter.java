package in.glg.rummy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.glg.rummy.R;

public class RummyVariantsAdapter extends BaseAdapter {
   private String[] items;
   private Context mContext;
   private LayoutInflater mLayoutInflater;
   private String mTimerValue;
   private HashMap<Integer, Boolean> myChecked = new HashMap();
   private List<String> prevCheckedItems;

   public RummyVariantsAdapter(Context context, String[] items) {
      this.mContext = context;
      this.items = items;
      this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.myChecked = new HashMap();
      for (int i = 0; i < items.length; i++) {
         this.myChecked.put(Integer.valueOf(i), Boolean.valueOf(false));
      }
   }

   private void setUpItemUI(TextView title, ImageView checkMark, Boolean checked) {
      if (checked.booleanValue()) {
         title.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_white));
      } else {
         title.setTextColor(ContextCompat.getColor(this.mContext, R.color.rummy_White));
      }
      if (checked.booleanValue()) {
         checkMark.setVisibility(View.VISIBLE);
      } else {
         checkMark.setVisibility(View.INVISIBLE);
      }
   }

   public List<String> getCheckedItems() {
      List<String> checkedItems = new ArrayList();
      for (int i = 0; i < this.myChecked.size(); i++) {
         if (((Boolean) this.myChecked.get(Integer.valueOf(i))).booleanValue()) {
            checkedItems.add(this.items[i]);
         }
      }
      return checkedItems;
   }

   public int getCount() {
      return this.items.length;
   }

   public Object getItem(int position) {
      return this.items[position];
   }

   public long getItemId(int position) {
      return 0L;
   }

   public List<String> getUnCheckedItems() {
      List<String> unCheckedItems = new ArrayList();
      for (int i = 0; i < this.myChecked.size(); i++) {
         if (!((Boolean) this.myChecked.get(Integer.valueOf(i))).booleanValue()) {
            unCheckedItems.add(this.items[i]);
         }
      }
      return unCheckedItems;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View vrootView = convertView;
      vrootView = this.mLayoutInflater.inflate(R.layout.rummy_drop_dowm_item, null);
      vrootView.setBackgroundColor(mContext.getResources().getColor(R.color.rummy_colorPrimary));
      TextView title = (TextView) vrootView.findViewById(R.id.title_tv);
      title.setText(this.items[position]);
      ImageView checkMark = (ImageView) vrootView.findViewById(R.id.checkbox_iv);
      Boolean checked = (Boolean) this.myChecked.get(Integer.valueOf(position));
      if (checked != null) {
         setUpItemUI(title, checkMark, checked);
      }
      return vrootView;
   }

   public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
   }

   public void setPrevCheckedItems(List<String> prevCheckedItems) {
      this.prevCheckedItems = prevCheckedItems;
   }

   public void setTimerValue(String timerValue) {
      this.mTimerValue = timerValue;
   }

   public void setUnChecked(int position) {
      this.myChecked.put(Integer.valueOf(position), Boolean.valueOf(false));
      notifyDataSetChanged();
   }

   public void toggleChecked(int position) {
      if (((Boolean) this.myChecked.get(Integer.valueOf(position))).booleanValue()) {
         this.myChecked.put(Integer.valueOf(position), Boolean.valueOf(false));
      } else {
         this.myChecked.put(Integer.valueOf(position), Boolean.valueOf(true));
      }
      notifyDataSetChanged();
   }

   public void toggleChipsItem(int position) {
      for (int i = 0; i < this.items.length; i++) {
         this.myChecked.put(Integer.valueOf(i), Boolean.valueOf(false));
      }
      this.myChecked.put(Integer.valueOf(position), Boolean.valueOf(true));
      notifyDataSetChanged();
   }
}
