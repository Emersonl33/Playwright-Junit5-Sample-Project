# 🤖 Automated Testing Project with Playwright, JUnit 5, MongoDB, and OpenRouter API

---

# 📌 Overview

This repository contains an automated testing framework built with:

- Java 21+
- Playwright framework 
- JUnit 5 to run tests  
- MongoDB (local) to store user data created during tests  
- A sample AI prompt with OpenRouter API to generate or analyze test data  
- OpenRouter API for AI data processing  
- Allure Reports for test reporting  
- Page Object pattern to organize test code  
- Environment variables to store API keys  

The framework supports both UI and API testing.

# 📌 Requirements
## 🔐 Set `OPENROUTER_API_KEY` Environment Variable in IntelliJ IDEA

### Step 1: Get Your API Key

1. Go to [https://openrouter.ai](https://openrouter.ai)  
2. Log in to your account  
3. Click on the user icon and navigate to the **Keys** section  
4. Locate your API key  
5. Copy the API key for later use

### Step 2: Configure Environment Variable in IntelliJ IDEA

1. Open IntelliJ IDEA  
2. Go to `Run > Edit Configurations`  
3. Select your test run configuration (JUnit, Application, etc.)  
4. Click **Environment variables** then the `...` button  
5. Add the variable like this:
```bash
OPENROUTER_API_KEY=as5d16a5s1d6a5s1d65as1d65as1d65a1d6
````
# 🍃 MongoDB Local Setup Tutorial

- It is necessary to have MongoDB installed to run the project with a local connection established.  
- The project uses a collection named `credentialsTests`, so make sure this collection is created in your database.

# 🚀 How to Run Tests
### Using Maven:
```bash
mvn clean test
````
## To access the report generated with allure
### Using Maven:
```bash
mvn allure:serve
````

