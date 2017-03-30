package pl.rmakowiecki.eventhub.ui.events_map_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import pl.rmakowiecki.eventhub.R;

class MapBottomSheet extends FrameLayout {

    private Context context;
    private LayoutInflater layoutInflater;

    public MapBottomSheet(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.map_bottom_sheet, this, true);
    }
}
