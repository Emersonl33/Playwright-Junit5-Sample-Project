package advantageonlineshopping.com.tests;

import advantageonlineshopping.com.utils.AllureUtils;
import advantageonlineshopping.com.Hooks;
import advantageonlineshopping.com.pages.HomePage;
import advantageonlineshopping.com.pages.RegistrationPage;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static advantageonlineshopping.com.utils.AllureUtils.screenshot;

@ExtendWith(AllureJunit5.class)
public class LoginTest extends Hooks {

    @Test
    void userSignup(){
        stepNavigateToHomePage();
        stepNavigateToHomePage();
        stepNavigateToRegister();

    }

    @Step("Navigate to home page")
    void stepNavigateToHomePage() {
        AllureUtils.attachLog("Logs de execução", "Iniciando o teste...\nPasso 1 executado com sucesso");
        page.navigate("https://advantageonlineshopping.com/#/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        screenshot(page, "Entry in home page");
    }

    @Step("Navigate to register page")
    void stepNavigateToRegister(){
        HomePage home = new HomePage(page);
        home.userIconClick();
        AllureUtils.screenshot(page, "Enter register page");
        home.createNewAccountLabelClick();
    }

    @Step("Fill register infos")
    void stepRegisterInfos(){
        RegistrationPage register = new RegistrationPage(page);
        register.usernameInputFill("");
        register.emailInputFill("");
        register.passwordInputFill("");
        register.confirmPasswordInputFill("");
        register.firstnameInputFill("");
        register.lastnameInputFill("");
        register.phoneNumberFill("");
        register.countrySelect("");
        register.adressInputFill("");
        register.stateInputFill("");
        register.stateInputFill("");
        register.postalCodeFill("");
        AllureUtils.screenshot(page, "Register fill");
    }

}
