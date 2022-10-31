import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int port = 2000;

    private ArrayList<ChatUser> users = new ArrayList<>();
    private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public ChatServer() {
    }

    public synchronized void addNewChatUser(String login, PrintWriter pw, BufferedReader br) {
        ChatUser user = new ChatUser(login, this, pw, br);
        executor.submit(user);
        this.users.add(user);
    }

    public synchronized void removeUser(ChatUser user) {
        System.out.println("Removing user!");
        user.shutdownUser();
        this.users.remove(user);
    }

    public synchronized void broadcast(String line) {
        System.out.println("num users: " + users.size());
        System.out.println("broadcast to " + users.size());
        for (ChatUser user : users) {
            if (line == null) {
                line = "hi";
            }
            try {
                user.send(line);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.removeUser(user);
            }
        }
    }

    public void listen() {
        try {
            ServerSocket s = new ServerSocket(port);
            System.out.println("[Server listening on port " + port + "]");
            while (true) {
                Socket socket = s.accept();
                LoginClient client = new LoginClient(socket, this);
                executor.submit(client);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer s = new ChatServer();
        s.listen();
    }
}
