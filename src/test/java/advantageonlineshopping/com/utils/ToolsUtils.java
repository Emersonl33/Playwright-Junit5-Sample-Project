package advantageonlineshopping.com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;


public class ToolsUtils {

    /**
     * Highlights the given element by applying a green border and background,
     * then clicks on it.
     *
     * @param locator Locator of the element to be clicked.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    @SneakyThrows
    public static void highlightAndClick(Locator locator) {
        locator.evaluate("el => { " +
                "el.style.border = '3px solid darkgreen'; " +
                "el.style.backgroundColor = 'lightgreen'; " +
                "}");

        Thread.sleep(500);

        locator.scrollIntoViewIfNeeded();
        locator.click();

        locator.evaluate("el => { " +
                "el.style.border = ''; " +
                "el.style.backgroundColor = ''; " +
                "}");
    }

    /**
     * Highlights and clicks an element found by a CSS selector on the given page.
     *
     * @param page        The Playwright Page instance where the element is located.
     * @param cssSelector The CSS selector string to locate the element.
     */
    public static void highlightAndClick(Page page, String cssSelector) {
        Locator locator = page.locator(cssSelector);
        highlightAndClick(locator);
    }

    /**
     * Highlights the given element by applying a blue border and light blue background,
     * scrolls to it, clears any existing content, and fills it with the provided value.
     *
     * @param locator Locator of the element to fill.
     * @param value   The string value to fill into the element.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    @SneakyThrows
    public static void highlightScrollAndFill(Locator locator, String value) {
        locator.scrollIntoViewIfNeeded();

        locator.evaluate("el => { " +
                "el.style.border = '2px solid darkblue'; " +
                "el.style.backgroundColor = '#e6f2ff'; " +
                "}");

        Thread.sleep(300);

        locator.clear();
        locator.fill(value);

        Thread.sleep(500);

        locator.evaluate("el => { " +
                "el.style.border = ''; " +
                "el.style.backgroundColor = ''; " +
                "}");
    }

    /**
     * Highlights the given element by applying a green border and background,
     * scrolls to it, then removes the highlight after a short delay.
     *
     * @param locator Locator of the element to highlight.
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    @SneakyThrows
    public static void highlightVisibility(Locator locator) {
        locator.scrollIntoViewIfNeeded();

        locator.evaluate("el => { " +
                "el.style.border = '3px solid darkgreen'; " +
                "el.style.backgroundColor = 'lightgreen'; " +
                "}");

        Thread.sleep(500);

        locator.evaluate("el => { " +
                "el.style.border = ''; " +
                "el.style.backgroundColor = ''; " +
                "}");
    }


}
