
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {

    private String eventName;
    private LocalDateTime currentTime = LocalDateTime.now();
    private int addStartMin;
    private int addEndMin;
    private String pattern = "hh:mm a";
    private String startHours;
    private String startMin;
    private String endHours;
    private String endMin;
    private String startDayTime;
    private String endDayTime;
    private String location;

    public Event(String eventName, int addStartMin, int addEndMin, String city) {
        this.eventName = eventName;
        this.addStartMin = addStartMin;
        this.addEndMin = addEndMin;
        this.location = city;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLocation() {
        return location;
    }

    public String getStartHours() {
        startHours = currentTime.plusMinutes(addStartMin).format(DateTimeFormatter.ofPattern(pattern)).substring(0, 2);
        if (Integer.parseInt(startHours) < 10) {
            startHours = startHours.substring(1);
        }
        return startHours;
    }

    public String getStartMin() {
        startMin = currentTime.plusMinutes(addStartMin).format(DateTimeFormatter.ofPattern(pattern)).substring(3, 5);
        return startMin;
    }

    public String getEndHours() {
        endHours = currentTime.plusMinutes(addEndMin).format(DateTimeFormatter.ofPattern(pattern)).substring(0, 2);
        if (Integer.parseInt(endHours) < 10) {
            endHours = endHours.substring(1);
        }
        return endHours;
    }

    public String getEndMin() {
        endMin = currentTime.plusMinutes(addEndMin).format(DateTimeFormatter.ofPattern(pattern)).substring(3, 5);
        return endMin;
    }

    public String getStartDayTime() {
        startDayTime = currentTime.plusMinutes(addStartMin).format(DateTimeFormatter.ofPattern(pattern)).substring(6, 8);
        return startDayTime;
    }

    public String getEndDayTime() {
        endDayTime = currentTime.plusMinutes(addEndMin).format(DateTimeFormatter.ofPattern(pattern)).substring(6, 8);
        return endDayTime;
    }

    public String getAndroidEventDetails() {
        String expectedTime;
        if (getEndDayTime().equals(getStartDayTime())) {
            expectedTime = String.format("Today  •  %s:%s – %s:%s %s", getStartHours(), getStartMin(), getEndHours(), getEndMin(), getEndDayTime());
        } else {
            expectedTime = String.format("Today  •  %s:%s %s – %s:%s %s", getStartHours(), getStartMin(), getStartDayTime(), getEndHours(), getEndMin(), getEndDayTime());
        }
        return expectedTime;
    }

    public String getIosEventDetails() {
        String expectedDetails = String.format("%s, %s, from %s:%s %s to %s:%s %s", getEventName(), getLocation(), getStartHours(), getStartMin(), getStartDayTime(), getEndHours(), getEndMin(), getEndDayTime());
        return expectedDetails;
    }

    public String getAndroidTimeFromPush() {
        String expectedTime;
        if (getEndDayTime().equals(getStartDayTime())) {
            expectedTime = String.format("%s:%s – %s:%s %s", getStartHours(), getStartMin(), getEndHours(), getEndMin(), getEndDayTime());
        } else {
            expectedTime = String.format("%s:%s %s – %s:%s %s", getStartHours(), getStartMin(), getStartDayTime(), getEndHours(), getEndMin(), getEndDayTime());
        }
        return expectedTime;
    }

    public String getIosDetailsFromPush() {
        String expectedDetails = String.format("CALENDAR, now, %s, Today at %s:%s %s", eventName, startHours, startMin, startDayTime);
        return expectedDetails;
    }
}

