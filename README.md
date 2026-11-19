# Selenium Java Automation Framework

A scalable, maintainable UI test automation framework built with **Java**, **Selenium WebDriver**, and **JUnit 5**, following the **Page Object Model (POM)** and **Data Factory** design patterns.

---

## Architecture & Project Structure

```text
selenium-cbtw-framework/
├── src/
│   ├── main/java/com/cbtw/automation/
│   │   ├── base/          # BasePage with reusable Selenium wrappers & explicit waits
│   │   ├── factories/     # Data Factory classes (DataFaker) for dynamic test data
│   │   ├── pages/         # Page Objects encapsulating locators and page actions
│   │   └── utils/         # ConfigReader for properties and CLI overrides
│   └── test/
│       ├── java/com/cbtw/automation/
│       │   ├── hooks/     # BaseTest lifecycle hooks (@BeforeEach / @AfterEach)
│       │   └── tests/     # JUnit 5 test suites focused on business assertions
│       └── resources/
│           └── config.properties  # Centralized environment and execution settings
└── pom.xml                # Maven dependencies and build plugins
```

---

## Environment Setup & Installation (Windows, macOS & Linux)

To run this framework from the terminal, you need **JDK 17+**, **Apache Maven 3.8+**, and a supported browser (**Google Chrome** or **Microsoft Edge**).

### 1. Browsers & WebDriver Management
* **Supported Browsers:** Google Chrome (default) or Microsoft Edge must be installed on your machine.
* **Automatic Driver Resolution:** No manual `chromedriver.exe` or `msedgedriver.exe` setup is required. Selenium 4 includes **Selenium Manager**, which automatically detects your local browser version and downloads/caches the matching driver binary on the first run.
* **Headless Execution (CI/CD / Linux Servers):** When running with `-Dheadless=true`, Chrome/Edge runs without a graphical UI, making it compatible with Docker containers and headless Linux runners (ensure `google-chrome-stable` or `microsoft-edge-stable` is installed on the runner).

### 2. Windows Setup (PowerShell)

**Install Maven via PowerShell (No Admin required):**
```powershell
# Download and extract Apache Maven 3.9.9
New-Item -ItemType Directory -Force -Path "$env:USERPROFILE\Apache"
Invoke-WebRequest -Uri "[https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip](https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip)" -OutFile "$env:TEMP\maven.zip"
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
/bin/bash -c "$(curl -fsSL [https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh](https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh))"

# Install OpenJDK 17, Maven, and Google Chrome
brew install openjdk@17 maven
brew install --cask google-chrome

# Link OpenJDK so system wrappers can find it
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

### 4. Linux Setup (Ubuntu / Debian)

```bash
# Install JDK 17 and Maven
sudo apt update
sudo apt install -y openjdk-17-jdk maven wget

# Install Google Chrome (required for local or headless runs)
wget -q [https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb](https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb)
sudo apt install -y ./google-chrome-stable_current_amd64.deb
rm google-chrome-stable_current_amd64.deb
```

### 5. Verify Installation & Build Project
Run these commands on any OS to confirm the environment is ready:
```bash
java -version
mvn -v
git clone <YOUR_REPOSITORY_URL>
cd selenium-cbtw-framework
mvn clean test-compile
```

---

## Configuration (`config.properties`)

Default execution parameters are centralized in `src/test/resources/config.properties`:

| Property | Default Value | Description |
| :--- | :--- | :--- |
| `base.url` | `https://www.saucedemo.com/` | Target application URL |
| `browser` | `chrome` | Target browser (`chrome` or `edge`) |
| `headless` | `false` | Runs browser in headless mode when set to `true` |
| `timeout.seconds` | `10` | Explicit wait timeout in seconds |

> **CLI Override Priority:** Any property in `config.properties` can be overridden at runtime via Maven `-D` system properties without modifying source code (ideal for CI/CD pipelines).

---

## Running Tests

### 1. From Terminal (Maven CLI)

* **Run all tests using default `config.properties` settings:**
  ```bash
  mvn test
  ```

* **Run in Headless mode (CI/CD execution):**
  * **macOS / Linux / Git Bash:**
    ```bash
    mvn test -Dheadless=true
    ```
  * **Windows PowerShell:** *(wrap `-D` flags in quotes)*
    ```powershell
    mvn test "-Dheadless=true"
    ```

* **Run on a different browser (e.g., Microsoft Edge):**
  ```bash
  mvn test -Dbrowser=edge
  ```

* **Run against a different environment URL:**
  ```bash
  mvn test -Dbase.url=[https://www.saucedemo.com/](https://www.saucedemo.com/) -Dheadless=true
  ```

* **Run a single test class or specific test method:**
  ```bash
  mvn test -Dtest=LoginTest
  mvn test -Dtest=LoginTest#testSuccessfulLogin
  ```

### 2. From IDE (IntelliJ IDEA / VS Code)
* Open `src/test/java/com/cbtw/automation/tests/LoginTest.java`.
* Click the green **Run (`▶`)** gutter icon next to the class declaration to run the full suite, or next to an individual `@Test` method to run/debug a single scenario.

---

## Troubleshooting Common Maven & Selenium Issues

| Error / Issue | Cause | Solution |
| :--- | :--- | :--- |
| `'mvn' is not recognized` / `command not found: mvn` | Maven `bin` folder is not in the system `PATH`, or the terminal was not restarted after installation. | Verify `PATH` includes `.../apache-maven-x.x.x/bin` and **restart your terminal/IDE**. Alternatively, generate the Maven Wrapper (`mvn wrapper:wrapper`) and use `./mvnw test` (`.\mvnw test` on Windows). |
| `JAVA_HOME is not defined correctly` / `Unsupported class file major version` | `JAVA_HOME` points to an older JDK (e.g., Java 8/11) or is missing. | Set `JAVA_HOME` to your JDK 17+ directory (`export JAVA_HOME=$(/usr/libexec/java_home -v 17)` on macOS, or `$env:JAVA_HOME="C:\Program Files\Java\jdk-17"` on Windows). |
| `Unknown lifecycle phase ".headless=true"` (Windows PowerShell) | PowerShell splits `-Dproperty=value` arguments at the dot (`.`) if unquoted. | Always wrap `-D` parameters in double quotes on PowerShell: `mvn test "-Dheadless=true"`. |
| `Tests run: 0` when running `mvn test` | `maven-surefire-plugin` version is outdated and does not detect JUnit 5 (`jupiter`) tests. | Ensure `maven-surefire-plugin` version `3.5.2` (or `3.0.0+`) is configured in `pom.xml`. |
| `SessionNotCreatedException` / Browser driver mismatch | Local Chrome/Edge is not installed or blocked by network/proxy during driver download. | Verify Chrome or Edge is installed locally, ensure internet access is enabled on the first run for **Selenium Manager**, or update `selenium-java` in `pom.xml`. |