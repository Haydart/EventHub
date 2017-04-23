package pl.rmakowiecki.eventhub.ui.screen_preference_subcategories;

public final class PreferenceInterest {

    private final String title;
    private final boolean interested;

    public PreferenceInterest(String interestTitle, boolean interest) {
        title = interestTitle;
        interested = interest;
    }
}
