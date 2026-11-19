# Hybrid Java Test Automation Framework (UI & API)

A scalable, maintainable, CI/CD-ready hybrid test automation framework built with **Java**, **Selenium WebDriver 4**, **REST Assured**, and **JUnit 5**, following the **Page Object Model (POM)**, **Service Object**, and **Data Factory** design patterns.

---

## Architecture & Project Structure

The project enforces strict separation of concerns between **Web UI** (`ui`) and **REST API** (`api`) layers, allowing fast, headless API execution without launching WebDriver instances:

```text
selenium-cbtw-framework/
├── src/
│   ├── main/java/com/cbtw/automation/
│   │   ├── api/
│   │   │   ├── clients/       # Service / API Client Objects (BookingClient)
│   │   │   ├── factories/     # Dynamic API payload generators (DataFaker)
│   │   │   └── models/        # Immutable Java Record DTOs (BookingRequest)
│   │   ├── ui/
│   │   │   ├── base/          # BasePage with reusable Selenium wrappers & explicit waits
│   │   │   ├── factories/     # UI Data Factory classes (DataFaker) for dynamic users
│   │   │   └── pages/         # Fluent Page Objects encapsulating locators and actions
│   │   └── utils/             # ConfigReader for properties and CLI overrides
│   └── test/
│       ├── java/com/cbtw/automation/
│       │   ├── api/
│       │   │   └── tests/     # JUnit 5 API test suites (@Tag("API"))
│       │   └── ui/
│       │       ├── hooks/     # BaseTest WebDriver lifecycle hooks (@BeforeEach / @AfterEach)
│       │       └── tests/     # JUnit 5 UI E2E & Smoke test suites (@Tag("E2E"))
│       └── resources/
│           └── config.properties  # Centralized environment and execution settings
└── pom.xml                    # Maven dependencies, Jackson, and Surefire tag filtering
```

---

## Environment Setup & Installation (Windows, macOS & Linux)

To run this framework from the terminal, you need **JDK 21+**, **Apache Maven 3.8+**, and a supported browser (**Google Chrome** or **Microsoft Edge**).

### 1. Browsers & WebDriver Management
- **Supported Browsers:** Google Chrome (default) or Microsoft Edge must be installed on your machine (only required for UI tests; API tests run purely over HTTP).
- **Automatic Driver Resolution:** No manual `chromedriver.exe` or `msedgedriver.exe` setup is required. Selenium 4 includes **Selenium Manager**, which automatically detects your local browser version and downloads/caches the matching driver binary on the first run.
- **Headless Execution (CI/CD / Linux Servers):** When running with `-Dheadless=true`, Chrome/Edge runs without a graphical UI, making it compatible with Docker containers and headless Linux runners (ensure `google-chrome-stable` or `microsoft-edge-stable` is installed on the runner).

### 2. Windows Setup (PowerShell)

**Install Maven via PowerShell (No Admin required):**
```powershell
# Download and extract Apache Maven 3.9.9
New-Item -ItemType Directory -Force -Path "$env:USERPROFILE\Apache"
Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip" -OutFile "$env:TEMP\maven.zip"
Expand-Archive -Path "$env:TEMP\maven.zip" -DestinationPath "$env:USERPROFILE\Apache" -Force

# Add Maven to User PATH permanently
$mavenBin = "$env:USERPROFILE\Apache\apache-maven-3.9.9\bin"
[Environment]::SetEnvironmentVariable("Path", "$([Environment]::GetEnvironmentVariable('Path', 'User'));$mavenBin", "User")
$env:Path += ";$mavenBin"
```
*(Restart your terminal after running the script and verify with `java -version` and `mvn -v`).*

### 3. macOS Setup (Homebrew)

```bash
# Install Homebrew (if not installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install OpenJDK, Maven, and Google Chrome
brew install openjdk@21 maven
brew install --cask google-chrome

# Link OpenJDK so system wrappers can find it
sudo ln -sfn $(brew --prefix)/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
```

### 4. Linux Setup (Ubuntu / Debian)

```bash
# Install JDK and Maven
sudo apt update
sudo apt install -y openjdk-21-jdk maven wget

# Install Google Chrome (required for local or headless UI runs)
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt install -y ./google-chrome-stable_current_amd64.deb
rm google-chrome-stable_current_amd64.deb
```

### 5. Verify Installation & Build Project
Run these commands on any OS to confirm the environment is ready:
```bash
java -version
mvn -v
git clone https://github.com/jvergara9208/selenium-cbtw-framework.git
cd selenium-cbtw-framework
mvn clean test-compile
```

---

## Configuration (`config.properties`)

Default execution parameters are centralized in `src/test/resources/config.properties`:

| Property | Default Value | Description |
| :--- | :--- | :--- |
| `base.url` | `https://www.saucedemo.com/` | Target Web UI application URL |
| `api.base.url` | `https://restful-booker.herokuapp.com` | Target REST API base URI |
| `browser` | `chrome` | Target browser (`chrome` or `edge`) |
| `headless` | `false` | Runs browser in headless mode when set to `true` |
| `timeout.seconds` | `10` | Explicit wait timeout in seconds |

> **CLI Override Priority:** Any property in `config.properties` can be overridden at runtime via Maven `-D` system properties without modifying source code (ideal for CI/CD pipelines).

---

## Running Tests

### 1. From Terminal (Maven CLI & JUnit 5 `@Tag` Filtering)

- **Run the entire suite (UI + API):**
  ```bash
  mvn test
  ```

- **Run ONLY API tests (fast execution, no browser launched):**
  - **Windows PowerShell:**
    ```powershell
    mvn test "-Dgroups=API"
    ```
  - **macOS / Linux / Git Bash:**
    ```bash
    mvn test -Dgroups=API
    ```

- **Run ONLY UI E2E tests:**
  - **Windows PowerShell:**
    ```powershell
    mvn test "-Dgroups=E2E & !API"
    ```
  - **macOS / Linux / Git Bash:**
    ```bash
    mvn test -Dgroups="E2E & !API"
    ```

- **Run Smoke tests across both UI and API in Headless mode (CI/CD pipeline simulation):**
  - **Windows PowerShell:**
    ```powershell
    mvn test "-Dgroups=Smoke" "-Dheadless=true"
    ```
  - **macOS / Linux / Git Bash:**
    ```bash
    mvn test -Dgroups=Smoke -Dheadless=true
    ```

- **Run UI tests on Microsoft Edge:**
  ```powershell
  mvn test "-Dgroups=E2E & !API" "-Dbrowser=edge"
  ```

- **Run a single test class or specific test method:**
  ```powershell
  mvn test "-Dtest=BookingApiTest"
  mvn test "-Dtest=LoginTest#testSuccessfulLogin"
  ```

### 2. From IDE (IntelliJ IDEA)
- Open `src/test/java/com/cbtw/automation/ui/tests/LoginTest.java` or `src/test/java/com/cbtw/automation/api/tests/BookingApiTest.java`.
- Click the green **Run (`▶`)** gutter icon next to the class declaration to run the full suite, or next to an individual `@Test` method to run/debug a single scenario.

---

## Troubleshooting Common Maven, Selenium & API Issues

| Error / Issue | Cause | Solution |
| :--- | :--- | :--- |
| `'mvn' is not recognized` / `command not found: mvn` | Maven `bin` folder is not in the system `PATH`, or the terminal was not restarted after installation. | Verify `PATH` includes `.../apache-maven-x.x.x/bin` and **restart your terminal/IDE**. Alternatively, use `./mvnw test` (`.\mvnw test` on Windows). |
| `JAVA_HOME is not defined correctly` / `Unsupported class file major version` | `JAVA_HOME` points to an older JDK (e.g., Java 8/11) or is missing. | Set `JAVA_HOME` to your JDK 21+ directory and restart your terminal. |
| `Unknown lifecycle phase ".headless=true"` (Windows PowerShell) | PowerShell splits `-Dproperty=value` arguments at the dot (`.`) if unquoted. | Always wrap `-D` parameters in double quotes on PowerShell: `mvn test "-Dheadless=true"`. |
| `Tests run: 0` when running `mvn test` | Default `<groups>` in `pom.xml` has a non-empty placeholder value or Surefire is outdated. | Ensure `<groups></groups>` is empty by default in `pom.xml` and `maven-surefire-plugin` `3.5.2` is configured. |
| HTTP `418 I'm a teapot` on Restful-Booker API | REST Assured appends a default charset (`; charset=UTF-8`) to `Content-Type` or sends wildcard `Accept` headers rejected by the API. | Handled in `BookingClient` via `EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)` and explicit `Accept: application/json`. |
| `SessionNotCreatedException` / Browser driver mismatch | Local Chrome/Edge is not installed or blocked by network/proxy during driver download. | Verify Chrome or Edge is installed locally and ensure internet access is enabled on the first run for **Selenium Manager**. |