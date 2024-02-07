import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final String[] COLORS = {"GIALLO", "VERDE", "BLU", "VIOLA", "ROSSO", "ARANCIONE", "AZZURRO"};

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server Mastermind avviato. In attesa di connessioni...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione da " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new MastermindGame(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MastermindGame implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public MastermindGame(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new DataInputStream(clientSocket.getInputStream());

                //genera codice segreto prendendo i colori di riferimento COLORS = {"GIALLO", "VERDE", "BLU", "VIOLA", "ROSSO", "ARANCIONE", "AZZURRO"};
                String[] code = generateCode();
                System.out.println("Codice segreto: " + Arrays.toString(code));

                out.writeUTF("Benvenuto a Mastermind! Indovina la sequenza di colori.\n");
                out.flush();

                String clientGuess;
                while (true) {
                    clientGuess = in.readUTF();

                    String feedback = checkGuess(code, clientGuess);
                    System.out.println("Feedback: " + feedback);

                    out.writeUTF(feedback + "\n");
                    out.flush();

                    if (feedback.equals("4 pirolini neri, 0 pirolini bianchi")) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String[] generateCode() {
            //generare un codice senza ripetizioni
            String[] code = new String[4];
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                code[i] = COLORS[random.nextInt(COLORS.length)];
                for (int j = 0; j < i; j++) {
                    if (code[i].equals(code[j])) {
                        i--;
                        break;
                    }
                }
            }
            return code;
        }

        private String colorecheck(String[] code, String colore) {
            for (int i = 0; i < 4; i++) {
                if (code[i].equals(colore)) {
                    return "true";
                }
            }
            return "false";
        }
        private String checkGuess(String[] code, String guess) {
            int blackPegs = 0; 
            int whitePegs = 0; 
            String c;
            //STAMPA IL CODICE INSERITO DAL CLIENT
            System.out.println("Codice inserito dal client: " +guess);
            //TRASFORMA LA STRINGA GUESS IN ARRAY
            String[] guessArray = guess.split(" ");
            guessArray=guess.split(", ");
            //STAMPA L'ARRAY GUESS
            System.out.println("Array guess: " + Arrays.toString(guessArray));
            for (int i = 0; i < 4; i++) {
                if (guessArray[i].equals(code[i])) {
                    blackPegs++;
                } else {
                    c=colorecheck(code, guessArray[i]);
                    if( c.equals("true") ){
                        whitePegs++;
                    }
                   
                }
            }
            
            return blackPegs + " pirolini neri, " + whitePegs + " pirolini bianchi\n";
        }
    }
}
