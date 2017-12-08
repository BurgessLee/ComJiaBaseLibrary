package com.comjia.baselibrary.utils.upgrade;

import android.content.DialogInterface;

public class DefaultPromptClickListener implements DialogInterface.OnClickListener {

    private final IUpdateAgent mAgent;
    private final boolean mIsAutoDismiss;

    public DefaultPromptClickListener(IUpdateAgent agent, boolean isAutoDismiss) {
        mAgent = agent;
        mIsAutoDismiss = isAutoDismiss;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mAgent.update();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mAgent.ignore();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // not now
                break;
        }
        if (mIsAutoDismiss) {
            dialog.dismiss();
        }
    }
}