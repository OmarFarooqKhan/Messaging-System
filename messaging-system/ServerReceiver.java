import java.net.*;
import java.io.*;
import java.util.concurrent.*;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
  private String myClientsName;
  private BufferedReader myClient;
  private ClientTable clientTable;

  public ServerReceiver(String n, BufferedReader c, ClientTable t) {
    myClientsName = n;
    myClient = c;
    clientTable = t;
  }

  public void run() {
    try {
      while (true) {
        String recipient = myClient.readLine(); // Matches CCCCC in ClientSender.java
        String text = myClient.readLine();      // Matches DDDDD in ClientSender.java
        if (recipient != null && text != null) {
          Message msg = new Message(myClientsName, text);
          
          BlockingQueue<Message> recipientsQueue
            = clientTable.getQueue(recipient); // Matches EEEEE in ServerSender.java
          if (recipientsQueue != null)
            recipientsQueue.offer(msg);
          if (text.toLowerCase().equals("Quit")) {
          	System.out.println("Quitting");

  			break;
  		}
          
          else
            Report.error("Message for unexistent client "
                         + recipient + ": " + text);
        }
        else
          // No point in closing socket. Just give up.
          return;
        }
      myClient.close();
      }
    catch (IOException e) {
      Report.error("Something went wrong with the client " 
                   + myClientsName + " " + e.getMessage()); 
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
    }
  }
}

