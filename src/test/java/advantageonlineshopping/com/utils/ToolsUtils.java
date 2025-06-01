package advantageonlineshopping.com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.SneakyThrows;


public class ToolsUtils {

    /**
     * Realça o elemento e clica nele,
     *
     * @param locator Locator do elemento a ser clicado.
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
     * Versão com Page e seletor CSS.
     */
    public static void highlightAndClick(Page page, String cssSelector) {
        Locator locator = page.locator(cssSelector);
        highlightAndClick(locator);
    }

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

    @SneakyThrows
    public static void highlightVisibility (Locator locator) {
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
