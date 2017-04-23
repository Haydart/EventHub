package pl.rmakowiecki.eventhub.ui.preferences_screen;

public final class PreferenceInterest {

    private final String title;
    private final boolean interested;

    public PreferenceInterest(String interestTitle, boolean interest) {
        title = interestTitle;
        interested = interest;
    }
}
