package advantageonlineshopping.com.tests;

import advantageonlineshopping.com.GlobalRegisterData;
import advantageonlineshopping.com.utils.AIUtils;
import advantageonlineshopping.com.utils.AllureUtils;
import advantageonlineshopping.com.Hooks;
import advantageonlineshopping.com.pages.HomePage;
import advantageonlineshopping.com.pages.RegistrationPage;
import advantageonlineshopping.com.utils.MongoDBUtils;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static advantageonlineshopping.com.utils.AllureUtils.screenshot;

@ExtendWith(AllureJunit5.class)
public class LoginTest extends Hooks {

    @Test
    void userSignup() throws Exception {
        MongoDBUtils.init("local");

        // Gera os dados da IA
        Map<String, String> userData = AIUtils.generateUserData();

        // Armazena nas variáveis globais
        AIUtils.populateGlobalRegisterData(userData);

        // Salva no MongoDB
        MongoDBUtils.saveRegisterData("credentialsTests");

        stepNavigateToHomePage();
        stepNavigateToRegister();

        // Preenche o formulário usando as variáveis globais
        stepRegisterInfos();

        MongoDBUtils.close();
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

        register.usernameInputFill(GlobalRegisterData.USERNAME);
        register.emailInputFill(GlobalRegisterData.EMAIL);
        register.passwordInputFill(GlobalRegisterData.PASSWORD);
        register.confirmPasswordInputFill(GlobalRegisterData.CONFIRM_PASSWORD);
        register.firstnameInputFill(GlobalRegisterData.FIRSTNAME);
        register.lastnameInputFill(GlobalRegisterData.LASTNAME);
        register.phoneNumberFill(GlobalRegisterData.PHONE_NUMBER);
        register.countrySelect(GlobalRegisterData.COUNTRY);
        register.cityInputFill(GlobalRegisterData.CITY);
        register.adressInputFill(GlobalRegisterData.ADDRESS);
        register.stateInputFill(GlobalRegisterData.STATE);
        register.postalCodeFill(GlobalRegisterData.POSTAL_CODE);
        AllureUtils.screenshot(page, "Register fill");
    }

}
