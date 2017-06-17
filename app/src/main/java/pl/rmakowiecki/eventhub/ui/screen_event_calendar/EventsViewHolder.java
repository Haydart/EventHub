package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_event_details.EventDetailsActivity;

import static pl.rmakowiecki.eventhub.background.Constants.EVENT_DETAILS_PARCEL_KEY;

/**
 * Created by m1per on 20.04.2017.
 */

class EventsViewHolder extends RecyclerView.ViewHolder {

    final View view;
    @BindView(R.id.name_text_view) TextView nameTextView;
    @BindView(R.id.organizer_text_view) TextView organizerTextView;
    @BindView(R.id.date_text_view) TextView dateTextView;
    @BindView(R.id.address_text_view) TextView addressTextView;
    @BindView(R.id.day_of_month_text_view) TextView dayDateTextView;
    @BindView(R.id.day_short_name_text_view) TextView dayNameTextView;
    @BindView(R.id.distance_text_view) TextView distanceTextView;
    @BindView(R.id.attend_event_action_button) ActionButton attendButton;
    @BindString(R.string.day_today) String today;
    @BindString(R.string.day_tomorrow) String tomorrow;
    private EventsFragmentPresenter presenter;
    private EventWDistance eventWDistance;

    EventsViewHolder(View view, EventsFragmentPresenter presenter) {
        super(view);
        ButterKnife.bind(this, view);
        this.view = view;
        this.presenter = presenter;
    }

    EventsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    @OnClick(R.id.attend_event_action_button)
    protected void preferencesButtonClick() {
        presenter.addEventParticipant(eventWDistance.getEvent().getId(), getAdapterPosition());
    }

    public void showParticipationSavingStatus(GenericQueryStatus status) {
        if (status == GenericQueryStatus.STATUS_SUCCESS) {
            attendButton.showSuccess();
        } else {
            attendButton.showFailure("");
        }
    }

    void bindView(EventWDistance eventDistance) {
        eventWDistance = eventDistance;
        view.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
            intent.putExtra(EVENT_DETAILS_PARCEL_KEY, eventWDistance.getEvent());
            view.getContext().startActivity(intent);
        });

        String dateFull;
        String time;
        String dateDay;
        String dayOfTheWeekShort;
        String dayOfTheWeekName;
        String date;
        int daysToEvent;

        DateTime todayDate = new DateTime();
        DateTime dateOfEvent = new DateTime(eventWDistance.getEvent().getTimestamp());
        daysToEvent = Days.daysBetween(todayDate.toLocalDate(), dateOfEvent.toLocalDate()).getDays();

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
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
        nameTextView.setText(eventWDistance.getEvent().getName());
        organizerTextView.setText(eventWDistance.getEvent().getOrganizer());
        dateTextView.setText(date + ", " + time);
        dayDateTextView.setText(dateDay);
        dayNameTextView.setText(dayOfTheWeekShort);
        addressTextView.setText(eventWDistance.getEvent().getAddress());
        distanceTextView.setText(eventWDistance.getDistanceDisplayable());
    }
}
