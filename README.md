# Horse Racing Game – Java Client-Server Multiplayer Application

## Overview

This is a real-time multiplayer Horse Racing Game built in **Java**, utilizing a **client-server architecture** over **HTTP**. The game supports **2 to 4 players**, allowing them to connect remotely and participate in turn-based gameplay. Communication is handled via **short polling**, where clients fetch updates from the server every two seconds to keep the game state synchronized.

## Features

### Gameplay

* ✨ **Turn-Based Horse Racing** on an 8x8 game board
* ⚲ **Dice Rolling Mechanics** to determine movement
* ⚠️ **Special Fields** such as obstacles and bonuses
* 🔹 **Current Player Display** for clarity and flow
* 👥 **Supports 2 to 4 Players** in a multiplayer environment

### Technical Highlights

* ☑️ **Java-based Application**
* 🚚 **Client-Server Communication via HTTP**
* ⏳ **Short Polling** (requests every 2 seconds for state updates)
* 🔄 **MVC Design Pattern** for separation of concerns
* 🔠 **Scalable and Modular Code Structure**

## Architecture

```text
+------------+         HTTP Request        +------------+
|  Client 1  |  <----------------------->  |            |
|            |                            |            |
+------------+                            |            |
                                          |   Server   |
+------------+         HTTP Request       |            |
|  Client 2  |  <----------------------->  |            |
+------------+                            +------------+
```

* Clients regularly send HTTP requests (polling) to fetch the latest game state
* The server processes moves, manages game logic, and broadcasts updates

## Setup & Installation

### Requirements

* Java 11 or above
* IDE (e.g., IntelliJ, Eclipse) or command-line setup

### Steps

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/horse-racing-game.git
   ```
2. **Build the Project:**

   * Using your IDE or

   ```bash
   javac -d bin src/*.java
   ```
3. **Run the Server:**

   ```bash
   java -cp bin Server
   ```
4. **Run Each Client:**

   ```bash
   java -cp bin Client
   ```

## Screenshots (Optional)

*Include UI screenshots here if available.*

## Future Improvements

*

## License

## Author

\[Youssif Eldemellawi] – \[[youssifeldemellawi@gmail.com](mailto:youssifeldemellawi@gmail.com)]
[https://github.com/youssifeldemellawiirofile](https://github.com/yourusername)
