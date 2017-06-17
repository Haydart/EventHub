package pl.rmakowiecki.eventhub.ui.screen_events_map;

import pl.rmakowiecki.eventhub.R;

enum RevealColor {
    WHITE {
        @Override
        int getColor() {
            return android.R.color.white;
        }
    },
    PRIMARY_COLOR {
        @Override
        int getColor() {
            return R.color.colorPrimary;
        }
    };

    abstract int getColor();
}
