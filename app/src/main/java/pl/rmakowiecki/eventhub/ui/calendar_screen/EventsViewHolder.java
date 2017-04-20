package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

/**
 * Created by m1per on 20.04.2017.
 */

class EventsViewHolder extends RecyclerView.ViewHolder {
    final View mView;
    Event mItem;

    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.organizer) TextView organizerTextView;
    @BindView(R.id.date) TextView dateTextView;
    @BindView(R.id.address) TextView locationTextView;
    @BindView(R.id.daydate) TextView dayDateTextView;
    @BindView(R.id.day) TextView dayNameTextView;

    EventsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        mView = view;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
