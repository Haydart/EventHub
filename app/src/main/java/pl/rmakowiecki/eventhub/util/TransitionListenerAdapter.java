package pl.rmakowiecki.eventhub.util;

import android.support.annotation.NonNull;
import android.transition.Transition;

public class TransitionListenerAdapter implements Transition.TransitionListener {
    @Override
    public void onTransitionStart(@NonNull Transition transition) {
        //no-op
    }

    @Override
    public void onTransitionEnd(@NonNull Transition transition) {
        //no-op
    }

    @Override
    public void onTransitionCancel(@NonNull Transition transition) {
        //no-op
    }

    @Override
    public void onTransitionPause(@NonNull Transition transition) {
        //no-op
    }

    @Override
    public void onTransitionResume(@NonNull Transition transition) {
        //no-op
    }
}
