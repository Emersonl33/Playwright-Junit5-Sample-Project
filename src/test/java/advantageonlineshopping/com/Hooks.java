package advantageonlineshopping.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Hooks {

    public Playwright playwright;
    public Browser browser;
    public BrowserContext context;
    public Page page;

    @BeforeAll
    void globalSetup() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void cleanup() {
        context.close();
    }

    @AfterAll
    void globalTeardown() {
        browser.close();
        playwright.close();
    }

}
