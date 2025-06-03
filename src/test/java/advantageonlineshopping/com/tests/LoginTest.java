package advantageonlineshopping.com.tests;

import advantageonlineshopping.com.Hooks;
import advantageonlineshopping.com.data.GlobalRegisterData;
import advantageonlineshopping.com.pages.HomePage;
import advantageonlineshopping.com.pages.LoginPage;
import advantageonlineshopping.com.pages.RegistrationPage;
import advantageonlineshopping.com.utils.AllureUtils;
import advantageonlineshopping.com.utils.MongoDBUtils;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static advantageonlineshopping.com.utils.AllureUtils.screenshot;

@Slf4j
@ExtendWith(AllureJunit5.class)
public class LoginTest extends Hooks {

    LoginPage login = new LoginPage(page);
    HomePage home = new HomePage(page);
    RegistrationPage register = new RegistrationPage(page);

    @Test
    void loginUserTest(){
        login = new LoginPage(page);
        home = new HomePage(page);
        register = new RegistrationPage(page);
        stepNavigateToHomePage();
        stepNavigateToLogin();
        foundUserDataDB();
        stepUserLogin();
        stepConfirmLogin();
    }

    @Step("Found user data in mongoDB")
    void foundUserDataDB(){
        MongoDBUtils.init("local");

        boolean loaded = MongoDBUtils.loadFirstRegisterData("credentialsTests");

        if (loaded) {
            log.info("User found: " + GlobalRegisterData.USERNAME);
        } else {
            log.error("User not found");
        }
    }

    @Step("Navigate to home page")
    void stepNavigateToHomePage() {
        AllureUtils.attachLog("Execution logs", "Starting the test...\nStep 1 executed successfully");
        page.navigate("https://advantageonlineshopping.com/#/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        screenshot(page, "Entry in home page");
    }

    @Step("Navigate to login page")
    void stepNavigateToLogin() {
        home.userIconClick();
    }

    @Step("log in the user")
    void stepUserLogin(){
        login.fillUsernameLogin(GlobalRegisterData.USERNAME);
        login.fillPasswordLogin(GlobalRegisterData.PASSWORD);
        login.clickSigninButton();
    }

    @Step("Confirm login")
    void stepConfirmLogin() {
        screenshot(page, "Register confirmation");
        String usernameUserIcon = home.userIconGetText();
        Assertions.assertEquals(
                GlobalRegisterData.USERNAME, usernameUserIcon,
                "Register failed. The username displayed in the icon does not match the one used during registration.\n" +
                        "Username found: " + usernameUserIcon + " | Expected: " + GlobalRegisterData.USERNAME
        );

        log.info("Registration completed successfully.");
        screenshot(page, "Register confirmation");
    }

}
