package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;

/**
 * Created by m1per on 20.04.2017.
 */

class EventsViewHolder extends RecyclerView.ViewHolder {
    final View view;

    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.organizerTextView) TextView organizerTextView;
    @BindView(R.id.dateTextView) TextView dateTextView;
    @BindView(R.id.addressTextView) TextView locationTextView;
    @BindView(R.id.dayOfMonthTextView) TextView dayDateTextView;
    @BindView(R.id.dayShortNameTextView) TextView dayNameTextView;

    EventsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    public void bindView(Event event) {
        //TODO: JUST A RARE SAMPLE FO DEVELOPMENT, NEEDS LOTS OF WORK
        Date d = new Date(event.getTimestamp() * 1000);
        String days[] = { "PON.", "WT.", "ŚR.", "CZW.", "PT.", "SOB.", "ND." };

        String date = new SimpleDateFormat("dd-MM-yyyy").format(d);
        String day = new SimpleDateFormat("dd").format(d);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayName = days[dayOfWeek - 1];

        nameTextView.setText(event.getName());
        organizerTextView.setText(event.getOrganizer());
        dateTextView.setText(date);
        dayDateTextView.setText(day);
        dayNameTextView.setText(dayName);
        locationTextView.setText(event.getLocation());
    }
}
