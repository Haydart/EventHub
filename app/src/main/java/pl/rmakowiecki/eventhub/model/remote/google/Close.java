
package pl.rmakowiecki.eventhub.model.remote.google;

public class Close {

    public final Integer day;
    public final String time;

    public Close(Integer day, String time) {
        this.day = day;
        this.time = time;
    }
}
