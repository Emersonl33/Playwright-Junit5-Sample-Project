import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import utils.ReportGenerator;


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
        try {
            ReportGenerator.generateAllureReport();
        } catch (Exception e) {
            // Don't fail tests because report generation failed; just log
            System.err.println("Failed to generate Allure report: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
