package screens.android;

import screens.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class EventDetailsScreen extends BaseScreen {
    private AppiumDriver driver;

    public final By ACTUAL_NAME = MobileBy.id("title");
    public final By ACTUAL_TIME = MobileBy.id("time");
    String expectedLocation = "new UiSelector().resourceId(\"com.google.android.calendar:id/first_line_text\").textContains(\"%s\")";

    public EventDetailsScreen(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public String getActualName() {
        return findWithWait(ACTUAL_NAME).getText();
    }

    public String getActualTime() {
        return findWithWait(ACTUAL_TIME).getText();
    }

    public String getActualLocation(String location) {
        return driver.findElement(MobileBy.AndroidUIAutomator(String.format(expectedLocation, location))).getText();
    }
}
