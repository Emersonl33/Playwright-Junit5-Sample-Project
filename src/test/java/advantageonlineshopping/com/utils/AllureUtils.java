package advantageonlineshopping.com.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class AllureUtils {

    /**
     * Captura um screenshot da página atual e anexa ao relatório Allure.
     * @param page Instância do Playwright Page.
     * @param title Título/descrição da imagem no relatório.
     */
    public static void screenshot(Page page, String title) {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(title, new ByteArrayInputStream(screenshot));
    }

    /**
     * Anexa um texto de log ao relatório Allure.
     * @param name Título/descrição do log no relatório.
     * @param text Conteúdo do log.
     */
    public static void attachLog(String name, String text) {
        Allure.addAttachment(name, "text/plain", text, ".txt");
    }

}
