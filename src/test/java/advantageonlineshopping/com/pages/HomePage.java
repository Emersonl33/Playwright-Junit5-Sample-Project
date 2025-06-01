package advantageonlineshopping.com.pages;

import advantageonlineshopping.com.utils.ToolsUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static advantageonlineshopping.com.utils.ToolsUtils.highlightAndClick;


public class HomePage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private final Page page;

    public HomePage (Page page) {
        this.page = page;
    }

    //navbar
    private Locator userIcon() {
        return page.locator("#menuUserLink");
    }

    private Locator usernameLabel() {
        return page.locator("[class='hi-user containMiniTitle ng-binding']");
    }

    private Locator createNewAccountLabel() {
        return page.locator("[translate='CREATE_NEW_ACCOUNT']");
    }

    public void userIconClick() {
        highlightAndClick(userIcon());
    }

    public void createNewAccountLabelClick() {
        highlightAndClick(createNewAccountLabel());
    }

    public String userIconGetText() {
        ToolsUtils.highlightVisibility(usernameLabel());
        return usernameLabel().innerText().trim();
    }

}
