package Screens.Android;

import Screens.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarAndroidScreen extends BaseScreen {

    private AppiumDriver driver;
    String eventLocator = "new UiSelector().descriptionContains(\"%s\")";

    private final By ADD_NEW_EVENT_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Create new event\")");
    private final By EVENT_BUTTON = MobileBy.AndroidUIAutomator("new UiSelector().text(\"Event\")");
    private final By OK_BUTTON = MobileBy.id("android:id/button1");
    private final By TIME_FROM_PUSH = MobileBy.xpath("//android.widget.ScrollView[@resource-id=\"com.android.systemui:id/notification_stack_scroller\"]/android.widget.FrameLayout[1]//android.widget.TextView[@resource-id=\"android:id/big_text\"]");
    String titleFromPushLocator = "//android.widget.TextView[@resource-id=\"android:id/title\" and contains(@text,'%s')]";

    public CalendarAndroidScreen(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public CalendarAndroidScreen tapAddNewEventButton() {
        findWithWait(ADD_NEW_EVENT_BUTTON).click();
        return this;
    }

    public NewEventScreen selectEventButton() {
        findWithWait(EVENT_BUTTON).click();
        return new NewEventScreen(driver);
    }

    public Boolean isEventCreated(String eventName) {
        return !driver.findElements(MobileBy.AndroidUIAutomator(String.format(eventLocator, eventName))).isEmpty();
    }

    public EventDetailsScreen openEvent(String eventName) {
        findWithWait(MobileBy.AndroidUIAutomator(String.format(eventLocator, eventName))).click();
        return new EventDetailsScreen(driver);
    }

    public CalendarAndroidScreen deleteEvent(String eventName) {
        if (!driver.findElements(MobileBy.AndroidUIAutomator(String.format(eventLocator, eventName))).isEmpty()) {
            MobileElement event = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(String.format(eventLocator, eventName)));
            Dimension elementSize = event.getSize();
            int y = event.getCenter().getY();
            int startX = (int) (0.2 * elementSize.getWidth());
            int endX = (int) (0.8 * elementSize.getWidth());
            new TouchAction(driver)
                    .press(PointOption.point(startX, y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                    .moveTo(PointOption.point(endX, y))
                    .release()
                    .perform();
            driver.findElement(OK_BUTTON).click();
        }
        return this;
    }

    public boolean validateTitleFromPush(String title) {
        Pattern pattern = Pattern.compile(title);
        Matcher matcher = pattern.matcher(getTitleFromPush(title));
        return matcher.find();
    }

    public String getTitleFromPush(String title) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.xpath(String.format(titleFromPushLocator, title)))).getText();
    }

    public boolean validateTimeFromPush(String time) {
        Pattern pattern = Pattern.compile(time);
        Matcher matcher = pattern.matcher(getTimeFromPush());
        return matcher.find();
    }

    public String getTimeFromPush() {
        return findWithWait(TIME_FROM_PUSH).getText();
    }
}
