# 🕵️‍♂️ Your Personal Dictionary App! 📚
Welcome to the your custom Dictionary App! This Java-based dictionary project is your go-to solution for managing words and definitions with style and efficiency.

## 🌈 When You'll Love It
- 🤯 Language Learning: Perfect for students or language enthusiasts looking to expand their vocabulary and keep track of new words.
- 🗂️ Personal Knowledge Base: Build your own reference database of words, terms, and their meanings for quick access and review.

## System Design
- **Client-Server Model**: The system is divided into a client and a server. The client interface allows users to interact with the dictionary, while the server handles data storage and management.
- **Server Design**

- **Client Design**

## Multi-threaded Server
Our multi-threaded server architecture is designed to handle multiple client requests efficiently. Here’s how it works:
- Thread Management: Each client connection is assigned its own thread. This approach allows the server to process multiple requests concurrently without causing delays or bottlenecks.
- Concurrency Control: By using threads, the server can manage concurrent read and write operations to the dictionary data. This ensures that updates from one client do not interfere with the operations of others.
- Scalability: The multi-threaded design makes it easy to scale the server to handle more clients as needed. This is crucial for maintaining performance as the number of users grows.

## 💡Getting Started
Make sure you’ve got JDK 21.0.3 installed. Then, follow these steps:
1. Navigate to the correct directory:
   ```bash
   cd /path/to/dictionary
   ```
2. Start the server:
   ```bash
   java -jar server/DictionaryServer.jar <port>
   ```
3. Start the client:
   ```bash
   java --module-path client/lib --add-modules javafx.controls,javafx.fxml -jar client/DictionaryClient.jar
   ```

## 🖼️ See It in Action

**Client Login Menu**

Experience the intuitive client login menu:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ClientLogin.png" alt="Client login menu" width="300"/>

**Multiple Clients**

Open several clients and manage words simultanelously:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ServalClient.png" alt="2 clients" width="800"/>

**Server Activity Report**

Stay informed with real-time server activity reports:

<img src="https://github.com/verazi/Multithreaded-Dictionary-Server/blob/master/zreport/ServerReport.png" alt="Server report" width="300"/>



## 🌟📚🚀 Dive in and start your word adventure today!
If you have any questions or need help, feel free to reach out. Happy word exploring!👋
