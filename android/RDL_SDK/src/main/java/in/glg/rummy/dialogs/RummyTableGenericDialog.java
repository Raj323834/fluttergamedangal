package in.glg.rummy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import in.glg.rummy.R;
import in.glg.rummy.databinding.RummyDialogGenericGameRoomBinding;

public class RummyTableGenericDialog extends Dialog {
    RummyDialogGenericGameRoomBinding binding;
    private String message;
    private String title;
    private View.OnClickListener yesClickListener;
    private View.OnClickListener noClickListener;
    
    
    public RummyTableGenericDialog(@NonNull Context context,
                                   String message) {
        super(context, R.style.DialogTheme);
        this.message = message;
        this.title = title;
        binding = RummyDialogGenericGameRoomBinding.inflate(LayoutInflater.from(context));
        
    }
    public RummyTableGenericDialog(@NonNull Context context,
                                   String message, String title) {
        super(context, R.style.DialogTheme);
        this.message = message;
        this.title = title;
        binding = RummyDialogGenericGameRoomBinding.inflate(LayoutInflater.from(context));
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        binding.popUpCloseBtn.setOnClickListener(v -> dismiss());
        if(getWindow()!=null){

            getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        if (message != null) {
            binding.dialogMsgTv.setVisibility(View.VISIBLE);
            binding.dialogMsgTv.setText(message);
        } else {
            binding.dialogMsgTv.setVisibility(View.GONE);
        }
        if (title != null) {
            binding.headerTv.setVisibility(View.VISIBLE);
            binding.headerTv.setText(title);
        } else {
            binding.headerTv.setVisibility(View.GONE);
        }
        
        if(yesClickListener==null)
        binding.okBtn.setOnClickListener(v -> dismiss());
    }
    
    
    public void setYesClickListener(View.OnClickListener yesClickListener) {
        this.yesClickListener = yesClickListener;
        binding.okBtn.setOnClickListener(v -> {
            dismiss();
            yesClickListener.onClick(v);
        });
    }
    
    public void setCloseClickListener(View.OnClickListener closeClickListener) {
        this.noClickListener = closeClickListener;
        binding.popUpCloseBtn.setOnClickListener(v -> {
            dismiss();
            closeClickListener.onClick(v);
        });
    }
    
    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
            binding.headerTv.setText(title);
            binding.headerTv.setVisibility(View.VISIBLE);
        } else {
            binding.headerTv.setVisibility(View.GONE);
        }
    }
    
    
}
