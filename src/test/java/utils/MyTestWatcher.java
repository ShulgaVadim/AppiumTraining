package utils;

import appiumdriver.AppiumDriverSingleton;
import io.qameta.allure.Attachment;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Log4j2
public class MyTestWatcher implements TestWatcher, AfterTestExecutionCallback {

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        log.info("Test Aborted for test {}: ", extensionContext.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        log.info("Test Disabled for test {}: with reason :- {}",
                extensionContext.getDisplayName(),
                optional.orElse("No reason"));
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        log.info("Test Successful for test {}: ", extensionContext.getDisplayName());
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshot(String name) throws Exception {
        String timestamp = new SimpleDateFormat("yyyy_MM-dd__hh_mm_ss").format(new Date());
        File scrFile = ((TakesScreenshot) AppiumDriverSingleton.getInstance()).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/target/allure-results/screenshot_" + timestamp + ".png"));
        return ((TakesScreenshot) AppiumDriverSingleton.getInstance()).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        if (extensionContext.getExecutionException().isPresent()) {
            log.info("Test {} Failed for testcase {}: with reason :- {}",
                    extensionContext.getUniqueId(), extensionContext.getDisplayName(), extensionContext.getExecutionException());
            try {
                takeScreenshot(extensionContext.getDisplayName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}