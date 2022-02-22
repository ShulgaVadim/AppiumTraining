package screens.ios;

import screens.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.appium.java_client.touch.offset.ElementOption.element;

public class CalendarIosScreen extends BaseScreen {

    private AppiumDriver driver;

    private final By TODAY_BUTTON = MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Today'`]");
    private final By ADD_NEW_EVENT_BUTTON = MobileBy.iOSNsPredicateString("label == 'Add'");
    private final By LIST_BUTTON = MobileBy.AccessibilityId("List");
    private final By DELETE_BUTTON = MobileBy.AccessibilityId("Delete Event");
    private final By CONFIRM_DELETE_BUTTON = MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Delete Event'`]");
    String eventLocator = "**/XCUIElementTypeButton[`name CONTAINS[cd] '%s'`]";
    String eventLocatorInList = "**/XCUIElementTypeCell[`name CONTAINS[cd] '%s'`]";
    private final By ALERT_CALENDAR = MobileBy.iOSClassChain("**/XCUIElementTypeScrollView[`name CONTAINS[cd] 'CALENDAR'`]");


    public CalendarIosScreen(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public CalendarIosScreen tapTodayButton() {
        findWithWait(TODAY_BUTTON).click();
        return this;
    }

    public NewEventScreen tapAddNewEventButton() {
        findWithWait(ADD_NEW_EVENT_BUTTON).click();
        return new NewEventScreen(driver);
    }

    public Boolean isEventCreated(String eventName) {
        return !driver.findElements(MobileBy.iOSClassChain(String.format(eventLocator, eventName))).isEmpty();
    }

    public String getExpectedDetails(String eventName) {
        return findWithWait(MobileBy.iOSClassChain(String.format(eventLocator, eventName))).getText();
    }

    public int getEntityOfPushNotifications() {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ALERT_CALENDAR)).size();
    }

    public String getDetailsFromAlert() {
        return findWithWait(ALERT_CALENDAR).getText();
    }

    public CalendarIosScreen deleteEvent(String eventName) {
        if (!driver.findElements(MobileBy.iOSClassChain(String.format(eventLocator, eventName))).isEmpty()) {
            findWithWait(LIST_BUTTON).click();
            findWithWait(TODAY_BUTTON).click();
            MobileElement event = findWithWait(MobileBy.iOSClassChain(String.format(eventLocatorInList, eventName)));
            new TouchAction(driver)
                    .longPress(LongPressOptions
                            .longPressOptions()
                            .withElement(element(event))
                            .withDuration(Duration.ofSeconds(3)))
                    .release()
                    .perform();
            findWithWait(DELETE_BUTTON).click();
            findWithWait(CONFIRM_DELETE_BUTTON).click();
            findWithWait(LIST_BUTTON).click();
        }
        return this;
    }
}

