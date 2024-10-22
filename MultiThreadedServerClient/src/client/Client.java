/**
 * Name: Renfei Yu (Klaus)
 * SID: 1394392
 * Login Username: reyu
 * Date: 2023/8/25
 */

/**
 * this is the client class for the user backend
 */

import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.net.SocketException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Client  extends JFrame{
	
	// address and port
	private static String address;
	private static int port;

	private JTextField textInputField;
    private JTextField meaningInputField;
    private JTextArea resultArea;

	private static BufferedReader input;
	private static PrintWriter output;


	public void startClientGUI() {

        // Create components
        JLabel titleLabel = new JLabel("Dictionary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font to bold and larger size

        JLabel textLabel = new JLabel("Text:");
        JLabel meaningLabel = new JLabel("Meaning:");
        textInputField = new JTextField(20);
        meaningInputField = new JTextField(20);
        
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton queryButton = new JButton("Query");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        // Set layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridBagLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add components to panels
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        topPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(textLabel, gbc);
        gbc.gridx = 1; // Add textInputField in the second column
        topPanel.add(textInputField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; // Reset gridx to add meaningLabel in the first column
        topPanel.add(meaningLabel, gbc);
        gbc.gridx = 1; // Add meaningInputField in the second column
        topPanel.add(meaningInputField, gbc);

        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(queryButton);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action logic here
                String textInput = textInputField.getText();
                String meaningInput = meaningInputField.getText();
                
				if(textInput.equals("") || meaningInput.equals("") || textInput.isEmpty() || meaningInput.isEmpty()){
					resultArea.setText("");
                    resultArea.append("Error: Text and meaning area should not be empty");
                    
				}else{
                    resultArea.setText("");
					resultArea.append("Add: " + "text-"+ textInput + "   meaning-" + meaningInput + "\n");
					String request = "ADD" + ":" + textInput + ":" + meaningInput;
                    try{
                        output.println(request);
                        
                        String tempLine;
                        if ((tempLine = input.readLine()) != null) {
                            resultArea.append(tempLine);
                            
                        }

                    }catch(IOException eee){
                            System.out.println("This is an IO exception - response from server");
                    }
					
				}
                
            
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete action logic here
                String textInput = textInputField.getText();

                if(textInput.equals("") || textInput.isEmpty()){
					resultArea.setText("");
                    resultArea.append("Error: Text area should not be empty");
                    
				}else{
                    resultArea.setText("");
					resultArea.append("Delete: "+ "text-"+ textInput + "\n");
					String request = "DEL" + ":" + textInput;
                    try{
                        output.println(request);
                        
                        String tempLine;
                        if ((tempLine = input.readLine()) != null) {
                            resultArea.append(tempLine);
                            
                        }

                    }catch(IOException eee){
                            System.out.println("This is an IO exception - response from server");
                    }
					
				}
                
                
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // update action logic here
                String textInput = textInputField.getText();
                String meaningInput = meaningInputField.getText();
                
				if(textInput.equals("") || meaningInput.equals("") || textInput.isEmpty() || meaningInput.isEmpty()){
					resultArea.setText("");
                    resultArea.append("Error: Text and meaning area should not be empty");
                    
				}else{
                    resultArea.setText("");
					resultArea.append("Update: " + "text-"+ textInput + "   meaning-" + meaningInput + "\n");
					String request = "UPD" + ":" + textInput + ":" + meaningInput;
                    try{
                        output.println(request);
                        
                        String tempLine;
                        if ((tempLine = input.readLine()) != null) {
                            resultArea.append(tempLine);
                            
                        }

                    }catch(IOException eee){
                            System.out.println("This is an IO exception - response from server");
                    }
					
				}
                
            
            }
        });

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Query action logic here
                String textInput = textInputField.getText();

                if(textInput.equals("") || textInput.isEmpty()){
					resultArea.setText("");
                    resultArea.append("Error: Text area should not be empty");
                    
				}else{
                    resultArea.setText("");
					resultArea.append("Query: "+ "text-"+ textInput + "\n");
					String request = "QUE" + ":" + textInput;
                    try{
                        output.println(request);
                        
                        String tempLine;
                        if ((tempLine = input.readLine()) != null) {
                            resultArea.append(tempLine);
                            
                        }

                    }catch(IOException eee){
                            System.out.println("This is an IO exception - response from server");
                    }
					
				}
                
                
            }
        });
        
        

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
	
	public static void main(String[] args){
		try{
			address=args[0];
			port=Integer.parseInt(args[1]);
		}catch (Exception e){
			System.out.println("You should input the <server-address> followed by the server <port number>");
		}


		try{
            Socket socket = new Socket(address, port);
			// Output and Input Stream
			input = new BufferedReader(new InputStreamReader (socket.getInputStream()));
		    output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			/**
			 * create a thread for GUI
			 */
			Client tempClient = new Client();
			tempClient.startClientGUI();
		    
		}catch(SocketException e){
			System.out.println("The server is not ready");
			System.exit(0);
		}
		catch (UnknownHostException e){
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

}
