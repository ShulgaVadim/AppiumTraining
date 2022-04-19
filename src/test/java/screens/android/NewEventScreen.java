package screens.android;

import io.qameta.allure.Step;
import screens.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class NewEventScreen extends BaseScreen {

    private AppiumDriver driver;

    private final By TITLE = MobileBy.id("title_edit_text");
    private final By START_TIME_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Start time\")");
    private final By KEYBOARD_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.ImageButton\")");
    private final By START_HOURS = MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_hour\")");
    private final By START_MINS = MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_minute\")");
    private final By OK_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\")");
    private final By END_TIME_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"End time\")");
    private final By END_HOURS = MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_hour\")");
    private final By END_MINS = MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_minute\")");
    private final By ADD_LOCATION_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").text(\"Add location\")");
    private final By SEARCH_BUTTON = MobileBy.id("search_field");
    private final By SAVE_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Save\")");
    private final By DELETE_DEFAULT_NOTIFICATION = MobileBy.AccessibilityId("Remove notification");
    private final By ADD_NOTIFICATION = MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").text(\"Add a notification\")");
    private final By AT_TIME_OF_EVENT = MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").text(\"At time of event\")");


    public NewEventScreen(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Step("Enter title {eventName} to the title field")
    public NewEventScreen enterEventTitle(String eventName) {
        findWithWait(TITLE).sendKeys(eventName);
        return this;
    }

    @Step("Enter start date {startHours}:{startMins} to the start time field")
    public NewEventScreen enterStartDate(String startHours, String startMins) {
        driver.findElement(START_TIME_BUTTON).click();
        driver.findElement(KEYBOARD_BUTTON).click();
        findWithWait(START_HOURS).sendKeys(startHours);
        driver.findElement(START_MINS).sendKeys(startMins);
        driver.findElement(OK_BUTTON).click();
        return this;
    }

    @Step("Enter end date {endHours}:{endMins} to the end time field")
    public NewEventScreen enterEndDate(String endHours, String endMins) {
        driver.findElement(END_TIME_BUTTON).click();
        driver.findElement(KEYBOARD_BUTTON).click();
        findWithWait(END_HOURS).sendKeys(endHours);
        driver.findElement(END_MINS).sendKeys(endMins);
        driver.findElement(OK_BUTTON).click();
        return this;
    }

    @Step("Enter location {location} to the location field")
    public NewEventScreen enterLocation(String location) {
        findWithWait(ADD_LOCATION_BUTTON).click();
        findWithWait(SEARCH_BUTTON).sendKeys(location);
        driver.getKeyboard().pressKey(Keys.ENTER);
        return this;
    }

    @Step("Add notification for event")
    public NewEventScreen addNotification() {
        driver.findElement(DELETE_DEFAULT_NOTIFICATION).click();
        driver.findElement(ADD_NOTIFICATION).click();
        findWithWait(AT_TIME_OF_EVENT).click();
        return this;
    }

    @Step("Click 'Save' button ")
    public CalendarAndroidScreen saveEvent() {
        driver.findElement(SAVE_BUTTON).click();
        return new CalendarAndroidScreen(driver);
    }
}
