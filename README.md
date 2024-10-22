# DistributedSystem_TCP_MultiThreaded_Dictionary
This is a distributed system project based using TCP protocol which unables users to set a server for different clients in order to modify and using dictionary in a shared platform.

# Run the code 
## Method 1
1. Terminal 1: java -jar DictionaryServer.jar portnumber NetTextFileName
2. Terminal 2-n: java -jar DictionaryClient.jar localhost portnumber


## Method 2
1. Terminal 1: cd into server folder
2. Terminal 2-n: cd into client folder
3. Terminal 1: javac Server.java
4. Terminal 1: java Server portnumber NewTextFileName
5. Terminal 2-n: javac Client.java
6. Terminal 2-n: java Client localhost portnumber