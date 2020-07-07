import java.io.*;


// Repeatedly reads recipient's nickname and text from the user in two
// separate lines, sending them to the server (read by ServerReceiver
// thread).

public class ClientSender extends Thread {

  private String nickname;
  private PrintStream server;

  ClientSender(String nickname, PrintStream server) {
    this.nickname = nickname;
    this.server = server;
  }

  public void run() {
    // So that we can use the method readLine:
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    try {
      // Then loop forever sending messages to recipients via the server:
      while (true) {
        String recipient = user.readLine();
        String text = user.readLine();
        if(user.readLine().toLowerCase().equals("quit")) {
        		System.out.println("Quitting");	
        		break;
        }
        server.println(recipient); // Matches CCCCC in ServerReceiver
        server.println(text);      // Matches DDDDD in ServerReceiver
      }
      server.close();

    }
    catch (IOException e) {
      Report.errorAndGiveUp("Communication broke in ClientSender" 
                        + e.getMessage());
    }
  }
}
/*

What happens if recipient is null? Then, according to the Java
documentation, println will send the string "null" (not the same as
null!). So maye we should check for that case! Paticularly in
extensions of this system.

 */
