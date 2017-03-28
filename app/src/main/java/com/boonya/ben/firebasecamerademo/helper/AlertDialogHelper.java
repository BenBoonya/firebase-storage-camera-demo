package com.boonya.ben.firebasecamerademo.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

import com.boonya.ben.firebasecamerademo.R;

/**
 * AlertDialogHelper
 * on 4/19/16
 *
 * @author Jutikorn Varojananulux <jutikorn.v@gmail.com>
 */
public class AlertDialogHelper {


    private AlertDialogHelper() {
        throw new IllegalAccessError("Utility class");
    }

    private static AlertDialog.Builder createDialogBuilder(Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    public static AlertDialog createAlertDialog(Context context,
                                                String title,
                                                String message,
                                                int neutralmsgBtnId,
                                                ArrayAdapter<String> adapter,
                                                DialogInterface.OnClickListener listener,
                                                final DialogInterface.OnCancelListener cancelListener) {
        AlertDialog.Builder builder = createDialogBuilder(context, title);
        if (adapter != null) {
            builder.setAdapter(adapter, listener);
        } else {
            builder.setMessage(message);
            builder.setNeutralButton(neutralmsgBtnId, listener);
        }
        if (cancelListener != null) {
            builder.setOnCancelListener(cancelListener);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        cancelListener.onCancel(dialog);
                    }
                    return false;
                }
            });
        }
        return builder.create();
    }

    public static AlertDialog createAlertDialog(Context context,
                                                int titleId,
                                                String message,
                                                int neutralmsgBtnId,
                                                DialogInterface.OnClickListener listener,
                                                final DialogInterface.OnCancelListener cancelListener) {

        return createAlertDialog(context,
                context.getString(titleId),
                message,
                neutralmsgBtnId,
                null,
                listener,
                cancelListener);
    }

    public static AlertDialog createAlertDialog(Context context,
                                                int titleId,
                                                String message,
                                                final DialogInterface.OnClickListener listener) {
        return createAlertDialog(context, context.getString(titleId), message, listener);
    }

    public static AlertDialog createAlertDialog(Context context,
                                                String title,
                                                String message,
                                                final DialogInterface.OnClickListener listener) {
        return createAlertDialog(context,
                title,
                message,
                R.string.label_ok,
                null,
                listener,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }


    public static AlertDialog createAlertDialog(Context context,
                                                int titleId,
                                                String message,
                                                int btnMsgId,
                                                final DialogInterface.OnClickListener listener) {
        return createAlertDialog(context,
                titleId,
                message,
                btnMsgId,
                listener,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    public static AlertDialog createErrorDialog(Context context, String message) {
        return createAlertDialog(context, R.string.label_error, message, R.string.label_ok, null);
    }

    public static AlertDialog createOkAlertDialog(Context context,
                                                  int titleId,
                                                  int messageId,
                                                  DialogInterface.OnClickListener listener) {
        return createAlertDialog(context, titleId, context.getString(messageId), listener);
    }

    public static AlertDialog createOkAlertDialog(Context context,
                                                  int messageId,
                                                  DialogInterface.OnClickListener listener) {
        return createOkAlertDialog(context, context.getString(messageId), listener);
    }

    public static AlertDialog createOkAlertDialog(Context context,
                                                  String message,
                                                  DialogInterface.OnClickListener listener) {
        return createAlertDialog(context, 0, message, listener);
    }


    public static AlertDialog createListAlertDialog(Context context,
                                                    int titleId,
                                                    ArrayAdapter<String> adapter,
                                                    DialogInterface.OnClickListener listener) {

        return createAlertDialog(context,
                context.getString(titleId),
                null,
                -1,
                adapter,
                listener,
                null);
    }


    public static AlertDialog createYesNoAlertDialog(Context context,
                                                     String message,
                                                     DialogInterface.OnClickListener listener,
                                                     DialogInterface.OnClickListener noListener,
                                                     String positiveButtonText,
                                                     String negativeButtonText) {
        AlertDialog.Builder builder = createDialogBuilder(context, null);
        builder.setMessage(message)
                .setPositiveButton(positiveButtonText, listener)
                .setNegativeButton(negativeButtonText, noListener);
        return builder.create();
    }

    public static AlertDialog createYesNoAlertDialog(Context context,
                                                     String message,
                                                     DialogInterface.OnClickListener listener,
                                                     DialogInterface.OnClickListener noListener,
                                                     int positiveButtonId,
                                                     int negativeButtonId) {
        return createYesNoAlertDialog(context, message, listener, noListener, context.getString(positiveButtonId), context.getString(negativeButtonId));
    }

    public static AlertDialog createYesNoAlertDialog(Context context,
                                                     String message,
                                                     DialogInterface.OnClickListener listener) {
        return createYesNoAlertDialog(context, message, listener, null, null);
    }

    public static AlertDialog createYesNoAlertDialog(Context context,
                                                     String message,
                                                     DialogInterface.OnClickListener listener,
                                                     DialogInterface.OnClickListener noListener,
                                                     DialogInterface.OnCancelListener cancelListener) {
        AlertDialog.Builder builder = createDialogBuilder(context, null);
        builder.setMessage(message)
                .setPositiveButton("yes", listener)
                .setNegativeButton("no", noListener)
                .setOnCancelListener(cancelListener);
        return builder.create();
    }

    public static AlertDialog createListItemDialog(Context context, String title, int arrayId,
                                                   final DialogInterface.OnClickListener listener) {
        return createListItemDialog(context, title, context.getResources().getStringArray(arrayId), listener);
    }

    public static AlertDialog createListItemDialog(Context context, String title, CharSequence[] items,
                                                   final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = createDialogBuilder(context, title);
        builder.setSingleChoiceItems(items, 0, listener);
        return builder.create();
    }
}
