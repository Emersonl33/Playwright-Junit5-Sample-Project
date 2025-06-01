package advantageonlineshopping.com.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class AllureUtils {

    /**
     * Captures a screenshot of the current page and attaches it to the Allure report.
     *
     * @param page  An instance of Playwright's Page object representing the current browser page.
     * @param title A title or description that will appear as the name of the screenshot in the Allure report.
     */
    public static void screenshot(Page page, String title) {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(title, new ByteArrayInputStream(screenshot));
    }

    /**
     * Attaches plain text content to the Allure report as a log entry.
     *
     * @param name A title or description that will appear as the name of the log entry in the Allure report.
     * @param text The actual content of the log to be attached.
     */
    public static void attachLog(String name, String text) {
        Allure.addAttachment(name, "text/plain", text, ".txt");
    }


}
