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

    /**
     * Sends a prompt to the OpenRouter API and extracts a structured JSON response containing user registration data.
     *
     * @param model          The AI model to use (e.g., "gpt-4", "mistral").
     * @param systemMessage  Instruction or context message from the system (e.g., behavior guideline).
     * @param userPrompt     User input that should trigger the data generation.
     * @return A map containing the generated user registration data.
     * @throws Exception If any step of the request or response handling fails.
     */
    public static Map<String, String> generateUserData(String model, String systemMessage, String userPrompt) throws Exception {
        validateApiKey();
        String requestBody = buildJsonRequest(model, systemMessage, userPrompt);
        String response = callApi(requestBody);
        String cleanedJson = extractJsonFromResponse(response);
        return convertJsonToMap(cleanedJson);
    }

    /**
     * Verifies if the API key is available as an environment variable.
     * Throws an exception if the key is missing or empty.
     */
    private static void validateApiKey() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API key not found in environment variable OPENROUTER_API_KEY");
        }
    }

    /**
     * Builds the JSON string to be sent in the HTTP POST request to the OpenRouter API.
     *
     * @param model         The name of the AI model to be used.
     * @param systemMessage A message defining the system behavior/context.
     * @param userPrompt    The user's input or instruction.
     * @return A properly formatted JSON string.
     */
    private static String buildJsonRequest(String model, String systemMessage, String userPrompt) {
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

    /**
     * Sends the HTTP request to the OpenRouter API and returns the raw response.
     *
     * @param jsonRequest The request payload (in JSON format).
     * @return The API response as a raw string.
     * @throws Exception If the API call fails or returns a non-200 status code.
     */
    private static String callApi(String jsonRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenRouter API error: " + response.body());
        }

        return response.body();
    }

    /**
     * Extracts the JSON string containing the user data from the AI response.
     * It assumes the JSON is embedded within the "content" field of the first message choice.
     *
     * @param response The full response from the OpenRouter API.
     * @return A clean JSON string extracted from the content.
     * @throws Exception If the expected JSON structure is not found.
     */
    private static String extractJsonFromResponse(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
        String content = contentNode.asText().strip();

        int startIndex = content.indexOf('{');
        int endIndex = content.lastIndexOf('}');

        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            throw new RuntimeException("Invalid JSON received from API");
        }

        return content.substring(startIndex, endIndex + 1);
    }

    /**
     * Converts a JSON-formatted string into a Map<String, String>.
     *
     * @param jsonString The JSON string representing user data.
     * @return A map with keys and values parsed from the JSON.
     * @throws Exception If the JSON cannot be parsed.
     */
    private static Map<String, String> convertJsonToMap(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});
    }

    /**
     * Populates the GlobalRegisterData class with values from the provided data map.
     *
     * @param data A map containing user registration data (e.g., username, email, password).
     */
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

