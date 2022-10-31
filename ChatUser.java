import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChatUser implements Runnable {
    private final String login;
    private final ChatServer server;
    private final PrintWriter pw;
    private final BufferedReader br;

    public ChatUser(String login, ChatServer server, PrintWriter pw, BufferedReader br) {
        this.login = login;
        this.server = server;
        this.pw = pw;
        this.br = br;
    }

    public String getLogin() {
        return this.login;
    }

    public void shutdownUser() {
        try {
            this.pw.close();
            this.br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void send(String line) {
        if (line == null) {
            line = "hello";
        }
        this.pw.println(line + "\n");
        this.pw.flush();
    }

    public void run() {
        System.out.println("Welcome " + login);
        send("Welcome " + login);
        while (true) {
            try {
                String line = this.br.readLine();
                if (line == null) {
                    server.removeUser(this);
                    break;
                }
                line = login + ": " + line;
                System.out.println(line);
                server.broadcast(line);
            } catch (IOException e) {
                e.printStackTrace();
                server.removeUser(this);
                break;
            }
        }
    }
}
