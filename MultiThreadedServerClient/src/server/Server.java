/**
 * Name: Renfei Yu (Klaus)
 * SID: 1394392
 * Login Username: reyu
 * Date: 2023/8/25
 */

/**
 * this is the server class for the dictionary
 */


import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;
import javax.swing.RowFilter.Entry;
import javax.swing.*;
import java.awt.*;

public class Server extends JFrame {

	//use hashmap as a dictionary, concurrentHashMap will handle the concurrency on the dictionary
	private static ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<>();
	
	// the port number will be defined in the command line argument
	private static int port;
	private static String dictionaryFile;
	
	// Identifies the user number connected
	private static int counter = 0;
	private static ServerSocket server;

    private static JTextArea connectionTextArea;
    private static JTextArea clientActionsTextArea;
    private static JTextArea dictionaryTextArea;

	public static void main(String[] args){
		ServerSocket server = null;
		
		try{
			port=Integer.parseInt(args[0]);
			dictionaryFile=args[1];

		}catch (Exception e){
			System.out.println("You should input the <port number> followed by the <dictionary file name>");
			System.exit(0);
		}
		Server tempServer = new Server();
		tempServer.startServerGUI();

		ServerSocketFactory factory = ServerSocketFactory.getDefault();

		try{
			server = factory.createServerSocket(port);
			editConnectionPanel("ready for connection- \n");

		} catch (IOException e) {
			System.out.println("This is an IO exception -server");
		}

		//start the dictionary by reading the dictionary file
		startDictionary();
		
		

		/**
		 * Thread per connection
		 * Wait for connections
		 */
		while(true) {
			if(server != null){
				try{
					Socket client = server.accept();
					counter++;
					editConnectionPanel("Client "+counter+": Applying for connection!\n");

					ClientHandler serveClient = new ClientHandler(client, dictionary, counter);
					serveClient.start();
				}catch(IOException e){
					System.out.println("IO exception at this point - server");
				}
				
			}

		}

		
	}

	public static void startDictionary(){
		/**
		 * the dictiornay file will be stroed in a txt file
		 */
		try{
			File file = new File(dictionaryFile);
			if(!file.exists()){
				file.createNewFile();
			}
			
			/**
			 * before ending the server, we need to store the dictionary into the file to make it stored\
			 * next time we want to read the dictionary, we need to read from the file and stored in the hashMap
			 */

			// Input stream
		
			String tempLine;
			
			BufferedReader input = new BufferedReader(new FileReader(file));
			
			while ((tempLine = input.readLine()) != null) {
				/*
				* split each line in the file and store them in the dictionary
				*/
				String[] components = tempLine.split(":");
				dictionary.put(components[0],components[1]);
				
			}

			editDictionaryPanel();

			input.close();

			
		}catch (IOException e) {
			e.printStackTrace();
        }

	}

	public static ConcurrentHashMap<String, String> getDictionary(){
		return dictionary;
	}

	public static void modifyDictionary(){
		try{
			File file = new File(dictionaryFile);
			// Output Stream
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for (String key : dictionary.keySet()) {
                String value = dictionary.get(key);
				output.write(key+":"+value+"\n");
			}
			output.flush();
			output.close();
			
			
		}catch (IOException e) {
            System.out.println("Cannot wrtie into the file - modify dictionary");
        }


	}

	public static void updateWord(String text, String meaning){
		dictionary.put(text, meaning);

		try{
			modifyDictionary();
			editDictionaryPanel();

		}catch (Exception e){
			System.out.println("There is an exception here - AddWord");
		}

	}

	public static void deleteWord(String text) {
		dictionary.remove(text,dictionary.get(text));
		try{
			modifyDictionary();
			editDictionaryPanel();

		}catch (Exception e){
			System.out.println("There is an exception here - DeleteWord");
		}
	}


	public void startServerGUI(){

		setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Server");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        connectionTextArea = createNonEditableTextArea("Connection:");
        dictionaryTextArea = createNonEditableTextArea("Dictionary:");
        clientActionsTextArea = createNonEditableTextArea("Client Actions:");

        JScrollPane connectionScrollPane = new JScrollPane(connectionTextArea);
        JScrollPane dictionaryScrollPane = new JScrollPane(dictionaryTextArea);
        JScrollPane clientActionsScrollPane = new JScrollPane(clientActionsTextArea);

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(connectionScrollPane, BorderLayout.SOUTH);
        leftPanel.add(dictionaryScrollPane, BorderLayout.CENTER);

        JPanel textAreasPanel = new JPanel(new BorderLayout());
        textAreasPanel.add(leftPanel, BorderLayout.WEST);
        textAreasPanel.add(clientActionsScrollPane, BorderLayout.CENTER);

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(textAreasPanel, BorderLayout.CENTER);

        add(contentPanel);

        setSize(800, 600);
        setVisible(true);
	}

	public static void editConnectionPanel(String content){
		connectionTextArea.append(content);
	}

	public static void editActionsPanel(String content){
		clientActionsTextArea.append(content);
	}

	public static void editDictionaryPanel(){

		dictionaryTextArea.setText("");
		for (String key : dictionary.keySet()) {
			String value = dictionary.get(key);
			dictionaryTextArea.append(key+":"+value+"\n");
		}
	}
	

	private JTextArea createNonEditableTextArea(String title) {
        JTextArea textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createTitledBorder(title));
        return textArea;
    }

    private JTextArea createEditableTextArea(String title) {
        JTextArea textArea = new JTextArea(10, 20);
        textArea.setEditable(true);
        textArea.setBorder(BorderFactory.createTitledBorder(title));
        return textArea;
    }

}
