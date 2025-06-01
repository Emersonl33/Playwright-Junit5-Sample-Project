package advantageonlineshopping.com.utils;

import advantageonlineshopping.com.data.GlobalRegisterData;
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

    public static Map<String, String> generateUserData(String model, String systemMessage, String userPrompt) throws Exception {
        validarApiKey();
        String requestBody = montarRequisicaoJson(model, systemMessage, userPrompt);
        String resposta = chamarApi(requestBody);
        String jsonLimpo = extrairJsonDaResposta(resposta);
        return converterJsonParaMap(jsonLimpo);
    }

    private static void validarApiKey() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API key não encontrada na variável de ambiente OPENROUTER_API_KEY");
        }
    }

    private static String montarRequisicaoJson(String model, String systemMessage, String userPrompt) {
        return """
        {
          "model": "%s",
          "messages": [
            {"role": "system", "content": "%s"},
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(model, systemMessage, userPrompt);
    }

    private static String chamarApi(String jsonRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

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

        return response.body();
    }

    private static String extrairJsonDaResposta(String resposta) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(resposta);
        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
        String content = contentNode.asText().strip();

        int startIndex = content.indexOf('{');
        int endIndex = content.lastIndexOf('}');

        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            throw new RuntimeException("JSON inválido recebido da API");
        }

        return content.substring(startIndex, endIndex + 1);
    }

    private static Map<String, String> converterJsonParaMap(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                jsonString, new TypeReference<Map<String, String>>() {}
        );
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

