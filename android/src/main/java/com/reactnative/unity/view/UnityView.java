package com.reactnative.unity.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.unity3d.player.UnityPlayer;

/**
 * Created by xzper on 2018-02-07.
 */

public class UnityView extends FrameLayout implements UnityEventListener {

    private UnityPlayer view;
    private Context context;

    protected UnityView(ThemedReactContext context, UnityPlayer view) {
        super(context);
        this.view = view;
        this.context = context.getCurrentActivity();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        UnityUtils.addUnityViewToGroup(this, context);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (view != null) {
            view.windowFocusChanged(hasWindowFocus);
        }

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (view != null) {
            view.configurationChanged(newConfig);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        UnityUtils.addUnityViewToBackground();
        super.onDetachedFromWindow();
    }

    @Override
    public void onMessage(String message) {
        dispatchEvent(this, new UnityMessageEvent(this.getId(), message));
    }

    protected static void dispatchEvent(UnityView view, Event event) {
        ReactContext reactContext = (ReactContext) view.getContext();
        EventDispatcher eventDispatcher =
                reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        eventDispatcher.dispatchEvent(event);
    }
}
