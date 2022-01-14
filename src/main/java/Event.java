import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Event {

    String eventName;
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime startTime = currentTime.plusHours(1);
    LocalDateTime endTime = startTime.plusMinutes(90);
    String pattern = "hh:mm a";
    String startHours;
    String startMin;
    String endHours;
    String endMin;
    String startDayTime;
    String endDayTime;

    public Event(String eventName) {
        this.eventName = eventName;
    }

    public String getStartHours() {
        startHours = startTime.format(DateTimeFormatter.ofPattern(pattern)).substring(0, 2);
        if (Integer.parseInt(startHours) < 10) {
            startHours = startHours.substring(1);
        }
        return startHours;
    }

    public String getStartMin() {
        startMin = startTime.format(DateTimeFormatter.ofPattern(pattern)).substring(3, 5);
        return startMin;
    }

    public String getEndHours() {
        endHours = endTime.format(DateTimeFormatter.ofPattern(pattern)).substring(0, 2);
        if (Integer.parseInt(endHours) < 10) {
            endHours = endHours.substring(1);
        }
        return endHours;
    }

    public String getEndMin() {
        endMin = endTime.format(DateTimeFormatter.ofPattern(pattern)).substring(3, 5);
        return endMin;
    }

    public String getStartDayTime() {
        startDayTime = startTime.format(DateTimeFormatter.ofPattern(pattern)).substring(6,8);
        return startDayTime;
    }

    public String getEndDayTime() {
        endDayTime = endTime.format(DateTimeFormatter.ofPattern(pattern)).substring(6,8);
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



}

