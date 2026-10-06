# HiTech

Desktop JavaFX application for managing clients and their orders, backed by a MySQL database.

## Features

- Client login by email
- Client creation (Personne Physique / Personne Morale)
- Clients table with filtering
- Orders management (create, deliver, cancel)
- Order status filtering and detailed order lines
- Automatic database table initialization at application startup

## Tech Stack

- Java 21
- JavaFX 21
- Maven
- MySQL

## Project Structure

- `/src/main/java/tn/hitech/hitech` → JavaFX application/controllers
- `/src/main/java/tn/hitech/Database` → repository/data access layer
- `/src/main/java/tn/hitech/Database/config` → database connection and initialization (`DbConnection`, `DbInitialiser`)
- `/src/main/resources/tn/hitech/hitech` → FXML views
- `/screenshots` → UI screenshots

## Prerequisites

Before running:

1. Install **JDK 21**
2. Install **Maven** (or use `./mvnw`)
3. Install and run **MySQL**
4. Create database:

```sql
CREATE DATABASE hitech;
```

5. Update DB credentials if needed in:
`/src/main/java/tn/hitech/Database/config/DbConnection.java`

Default values in the project:
- URL: `jdbc:mysql://localhost:3306/hitech`
- USER: `root`
- PASSWORD: `root`

## Database Initialization (important)

The app environment requires database tables.  
`DbInitialiser.initialise()` is called automatically when the JavaFX app starts (`HiTechApplication`), so tables are created if they do not exist.

You can also run the initializer directly:

```bash
./mvnw -q -DskipTests exec:java -Dexec.mainClass=tn.hitech.Database.config.DbInitialiser
```

> Note: `DbInitialiser.main()` drops and recreates all tables before initializing.

## Run the Application

From the repository root:

```bash
./mvnw clean javafx:run
```

On Windows:

```bash
mvnw.cmd clean javafx:run
```

## First Usage

1. Start the app
2. In login, enter an existing client email and click **entrer**
3. If no client exists, click **Nouveau client** and create one
4. Use **tout les clients** to view/manage clients list
5. Open orders screen to create, deliver, cancel, and inspect orders

## Screenshots

### Login
![Login](<screenshots/(1).png>)

### Clients Table
![Clients Table](<screenshots/(2).png>)

### Add Client
![Add Client](<screenshots/(3).png>)

### Orders
![Orders](<screenshots/(4).png>)

### Add Order
![Add Order](<screenshots/(5).png>)

## Build / Test

Run existing tests/build checks:

```bash
./mvnw test
```

## Notes

- The UI labels are mostly in French.
- Stock updates automatically when delivering/canceling orders.
