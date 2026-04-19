package advantageonlineshopping.com.utils;

import advantageonlineshopping.com.data.GlobalRegisterData;
import advantageonlineshopping.com.exceptions.ApiException;
import advantageonlineshopping.com.exceptions.InvalidResponseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.fasterxml.jackson.core.type.TypeReference;

public class AIUtils {

    private static final Logger LOGGER = Logger.getLogger(AIUtils.class.getName());
    private static final String API_KEY = System.getenv("OPENROUTER_API_KEY");
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Sends a prompt to the OpenRouter API and extracts a structured JSON response containing user registration data.
     *
     * @param model          The AI model to use (e.g., "gpt-4", "mistral").
     * @param systemMessage  Instruction or context message from the system (e.g., behavior guideline).
     * @param userPrompt     User input that should trigger the data generation.
     * @return A map containing the generated user registration data.
     * @throws ApiException If any step of the request or response handling fails.
     */
    public static Map<String, String> generateUserData(String model, String systemMessage, String userPrompt) throws ApiException {
        try {
            validateApiKey();
            String requestBody = buildJsonRequest(model, systemMessage, userPrompt);
            String response = callApi(requestBody);
            String cleanedJson = extractJsonFromResponse(response);
            return convertJsonToMap(cleanedJson);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating user data", e);
            throw new ApiException("Failed to generate user data: " + e.getMessage(), e);
        }
    }

    /**
     * Verifies if the API key is available as an environment variable.
     * Throws an exception if the key is missing or empty.
     */
    private static void validateApiKey() throws ApiException {
        if (API_KEY == null || API_KEY.isEmpty()) {
            LOGGER.log(Level.SEVERE, "API key not found");
            throw new ApiException("API key not found in environment variable OPENROUTER_API_KEY");
        }
    }

    /**
     * Builds the JSON string to be sent in the HTTP POST request to the OpenRouter API.
     * Uses ObjectMapper to properly escape special characters and avoid JSON injection.
     *
     * @param model         The name of the AI model to be used.
     * @param systemMessage A message defining the system behavior/context.
     * @param userPrompt    The user's input or instruction.
     * @return A properly formatted JSON string.
     * @throws ApiException If JSON serialization fails.
     */
    private static String buildJsonRequest(String model, String systemMessage, String userPrompt) throws ApiException {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            List<Map<String, String>> messages = Arrays.asList(
                    Map.of("role", "system", "content", systemMessage),
                    Map.of("role", "user", "content", userPrompt)
            );
            request.put("messages", messages);
            return OBJECT_MAPPER.writeValueAsString(request);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error building JSON request", e);
            throw new ApiException("Failed to build JSON request: " + e.getMessage(), e);
        }
    }

    /**
     * Sends the HTTP request to the OpenRouter API and returns the raw response.
     * Reuses a shared HttpClient instance and includes timeout configuration.
     *
     * @param jsonRequest The request payload (in JSON format).
     * @return The API response as a raw string.
     * @throws ApiException If the API call fails or returns a non-200 status code.
     */
    private static String callApi(String jsonRequest) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.log(Level.WARNING, "API error: Status code " + response.statusCode());
                throw new ApiException("OpenRouter API error (status " + response.statusCode() + "): " + response.body());
            }

            LOGGER.log(Level.FINE, "API call successful");
            return response.body();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calling API", e);
            throw new ApiException("Failed to call OpenRouter API: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts the JSON string containing the user data from the AI response.
     * It assumes the JSON is embedded within the "content" field of the first message choice.
     *
     * @param response The full response from the OpenRouter API.
     * @return A clean JSON string extracted from the content.
     * @throws ApiException If the expected JSON structure is not found.
     */
    private static String extractJsonFromResponse(String response) throws ApiException {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(response);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

            if (contentNode == null || contentNode.isMissingNode() || contentNode.isNull()) {
                LOGGER.log(Level.WARNING, "Invalid response structure: missing content");
                throw new ApiException("Invalid response structure: missing content field");
            }

            String content = contentNode.asText().strip();

            int startIndex = content.indexOf('{');
            int endIndex = content.lastIndexOf('}');

            if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
                LOGGER.log(Level.WARNING, "Invalid JSON found in response");
                throw new InvalidResponseException("Invalid JSON received from API. Expected JSON object in response");
            }

            String extractedJson = content.substring(startIndex, endIndex + 1);
            LOGGER.log(Level.FINE, "JSON extracted successfully from response");
            return extractedJson;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error extracting JSON from response", e);
            throw new ApiException("Failed to extract JSON from API response: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a JSON-formatted string into a Map<String, String>.
     *
     * @param jsonString The JSON string representing user data.
     * @return A map with keys and values parsed from the JSON.
     * @throws ApiException If the JSON cannot be parsed.
     */
    private static Map<String, String> convertJsonToMap(String jsonString) throws ApiException {
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<>() {});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error converting JSON to map", e);
            throw new ApiException("Failed to parse JSON response: " + e.getMessage(), e);
        }
    }

    /**
     * Populates the GlobalRegisterData class with values from the provided data map.
     * Safely handles null or missing values.
     *
     * @param data A map containing user registration data (e.g., username, email, password).
     */
    public static void populateGlobalRegisterData(Map<String, String> data) {
        if (data == null) {
            LOGGER.log(Level.WARNING, "Attempting to populate with null data");
            return;
        }

        GlobalRegisterData.USERNAME = data.getOrDefault("username", "");
        GlobalRegisterData.EMAIL = data.getOrDefault("email", "");
        GlobalRegisterData.PASSWORD = data.getOrDefault("password", "");
        GlobalRegisterData.CONFIRM_PASSWORD = data.getOrDefault("confirmPassword", "");
        GlobalRegisterData.FIRSTNAME = data.getOrDefault("firstname", "");
        GlobalRegisterData.LASTNAME = data.getOrDefault("lastname", "");
        GlobalRegisterData.PHONE_NUMBER = data.getOrDefault("phoneNumber", "");
        GlobalRegisterData.COUNTRY = data.getOrDefault("country", "");
        GlobalRegisterData.CITY = data.getOrDefault("city", "");
        GlobalRegisterData.ADDRESS = data.getOrDefault("address", "");
        GlobalRegisterData.STATE = data.getOrDefault("state", "");
        GlobalRegisterData.POSTAL_CODE = data.getOrDefault("postalCode", "");

        LOGGER.log(Level.FINE, "GlobalRegisterData populated successfully");
    }
}

