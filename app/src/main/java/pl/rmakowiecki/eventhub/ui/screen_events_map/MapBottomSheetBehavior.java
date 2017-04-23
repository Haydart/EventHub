package pl.rmakowiecki.eventhub.ui.screen_events_map;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

class MapBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    @SuppressWarnings("unchecked")
    public static <V extends View> MapBottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not associated with MapBottomSheetBehavior 1");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof MapBottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with MapBottomSheetBehavior 2");
        }
        return (MapBottomSheetBehavior<V>) behavior;
    }
}
