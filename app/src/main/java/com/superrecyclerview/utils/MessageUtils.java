package com.superrecyclerview.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MessageUtils {

    private MessageUtils() {
        throw new RuntimeException("MessageUtils cannot be initialized!");
    }

    private static Toast sToast;

    public static void showInfo(Context context, String text) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.show();
    }

    public static void showTopError(Context context, String text) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.setGravity(Gravity.TOP, 0, CommonUtils.dp2px(context, 50));
        sToast.show();
    }

//    public static void showMessage(Context context, String title, String content,
//                                   final MessageDialog.OnChooseResultListener listener) {
//        MessageDialog dialog = new MessageDialog(context);
//        dialog.setTitle(title);
//        dialog.setContent(content);
//        dialog.hideCancel();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnChooseResultListener(listener);
//        dialog.show();
//    }
//
//    public static void showQuestion(Context context, String title, String content,
//                                    final MessageDialog.OnChooseResultListener listener) {
//        MessageDialog dialog = new MessageDialog(context);
//        dialog.setTitle(title);
//        dialog.setContent(content);
//        dialog.setOnChooseResultListener(listener);
//        dialog.show();
//    }
//
//    private static ProgressDialog dialog;
//    private static boolean isLoding;
//
//    public static void showLoading(Context context, String content) {
//        isLoding = true;
//        dialog = new ProgressDialog(context);
//        dialog.setTvLoading(content);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//    }
//
//    public static void stopLoading() {
//        if (dialog != null && isLoding)
//            dialog.dismiss();
//        isLoding = false;
//    }
//
//    public static void showSuccess(Activity activity, String text) {
//        ViewGroup viewGroup = (ViewGroup) activity
//                .findViewById(android.R.id.content).getRootView();
//        TSnackbar.make(viewGroup, text, TSnackbar.LENGTH_LONG)
//                .setPromptThemBackground(Prompt.SUCCESS).show();
//    }
//
//    public static void showWarning(Activity activity, String text) {
//        ViewGroup viewGroup = (ViewGroup) activity
//                .findViewById(android.R.id.content).getRootView();
//        TSnackbar.make(viewGroup, text, TSnackbar.LENGTH_LONG)
//                .setPromptThemBackground(Prompt.WARNING).show();
//    }
//
//    private static TSnackbar sTSnackbar;
//
//    public static TSnackbar hideProgress() {
//        if (sTSnackbar.isShownOrQueued()) {
//            sTSnackbar.dismiss();
//        }
//        return sTSnackbar;
//    }
//
//    public static TSnackbar showProgress(Activity activity, String text) {
//        ViewGroup viewGroup = (ViewGroup) activity
//                .findViewById(android.R.id.content).getRootView();
//        sTSnackbar = TSnackbar.make(viewGroup, text, TSnackbar.LENGTH_INDEFINITE)
//                .setPromptThemBackground(Prompt.WARNING)
//                .addIconProgressLoading(0, true, false);
//        sTSnackbar.show();
//        return sTSnackbar;
//    }
}
