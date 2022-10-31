import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginClient implements Runnable {
    private final Socket socket;
    private final ChatServer server;

    public LoginClient(Socket s, ChatServer server) {
        this.socket = s;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // pw.print("Login: ");
            // pw.flush();
            String login = br.readLine();
            System.out.println("login from " + login);
            // user is logged in - add them to the server.
            server.addNewChatUser(login, pw, br);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
