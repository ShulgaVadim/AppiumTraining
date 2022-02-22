package screens.ios;

import screens.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;

import java.util.List;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;

public class NewEventScreen extends BaseScreen {

    private AppiumDriver driver;
    private final By TITLE_FIELD = MobileBy.iOSNsPredicateString("name == 'Title'");
    private final By LOCATION_FIELD = MobileBy.iOSNsPredicateString("name == 'Location or Video Call'");
    private final By ENTER_LOCATION_FIELD = MobileBy.AccessibilityId("Enter Location or Video Call");
    private final By DONE_BUTTON = MobileBy.AccessibilityId("Done");
    private final By START_DATE_BUTTON = MobileBy.iOSClassChain("**/XCUIElementTypeOther[`label == 'Date and Time Picker'`][1]/XCUIElementTypeButton[2]");
    private final By START_TIME_WHEEL = MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel");
    private final By END_TIME_WHEEL = MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel");
    private final By END_DATE_BUTTON = MobileBy.iOSClassChain("**/XCUIElementTypeOther[`label == 'Date and Time Picker'`][2]/XCUIElementTypeButton[2]");
    private final By ADD_BUTTON = MobileBy.iOSNsPredicateString("label == 'Add' AND visible == 1");
    private final By ALERT_BUTTON = MobileBy.iOSClassChain("**/XCUIElementTypeCell[`value == \"None\"`][2]");
    private final By AT_TIME_OF_EVENT = MobileBy.AccessibilityId("At time of event");

    public NewEventScreen(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public NewEventScreen enterTitle(String title) {
        findWithWait(TITLE_FIELD).sendKeys(title);
        return this;
    }

    public NewEventScreen enterLocation(String location) {
        findWithWait(LOCATION_FIELD).click();
        findWithWait(ENTER_LOCATION_FIELD).sendKeys(location);
        findWithWait(DONE_BUTTON).click();
        return this;
    }

    public NewEventScreen enterStartDate(String startHours, String startMins, String startDayTime) {
        findWithWait(START_DATE_BUTTON).click();
        List<MobileElement> wheelsStart = driver.findElements(START_TIME_WHEEL);
        wheelsStart.get(2).sendKeys(startDayTime);
        wheelsStart.get(0).sendKeys(startHours);
        doubleClick(wheelsStart.get(1));
        driver.getKeyboard().sendKeys(startMins);
        findWithWait(START_DATE_BUTTON).click();
        return this;
    }

    public NewEventScreen enterEndDate(String endHours, String endMins, String endDayTime) {
        findWithWait(END_DATE_BUTTON).click();
        List<MobileElement> wheelsEnd = driver.findElements(END_TIME_WHEEL);
        wheelsEnd.get(2).sendKeys(endDayTime);
        wheelsEnd.get(0).sendKeys(endHours);
        doubleClick(wheelsEnd.get(1));
        driver.getKeyboard().sendKeys(endMins);
        return this;
    }

    public void doubleClick(MobileElement element) {
        new TouchAction(driver).tap(tapOptions()
                        .withTapsCount(2)
                        .withElement(element(element)))
                .perform();
    }

    public NewEventScreen addAlert() {
        driver.findElement(ALERT_BUTTON).click();
        findWithWait(AT_TIME_OF_EVENT).click();
        return this;
    }

    public CalendarIosScreen tapAddButton() {
        findWithWait(ADD_BUTTON).click();
        return new CalendarIosScreen(driver);
    }
}
