package Screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BaseScreen {

    private AppiumDriver driver;
    private Dimension screenSize;

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    private ExpectedCondition<MobileElement> elementIsDisplayed(By by) {
        return AppiumDriver -> {
            List<MobileElement> list;
            list = AppiumDriver.findElements(by);
            if (list.size() > 0 && list.get(0).isDisplayed()) {
                return list.get(0);
            } else return null;
        };
    }

    public MobileElement findWithWait(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        return wait.until(elementIsDisplayed(by));
    }

    public void showNotifications() {
        manageNotifications(true);
    }

    public void hideNotifications() {
        manageNotifications(false);
    }

    private void manageNotifications(Boolean show) {
        screenSize = driver.manage().window().getSize();
        int yMargin = 5;
        int xMid = screenSize.width / 2;
        PointOption top = PointOption.point(xMid, yMargin);
        PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);

        TouchAction action = new TouchAction(driver);
        if (show) {
            action.press(top);
        } else {
            action.press(bottom);
        }
        action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        if (show) {
            action.moveTo(bottom);
        } else {
            action.moveTo(top);
        }
        action.perform();
    }
}
