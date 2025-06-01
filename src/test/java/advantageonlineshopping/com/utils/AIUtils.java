package advantageonlineshopping.com.utils;

import advantageonlineshopping.com.GlobalRegisterData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

public class AIUtils {

    private static final String API_KEY = System.getenv("OPENROUTER_API_KEY");
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public static Map<String, String> generateUserData() throws Exception {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API key não encontrada na variável de ambiente OPENROUTER_API_KEY");
        }

        HttpClient client = HttpClient.newHttpClient();

        String jsonRequest = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "system", "content": "Você é um gerador de dados de registro. É importante que você seja criativo!!!"},
            {"role": "user", "content": "Gere um JSON puro, sem texto adicional, contendo os campos username, email, password <Use  4 character or longer, Use maximum 12 character, Including at least one lower letter, Including at least one upper letter, Including at least one number>, confirmPassword, firstname, lastname, phoneNumber, country<nome real em inglês>, city, address, state, postalCode."}
          ]
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API OpenRouter: " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
        String content = contentNode.asText().strip();

        int startIndex = content.indexOf('{');
        int endIndex = content.lastIndexOf('}');

        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            throw new RuntimeException("JSON inválido recebido da API");
        }

        String jsonString = content.substring(startIndex, endIndex + 1);

        return mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});
    }

    public static void populateGlobalRegisterData(Map<String, String> data) {
        GlobalRegisterData.USERNAME = data.get("username");
        GlobalRegisterData.EMAIL = data.get("email");
        GlobalRegisterData.PASSWORD = data.get("password");
        GlobalRegisterData.CONFIRM_PASSWORD = data.get("confirmPassword");
        GlobalRegisterData.FIRSTNAME = data.get("firstname");
        GlobalRegisterData.LASTNAME = data.get("lastname");
        GlobalRegisterData.PHONE_NUMBER = data.get("phoneNumber");
        GlobalRegisterData.COUNTRY = data.get("country");
        GlobalRegisterData.CITY = data.get("city");
        GlobalRegisterData.ADDRESS = data.get("address");
        GlobalRegisterData.STATE = data.get("state");
        GlobalRegisterData.POSTAL_CODE = data.get("postalCode");
    }
}

