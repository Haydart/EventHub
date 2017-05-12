package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.distance_text_view) TextView distanceTextView;
    @BindString(R.string.day_today) String today;
    @BindString(R.string.day_tomorrow) String tomorrow;

    EventsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    public void bindView(Event event) {
        RxLocationProvider provider = new RxLocationProvider();
        Observable<LocationCoordinates> locationObservable = provider.getLocation();
        final double[] latitude = new double[1];
        final double[] longitude = new double[1];

        //TODO: JUST A RARE SAMPLE FO DEVELOPMENT, NEEDS LOTS OF WORK

        String dateFull;
        String time;
        String dateDay;
        String dayOfTheWeekShort;
        String dayOfTheWeekName;
        String date;
        String distance;
        String coords[];
        int daysToEvent;

        coords = event.getCoordinates().split(",");

        provider.getLocation()
                .filter(location -> location != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coordinates -> {
                            showDistance(coordinates, coords);
                        });

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

    private void showDistance(LocationCoordinates userCoords, String[] eventCoords) {
        Location userLocation = new Location("");
        String distance;
        double distanceInMeters;
        double calculatedDistance;

        userLocation.setLatitude(userCoords.getLatitude());
        userLocation.setLongitude(userCoords.getLongitude());

        Location eventLocation = new Location("");
        eventLocation.setLatitude(Double.parseDouble(eventCoords[0]));
        eventLocation.setLongitude(Double.parseDouble(eventCoords[1]));

        distanceInMeters = (userLocation.distanceTo(eventLocation));
        if (distanceInMeters < 1000) {
            distance = Integer.toString((int) distanceInMeters);
            distanceTextView.setText(distance + " m");
        } else {
            calculatedDistance = BigDecimal.valueOf(distanceInMeters / 1000)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            distance = Double.toString(calculatedDistance);
            distanceTextView.setText(distance + " km");
        }
    }
}
