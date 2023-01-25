package in.glg.rummy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import in.glg.rummy.R;
import in.glg.rummy.databinding.RummyDialogLeaveTableNewBinding;

public class RummyTableConfirmationDialog extends Dialog {

    RummyDialogLeaveTableNewBinding binding;
    private String message;
    private String title;
    private View.OnClickListener yesClickListener;
    private View.OnClickListener noClickListener;

    public RummyTableConfirmationDialog(@NonNull Context context
            , String message, String title) {
        super(context, R.style.DialogTheme);
        this.message = message;
        this.title = title;
        binding = RummyDialogLeaveTableNewBinding.inflate(LayoutInflater.from(context));

    }

    public RummyTableConfirmationDialog(@NonNull Context context,
                                        String message) {
        super(context, R.style.DialogTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.message = message;
        this.title = title;
        binding = RummyDialogLeaveTableNewBinding.inflate(LayoutInflater.from(context));
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
       // binding.popUpCloseBtn.setOnClickListener(v -> dismiss());
        if (noClickListener == null)
            binding.noBtn.setOnClickListener(v -> dismiss());
        
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
        binding.yesBtn.setOnClickListener(v -> dismiss());
    }
    
    
    public void setYesClickListener(View.OnClickListener yesClickListener) {
        this.yesClickListener = yesClickListener;
        binding.yesBtn.setOnClickListener(v -> {
            dismiss();
            yesClickListener.onClick(v);
        });
    }
    
    public void setNoAndCloseClickListener(View.OnClickListener noClickListener) {
        this.noClickListener = noClickListener;
        binding.noBtn.setOnClickListener(v -> {
            dismiss();
            noClickListener.onClick(v);
        });
        
        binding.popUpCloseBtn.setOnClickListener(v -> {
            dismiss();
            noClickListener.onClick(v);
        });
    }
    
    public void setDropMoveCheckBox(boolean isChecked) {
        binding.cbDropMove.setChecked(isChecked);
    }
    
    public boolean isDropAndMoveChecked() {
        return binding.cbDropMove.isChecked();
    }
    
    public void moveConfirmationClickListener(View.OnClickListener onClickListener) {
        binding.llDropMoveConfirmation.setOnClickListener(onClickListener);
        if (onClickListener != null)
            binding.llDropMoveConfirmation.setVisibility(View.VISIBLE);
        else
            binding.llDropMoveConfirmation.setVisibility(View.GONE);
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
