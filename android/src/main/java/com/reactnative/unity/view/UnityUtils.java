package com.reactnative.unity.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unity3d.player.UnityPlayer;

import java.util.concurrent.CopyOnWriteArraySet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by xzper on 2018-03-08.
 */

public class UnityUtils {
    private static UnityPlayer unityPlayer;

    private static final CopyOnWriteArraySet<UnityEventListener> mUnityEventListeners =
            new CopyOnWriteArraySet<>();

    public static UnityPlayer getPlayer() {
        return unityPlayer;
    }

    public static boolean hasUnityPlayer() {
        return unityPlayer != null;
    }

    public static void postMessage(String gameObject, String methodName, String message) {
        UnityPlayer.UnitySendMessage(gameObject, methodName, message);
    }

    /**
     * Invoke by unity C#
     */
    public static void onUnityMessage(String message) {
        for (UnityEventListener listener : mUnityEventListeners) {
            try {
                listener.onMessage(message);
            } catch (Exception e) {
            }
        }
    }

    public static void addUnityEventListener(UnityEventListener listener) {
        mUnityEventListeners.add(listener);
    }

    public static void removeUnityEventListener(UnityEventListener listener) {
        mUnityEventListeners.remove(listener);
    }

    public static void addUnityViewToGroup(ViewGroup group, Context context) {
        final ViewGroup reactGroup = group;
        final Activity activity = ((Activity) context);

        if (UnityUtils.hasUnityPlayer()) {
            if (unityPlayer.getParent() != null) {
                ((ViewGroup) unityPlayer.getParent()).removeView(unityPlayer);
            }

            group.addView(unityPlayer, MATCH_PARENT, MATCH_PARENT);
            unityPlayer.windowFocusChanged(true);
            unityPlayer.requestFocus();
            unityPlayer.resume();

            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().setFormat(PixelFormat.RGBA_8888);

                unityPlayer = new UnityPlayer(activity);

                if (unityPlayer.getParent() != null) {
                    ((ViewGroup) unityPlayer.getParent()).removeView(unityPlayer);
                }
                reactGroup.addView(unityPlayer, MATCH_PARENT, MATCH_PARENT);

                try {
                    // wait a moment. fix unity cannot start when startup.
                    Thread.sleep(200);
                    unityPlayer.windowFocusChanged(true);
                    unityPlayer.requestFocus();
                    unityPlayer.resume();
                } catch (Exception e) {
                }

                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                // Clear low profile flags to apply non-fullscreen mode before splash screen
                showSystemUi();
                addUiVisibilityChangeListener();
            }
        });
    }

    public static void addUnityViewToBackground() {
        if (unityPlayer == null) {
            return;
        }
        if (unityPlayer.getParent() != null) {
            ((ViewGroup) unityPlayer.getParent()).removeView(unityPlayer);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unityPlayer.setZ(-1f);
        }

        final Activity activity = ((Activity) unityPlayer.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1, 1);
        activity.addContentView(unityPlayer, layoutParams);

        unityPlayer.pause();
    }

    private static int getLowProfileFlag() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                ?
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                :
                View.SYSTEM_UI_FLAG_LOW_PROFILE;
    }

    private static void showSystemUi() {
        if (unityPlayer == null) {
            return;
        }
        unityPlayer.setSystemUiVisibility(unityPlayer.getSystemUiVisibility() & ~getLowProfileFlag());
    }

    private static void addUiVisibilityChangeListener() {
        unityPlayer.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(final int visibility) {
                // Whatever changes - force status/nav bar to be visible
                showSystemUi();
            }
        });
    }
}

