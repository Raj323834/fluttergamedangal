package in.glg.rummy.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import in.glg.rummy.fancycoverflow.RummyFancyCoverFlowAdapter;

public class RummyCircularAdapter extends RummyFancyCoverFlowAdapter {
   static final boolean DEBUG = false;
   static final String TAG = RummyCircularAdapter.class.getSimpleName();
   private BaseAdapter mListAdapter;
   private int mListAdapterCount;

   public RummyCircularAdapter(BaseAdapter listAdapter) {
      if (listAdapter == null) {
         throw new IllegalArgumentException("listAdapter cannot be null.");
      }
      this.mListAdapter = listAdapter;
      this.mListAdapterCount = listAdapter.getCount();
   }

   public int getCount() {
      return Integer.MAX_VALUE;
   }

   public Object getItem(int position) {
      return this.mListAdapter.getItem(position % this.mListAdapterCount);
   }

   public long getItemId(int position) {
      return this.mListAdapter.getItemId(position % this.mListAdapterCount);
   }

   public boolean areAllItemsEnabled() {
      return this.mListAdapter.areAllItemsEnabled();
   }

   public int getItemViewType(int position) {
      return this.mListAdapter.getItemViewType(position % this.mListAdapterCount);
   }

   public int getViewTypeCount() {
      return this.mListAdapter.getViewTypeCount();
   }

   public boolean isEmpty() {
      return this.mListAdapter.isEmpty();
   }

   public boolean isEnabled(int position) {
      return this.mListAdapter.isEnabled(position % this.mListAdapterCount);
   }

   public void notifyDataSetChanged() {
      this.mListAdapter.notifyDataSetChanged();
      this.mListAdapterCount = this.mListAdapter.getCount();
      super.notifyDataSetChanged();
   }

   public void notifyDataSetInvalidated() {
      this.mListAdapter.notifyDataSetInvalidated();
      super.notifyDataSetInvalidated();
   }

   public View getDropDownView(int position, View convertView, ViewGroup parent) {
      return this.mListAdapter.getDropDownView(position % this.mListAdapterCount, convertView, parent);
   }

   public boolean hasStableIds() {
      return this.mListAdapter.hasStableIds();
   }

   public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
      return this.mListAdapter.getView(i % this.mListAdapterCount, reuseableView, viewGroup);
   }
}
