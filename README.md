Penny – Personal Expense Tracker

Project Overview

Penny is a Personal Expense Tracker developed using HTML, CSS, JavaScript, and Java. It helps users manage daily expenses, monitor monthly spending, and maintain records of their financial activities.

The application uses HTML, CSS, and JavaScript for the frontend and Java for backend processing and data storage.

Objectives

- Manage daily expenses.
- Monitor monthly spending.
- Organize expenses into categories.
- Set monthly budgets.
- Generate expense reports.
- Provide a user-friendly interface.

Features

1. Dashboard

- Displays monthly spending.
- Shows budget information.
- Displays recent expenses.

2. Expense Management

- Add expenses.
- Edit expenses.
- Delete expenses.
- Undo deleted expenses.

3. Automatic Category Suggestion

- Suggests categories based on expense descriptions.

4. Search and Filter

- Search expense records.
- Filter expenses.

5. Budget Management

- Set monthly budgets.
- Set category-wise budgets.
- Receive budget alerts.

6. Reports

- Daily reports.
- Weekly reports.
- Monthly reports.
- Category-wise expense analysis.

7. Currency Selection

- Indian Rupee (₹)
- US Dollar ($)
- Euro (€)
- British Pound (£)

Technologies Used

Technology| Purpose
HTML| Frontend structure
CSS| Styling
JavaScript| Frontend functionality
Java| Backend development
Maven| Project management
Gson| JSON processing

Penny_Expense_Tracker/
│
├── LICENSE
├── README.md
├── pom.xml
│
└── src/
    └── main/
        ├── java/
        │   └── PennyServer.java
        │
        └── resources/
            └── static/
                └── index.html

Requirements

- JDK 17 or later
- Apache Maven
- Modern web browser

How to Run

1. Build the project

mvn clean package

2. Start the Java server

mvn exec:java -Dexec.mainClass=PennyServer

3. Open the application

Visit:

http://localhost:8080

Backend Working

The Java backend uses an HTTP server to communicate with the frontend.

Main API endpoints:

- "GET /api/state" – Retrieves application data.
- "PUT /api/state" – Updates application data.

Gson is used for JSON processing. Application data is stored locally in "penny-data.json".

Future Enhancements

- User login and authentication.
- MySQL database integration.
- PDF and Excel report export.
- Cloud data backup.
- Advanced financial analysis.

License

This project is licensed under the MIT License. See the LICENSE file for details.

Author

SANTHOSH B-02

Java Programming Mini Project
