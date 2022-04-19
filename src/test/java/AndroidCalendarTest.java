import appiumdriver.AppiumDriverSingleton;
import io.qameta.allure.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Result;
import screens.android.CalendarAndroidScreen;
import screens.android.EventDetailsScreen;
import screens.android.NewEventScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.*;
import utils.MyTestWatcher;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MyTestWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AndroidCalendarTest extends Result {
    AndroidDriver driver;
    CalendarAndroidScreen calendarAndroidScreen;
    NewEventScreen newEventScreen;
    EventDetailsScreen eventDetailsScreen;
    Event testEvent = new Event("Test Event", 2, 8, "Paris");


    @BeforeEach
    public void setUp() throws Exception {
        driver = AppiumDriverSingleton.getInstance();
        calendarAndroidScreen = new CalendarAndroidScreen(driver);
        newEventScreen = new NewEventScreen(driver);
        eventDetailsScreen = new EventDetailsScreen(driver);
    }

    @Description("1.Create new event with location on Android platform")
    @Test
    public void createEventTest() {
        calendarAndroidScreen
                .tapAddNewEventButton()
                .selectEventButton();
        newEventScreen
                .enterEventTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin())
                .enterLocation(testEvent.getLocation())
                .saveEvent();
        Assert.assertTrue("The event is missed in the list of events", calendarAndroidScreen.isEventCreated(testEvent.getEventName()));
        calendarAndroidScreen
                .openEvent(testEvent.getEventName());
        Assertions.assertAll(
                () -> assertEquals(testEvent.getEventName(), eventDetailsScreen.getActualName(), "Event name is incorrect"),
                () -> assertEquals(testEvent.getAndroidEventDetails(), eventDetailsScreen.getActualTime(), "Event time is incorrect"),
                () -> assertEquals(testEvent.getLocation(), eventDetailsScreen.getActualLocation(testEvent.getLocation()), "Location is incorrect")
        );
    }

    @Description("2. Create new event with push notification on Android platform")
    @Test
    public void createEventWithPushN() {
        calendarAndroidScreen
                .tapAddNewEventButton()
                .selectEventButton();
        newEventScreen
                .enterEventTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin())
                .addNotification()
                .saveEvent();
        Assert.assertTrue("The event is missed in the list of events", calendarAndroidScreen.isEventCreated(testEvent.getEventName()));
        driver.openNotifications();
        Assertions.assertAll(
                () -> assertEquals(calendarAndroidScreen.getEntityOfPushNotifications(), 1, "Calendar push notification is not the only one"),
                () -> assertTrue(calendarAndroidScreen.validateTitleFromPush(testEvent.getEventName()), "Event title is not correct"),
                () -> assertTrue(calendarAndroidScreen.validateTimeFromPush(testEvent.getAndroidTimeFromPush()), "Event time is not correct"))
        ;
    }

    @AfterEach
    public void deleteEvent() {
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        driver.launchApp();
        calendarAndroidScreen.deleteEvent(testEvent.getEventName());
    }

    @AfterAll
    public void driverTearDown() throws IOException {
        if (driver != null) {
            AppiumDriverSingleton.quit();
        }
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }
}

