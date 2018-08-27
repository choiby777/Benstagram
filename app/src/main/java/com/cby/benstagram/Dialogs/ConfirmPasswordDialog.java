package com.cby.benstagram.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.R;

public class ConfirmPasswordDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "ConfirmPasswordDialog";

    private EditText editText_password;
    private TextView txt_confirm , txt_cancel;

    public interface OnConfirmPasswordListener{
        public void onConfirmPasswordListener(String password);
    }

    OnConfirmPasswordListener mOnConfirmPasswordListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password , container , false);

        Log.d(TAG, "onCreateView: ConfirmPasswordDialog created");

        editText_password = view.findViewById(R.id.editText_password);
        txt_confirm = view.findViewById(R.id.txt_confirm);
        txt_cancel = view.findViewById(R.id.txt_cancel);

        txt_confirm.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.txt_confirm){

            Log.d(TAG, "onClick: user clicked confirm");

            String password = editText_password.getText().toString();

            if (!password.equals("")){
                mOnConfirmPasswordListener.onConfirmPasswordListener(password);
                getDialog().dismiss();
            }else{
                Toast.makeText(getActivity(), "Input password", Toast.LENGTH_SHORT).show();
            }


        }else if (v.getId() == R.id.txt_cancel){

            Log.d(TAG, "onClick: user clicked cancel");
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mOnConfirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }
}
