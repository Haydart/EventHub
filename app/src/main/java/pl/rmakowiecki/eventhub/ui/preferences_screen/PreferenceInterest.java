package pl.rmakowiecki.eventhub.ui.preferences_screen;

public class PreferenceInterest {

    private String title;
    private boolean interested;

    public PreferenceInterest(String interestTitle, boolean interest) {
        title = interestTitle;
        interested = interest;
    }

    public String getTitle() {
        return title;
    }

    public boolean isInterested() {
        return interested;
    }
}
