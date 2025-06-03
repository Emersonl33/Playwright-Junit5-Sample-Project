package advantageonlineshopping.com.pages;

import advantageonlineshopping.com.data.GlobalRegisterData;
import advantageonlineshopping.com.utils.ToolsUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage (Page page){
        this.page = page;
    }

    private Locator usernameLoginInput(){
        return page.locator("[name='username']");
    }

    private Locator passwordLoginInput(){
        return page.locator("[name='password']");
    }

    private Locator signinButton(){
        return page.locator("#sign_in_btn");
    }

    public void fillUsernameLogin(String username){
        GlobalRegisterData.USERNAME = username;
        ToolsUtils.highlightScrollAndFill(usernameLoginInput(), username);
    }

    public void fillPasswordLogin(String password){
        GlobalRegisterData.PASSWORD = password;
        ToolsUtils.highlightScrollAndFill(passwordLoginInput(), password);
    }

    public void clickSigninButton(){
        ToolsUtils.highlightAndClick(signinButton());
    }

}


