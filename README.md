# Run Java Tests with Selenium on TestMu AI (Formerly LambdaTest)

<p align="center">
  <a href="https://www.testmuai.com/"><img src="https://img.shields.io/badge/MADE%20BY%20TestMu%20AI-000000.svg?style=for-the-badge&labelColor=000" alt="Made by TestMu AI"></a>
  <a href="https://central.sonatype.com/artifact/org.seleniumhq.selenium/selenium-java"><img src="https://img.shields.io/maven-central/v/org.seleniumhq.selenium/selenium-java.svg?style=for-the-badge&labelColor=000000" alt="Selenium version"></a>
  <a href="https://community.testmuai.com/"><img src="https://img.shields.io/badge/Join%20the%20community-blueviolet.svg?style=for-the-badge&labelColor=000000" alt="Community"></a>
</p>

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Maven: install from [maven.apache.org](https://maven.apache.org/) or via Homebrew
- A [TestMu AI](https://www.testmuai.com/) account with your username and access key

### Setup

Clone and install dependencies:

```bash
git clone https://github.com/abdul2605/lamdatestSelenium.git && cd lamdatestSelenium
```

Set your credentials as environment variables.

**macOS / Linux:**

```bash
export LT_USERNAME="YOUR_USERNAME"
export LT_ACCESS_KEY="YOUR_ACCESS_KEY"
```

**Windows:**

```bash
set LT_USERNAME="YOUR_USERNAME"
set LT_ACCESS_KEY="YOUR_ACCESS_KEY"
```

### Run tests

```bash
mvn clean install exec:java -Dexec.mainClass="com.lambdatest.BasicAuthentication" -Dexec.classpathScope=test -e
```

View results on your TestMu AI dashboard.

---

## Test Scenarios

### Test Scenario 1 — Simple Form Demo (`TestMuSimpleFormTest.java`)

1. Open [Selenium Playground](https://www.testmuai.com/selenium-playground/) and click **"Simple Form Demo"**.
2. Validate that the URL contains `simple-form-demo`.
3. Enter **"Welcome to TestMu AI"** in the **"Enter Message"** text box.
4. Click **"Get Checked Value"**.
5. Validate that the same text is displayed under the **"Your Message:"** section.

```bash
mvn clean install exec:java -Dexec.mainClass="com.lambdatest.TestMuSimpleFormTest" -Dexec.classpathScope=test
```

---

### Test Scenario 2 — Drag & Drop Sliders (`DragDropSliderTest.java`)

1. Open [Selenium Playground](https://www.testmuai.com/selenium-playground/) and click **"Drag & Drop Sliders"**.
2. Select the slider **"Default value 15"** and drag it to **95**.
3. Validate that the displayed range value shows **95**.

```bash
mvn clean install exec:java -Dexec.mainClass="com.lambdatest.DragDropSliderTest" -Dexec.classpathScope=test
```

---

### Test Scenario 3 — Input Form Submit (`TS3InputFormTest.java`)

1. Open [Selenium Playground](https://www.testmuai.com/selenium-playground/) and click **"Input Form Submit"**.
2. Click **"Submit"** without filling in any fields.
3. Assert the **"Please fill in this field."** HTML5 validation error on the Name field.
4. Fill in all required fields: Name, Email, Password, Company, Website, City, Address, State, Zip Code.
5. Select **"United States"** from the **Country** dropdown.
6. Click **"Submit"**.
7. Validate the success message: **"Thanks for contacting us, we will get back to you shortly."**

```bash
mvn clean install exec:java -Dexec.mainClass="com.lambdatest.TS3InputFormTest" -Dexec.classpathScope=test
```

---

## Dependency Notes

- Selenium upgraded to **4.44.0** with `selenium-devtools-v148` to match Chrome 148 on TestMu AI cloud.
- CDP imports updated from `v140` → `v148` across all relevant test files.

### Local testing with TestMu AI Tunnel

To test locally hosted apps, set up the TestMu AI tunnel. OS-specific guides:

- [Local Testing on Windows](https://www.testmuai.com/support/docs/local-testing-for-windows/)
- [Local Testing on macOS](https://www.testmuai.com/support/docs/local-testing-for-macos/)
- [Local Testing on Linux](https://www.testmuai.com/support/docs/local-testing-for-linux/)

Add the following to your capabilities:

```js
tunnel: true,
```
