import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Benvenuto a Mastermind! Inserisci una sequenza di 4 colori tra: GIALLO, VERDE, BLU, VIOLA, ROSSO, ARANCIONE, AZZURRO");

            int attempts = 0;
            while (attempts < 10) {
                System.out.println("Tentativo " + (attempts + 1) + ":");
                String userGuess = stdIn.readLine();

                out.writeUTF(userGuess); // Use writeUTF instead of writeBytes
                out.flush(); // Flush the output stream to send the data

                String feedback = in.readUTF(); // Use readUTF instead of readLine
                System.out.println("Feedback: " + feedback + " attempts:" + attempts);

                if (feedback.equals("4 pirolini neri, 0 pirolini bianchi")) {
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