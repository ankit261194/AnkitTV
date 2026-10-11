package com.liskovsoft.smartyoutubetv2.tv.ui.main;

import android.content.Intent;
import android.os.Bundle;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.SplashPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.views.SplashView;
import com.liskovsoft.smartyoutubetv2.common.misc.MotherActivity;

public class SplashActivity extends MotherActivity implements SplashView {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static boolean sChimePlayed;
    private Intent mNewIntent;
    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewIntent = getIntent();

        playStartupChime();

        mPresenter = SplashPresenter.instance(this);
        mPresenter.setView(this);
        mPresenter.onViewInitialized();

        //finish();
    }

    private void playStartupChime() {
        if (sChimePlayed) {
            return;
        }
        sChimePlayed = true;
        try {
            android.media.MediaPlayer player = android.media.MediaPlayer.create(this, com.liskovsoft.smartyoutubetv2.tv.R.raw.ankit_tv_chime);
            if (player != null) {
                player.setOnCompletionListener(mp -> {
                    try { mp.release(); } catch (Throwable ignored) {}
                });
                player.setOnErrorListener((mp, what, extra) -> {
                    try { mp.release(); } catch (Throwable ignored) {}
                    return true;
                });
                player.start();
            }
        } catch (Throwable ignored) {
            // Safe fallback, never crash
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mNewIntent = intent;

        mPresenter.onViewInitialized();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDestroyed();
    }

    @Override
    public Intent getNewIntent() {
        return mNewIntent;
    }

    @Override
    public void finishView() {
        try {
            finish();
        } catch (NullPointerException e) {
            // NullPointerException: Attempt to invoke virtual method 'void com.android.server.wm.DisplayContent.moveStack(com.android.server.wm.TaskStack, boolean)'
            e.printStackTrace();
        }
    }
}
