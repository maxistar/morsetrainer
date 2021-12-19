package com.maxistar.morsetrainer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AboutBox extends DialogPreference
{
    // This is the constructor called by the inflater
    public AboutBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        final SpannableString s =
                new SpannableString(getContext().getResources().getString(R.string.about_text));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        final TextView view = new TextView(getContext());
        view.setText(s);
        view.setPadding(32, 32, 32, 32);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        // Data has changed, notify so UI can be refreshed!
        builder.setTitle(R.string.about);
        builder.setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        builder.setNegativeButton(null, null);
    }

}
