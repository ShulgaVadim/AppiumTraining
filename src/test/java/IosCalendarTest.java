import appiumdriver.AppiumDriverSingleton;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import screens.ios.CalendarIosScreen;
import screens.ios.NewEventScreen;
import io.appium.java_client.ios.IOSDriver;
import org.junit.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IosCalendarTest {

    IOSDriver driver;
    CalendarIosScreen calendarIosScreen;
    NewEventScreen newEventScreen;
    Event testEvent = new Event("Test Event", 2, 5, "Chicago");

    @Before
    public void setUp() throws Exception {
        driver = AppiumDriverSingleton.getInstance();
        calendarIosScreen = new CalendarIosScreen(driver);
        newEventScreen = new NewEventScreen(driver);
    }

    @Description("3.Create new event on iOS platform")
    @Test
    public void createEvent() {
        driver.resetApp();
        calendarIosScreen
                .tapTodayButton()
                .tapAddNewEventButton();
        newEventScreen
                .enterTitle(testEvent.getEventName())
                .enterLocation(testEvent.getLocation())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin(), testEvent.getStartDayTime())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin(), testEvent.getEndDayTime())
                .tapAddButton();
        Assert.assertTrue("The event is missed in the list of events", calendarIosScreen.isEventCreated(testEvent.getEventName()));
        Assert.assertEquals("Expected event details don't match to actual ones", calendarIosScreen.getExpectedDetails(testEvent.getEventName()), testEvent.getIosEventDetails());
    }

    @Description("4.Create new event with alert on iOS platform")
    @Test
    public void createEventWithAlert() {
        driver.resetApp();
        calendarIosScreen
                .tapTodayButton()
                .tapAddNewEventButton();
        newEventScreen
                .enterTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin(), testEvent.getStartDayTime())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin(), testEvent.getEndDayTime())
                .addAlert()
                .tapAddButton();
        Assert.assertTrue("The event is missed in the list of events", calendarIosScreen.isEventCreated(testEvent.getEventName()));
        calendarIosScreen.showNotifications();
        Assertions.assertAll(
                () -> assertEquals(calendarIosScreen.getEntityOfPushNotifications(), 1, "Calendar push notification is not the only one"),
                () -> assertEquals(calendarIosScreen.getDetailsFromAlert(), testEvent.getIosDetailsFromPush(), "Expected event details don't match to actual ones"))
        ;
        calendarIosScreen.hideNotifications();
    }

    @After
    public void deleteEvent() {
        driver.closeApp();
        driver.launchApp();
        calendarIosScreen.deleteEvent(testEvent.getEventName());
    }

    @After
    public void driverTearDown() throws IOException {
        Runtime.getRuntime().exec("xcrun simctl shutdown 985A2290-A9D8-4A7C-89B2-73C6211EAC93");
        driver.quit();
    }
}


