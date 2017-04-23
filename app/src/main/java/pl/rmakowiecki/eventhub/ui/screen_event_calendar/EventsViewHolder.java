package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;

/**
 * Created by m1per on 20.04.2017.
 */

class EventsViewHolder extends RecyclerView.ViewHolder {
    final View view;

    @BindView(R.id.name_text_view) TextView nameTextView;
    @BindView(R.id.organizer_text_view) TextView organizerTextView;
    @BindView(R.id.date_text_view) TextView dateTextView;
    @BindView(R.id.address_text_view) TextView locationTextView;
    @BindView(R.id.day_of_month_text_view) TextView dayDateTextView;
    @BindView(R.id.day_short_name_text_view) TextView dayNameTextView;
    @BindString(R.string.day_today) String today;
    @BindString(R.string.day_tomorrow) String tomorrow;

    EventsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    public void bindView(Event event) {
        //TODO: JUST A RARE SAMPLE FO DEVELOPMENT, NEEDS LOTS OF WORK
        String dateFull;
        String time;
        String dateDay;
        String dayOfTheWeekShort;
        String dayOfTheWeekName;
        String date;
        int daysToEvent;

        DateTime todayDate = new DateTime();
        DateTime dateOfEvent = new DateTime(TimeUnit.SECONDS.toMillis(event.getTimestamp()));
        daysToEvent = Days.daysBetween(todayDate.toLocalDate(), dateOfEvent.toLocalDate()).getDays();

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
        dateFull = dateOfEvent.toString(dtf);
        dtf = DateTimeFormat.forPattern("HH:mm");
        time = dateOfEvent.toString(dtf);
        dtf = DateTimeFormat.forPattern("dd");
        dateDay = dateOfEvent.toString(dtf);
        dtf = DateTimeFormat.forPattern("EEE");
        dayOfTheWeekShort = dateOfEvent.toString(dtf);
        dayOfTheWeekShort = dayOfTheWeekShort.toUpperCase();
        dtf = DateTimeFormat.forPattern("EEEE");
        dayOfTheWeekName = dateOfEvent.toString(dtf);

        if (daysToEvent == 0) {
            date = today;
        } else if (daysToEvent == 1) {
            date = tomorrow;
        } else if (daysToEvent < 7) {
            date = dayOfTheWeekName;
        } else {
            date = dateFull;
        }
        nameTextView.setText(event.getName());
        organizerTextView.setText(event.getOrganizer());
        dateTextView.setText(date + ", " + time);
        dayDateTextView.setText(dateDay);
        dayNameTextView.setText(dayOfTheWeekShort);
        locationTextView.setText(event.getLocation());
    }
}
