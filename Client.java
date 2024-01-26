import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Benvenuto a Mastermind! Inserisci una sequenza di 4 colori tra: GIALLO, VERDE, BLU, VIOLA, ROSSO, ARANCIONE, AZZURRO");

            int attempts = 0;
            while (attempts < 10) {
                System.out.println("Tentativo " + (attempts + 1) + ":");
                String userGuess = scanner.nextLine();

                out.println(userGuess);

                String feedback = in.readLine();
                System.out.println("Feedback: " + feedback);

                if (feedback.equals("4 pirolini neri")) {
                    System.out.println("Congratulazioni! Hai indovinato la sequenza. Il gioco termina.");
                    break;
                }

                attempts++;
            }

            if (attempts == 10) {
                System.out.println("Hai esaurito i tentativi. Il gioco termina.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}