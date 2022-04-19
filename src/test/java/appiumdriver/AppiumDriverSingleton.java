package appiumdriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.CapabilitiesReader;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppiumDriverSingleton {

    private static AppiumDriver driver;

    private AppiumDriverSingleton() {
    }

    public static <T extends AppiumDriver> T getInstance() throws Exception {
        if (driver == null) {
            String appEnv = System.getProperty("app.env");
            DesiredCapabilities caps = new CapabilitiesReader().getDesiredCapabilities(appEnv, "/src/test/resources/capabilities.json");
            URL driverURL = new URL("http://0.0.0.0:4723/wd/hub");
            switch (appEnv) {
                case ("android"):
                    driver = new AndroidDriver(driverURL, caps);
                    break;
                case ("ios"):
                    driver = new IOSDriver(driverURL, caps);
                default:
                    driver = new AndroidDriver(driverURL, caps);
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return (T) driver;
    }

    public static void quit() {
        driver.quit();
    }
}
