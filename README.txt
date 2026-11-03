# Penny – HTML frontend + Java backend

This project keeps the Penny HTML/CSS interface and uses Java for its backend.

## Requirements
- JDK 17 or newer
- Apache Maven

## Run on Windows
1. Extract the ZIP file.
2. Open Command Prompt inside the extracted `Penny_Java_Web_App` folder.
3. Run: `mvn clean package`
4. Run: `mvn exec:java -Dexec.mainClass=PennyServer`

If the `exec` goal is unavailable in your Maven setup, use:
`mvn org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=PennyServer`

5. Open `http://localhost:8080` in Chrome or another browser.

## How it works
- `src/main/resources/static/index.html` contains the original-style HTML, CSS and browser-side interactions.
- `src/main/java/PennyServer.java` is the Java backend.
- `GET /api/state` loads saved expenses and budgets.
- `PUT /api/state` saves changes.
- `penny-data.json` is created in the folder where the server is started.

## Important
Keep the Java server running while using the website. Do not open `index.html` by double-clicking it; open `http://localhost:8080` instead.
The app is intended to run on the same computer as the Java server. To access it from another device on the same Wi-Fi, keep the Java server running on the computer, allow it through the computer firewall if prompted, and open `http://COMPUTER-IP:8080` on the other device. `localhost` on a phone refers to the phone itself, not the computer.
