# Personal Dictionary📚
This Java-based dictionary project is your go-to solution for managing words and definitions with style and efficiency.

## System Design
- **Client-Server Model** <br>
  The system is divided into a client and a server. The client interface allows users to interact with the dictionary, while the server handles data storage and management.
- **Thread Management & Concurrency Control** <br>
  Each client connection is assigned its own thread. This approach allows the server to process multiple requests concurrently without causing delays or bottlenecks.

## 💡Getting Started
Make sure you’ve got JDK 21.0.3 installed. Then, follow these steps:
1. Download the following files and place them into a folder
   - `DictionaryClient.jar`
   - `DictionaryServer.jar`
   - `dictionary.txt` (optional)
2. Navigate to the correct directory
   ```
   cd /path/to/folder
   ```
3. Start the server:
   ```
   java -jar server/DictionaryServer.jar
   ```

4. Start the client:
   ```
   java -jar client/DictionaryClient.jar
   ```

## 🖼️ See It in Action

**Server**

Experience the sever management:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/server.png" alt="2 clients" width="300"/>

Stay informed with real-time server activity reports in terminal:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ServerReport.png" alt="Server report" width="300"/>

**Client**

Experience the intuitive client login menu:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ClientLogin.png" alt="Client login menu" width="300"/>

**Multiple Clients**

Open several clients and manage words simultanelously:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ServalClient.png" alt="2 clients" width="800"/>



## 🌟 Dive in and start your word adventure today! 📚🚀
If you have any questions or need help, feel free to reach out. Happy word exploring!👋
