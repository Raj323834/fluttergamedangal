package in.glg.rummy.actionmenu;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class RummyActionItem {
   private int actionId;
   private Drawable icon;
   private String mTag;
   private boolean selected;
   private boolean sticky;
   private Bitmap thumb;
   private String title;

   public RummyActionItem(int actionId, String title, Drawable icon) {
      this.actionId = -1;
      this.title = title;
      this.icon = icon;
      this.actionId = actionId;
   }

   public RummyActionItem() {
      this(-1, null, null);
   }

   public RummyActionItem(int actionId, String title) {
      this(actionId, title, null);
   }

   public RummyActionItem(Drawable icon) {
      this(-1, null, icon);
   }

   public RummyActionItem(int actionId, Drawable icon) {
      this(actionId, null, icon);
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getTitle() {
      return this.title;
   }

   public void setIcon(Drawable icon) {
      this.icon = icon;
   }

   public Drawable getIcon() {
      return this.icon;
   }

   public void setActionId(int actionId) {
      this.actionId = actionId;
   }

   public int getActionId() {
      return this.actionId;
   }

   public void setSticky(boolean sticky) {
      this.sticky = sticky;
   }

   public boolean isSticky() {
      return this.sticky;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setThumb(Bitmap thumb) {
      this.thumb = thumb;
   }

   public Bitmap getThumb() {
      return this.thumb;
   }

   public void setTag(String tag) {
      this.mTag = tag;
   }

   public String getTag() {
      return this.mTag;
   }
}
