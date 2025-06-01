package advantageonlineshopping.com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ToolsUtils {

    /**
     * Realça o elemento e clica nele,
     *
     * @param locator Locator do elemento a ser clicado.
     */
    public static void highlightAndClick(Locator locator) {
        locator.evaluate("el => { " +
                "el.style.border = '3px solid darkgreen'; " +
                "el.style.backgroundColor = 'lightgreen'; " +
                "}");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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

    public static void inputFill(Locator locator, String fill){
        highlightAndClick(locator);
        locator.clear();
        locator.fill(fill);
    }
}
