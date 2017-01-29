package com.poolapps.simplecrud.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.poolapps.simplecrud.R;


public class ConfirmDeletionDialog extends DialogFragment {

    public interface ConfirmDeletionDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmDeletionDialogListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (ConfirmDeletionDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                        + " must implement ConfirmDeletionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_deletion_title)
                .setMessage(R.string.dialog_deletion_message)
                .setPositiveButton(R.string.dialog_deletion_confirmed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mListener.onDialogPositiveClick(ConfirmDeletionDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_deletion_canceled, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogNegativeClick(ConfirmDeletionDialog.this);
                    }
                });
        return builder.create();
    }


}
