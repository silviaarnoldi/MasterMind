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
        private PrintWriter out;
        private BufferedReader in;

        public MastermindGame(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String[] code = generateCode();

                out.println("Benvenuto a Mastermind! Indovina la sequenza di colori.");

                int attempts = 0;
                while (attempts < 10) {
                    attempts++;

                    String clientGuess = in.readLine();

                    String feedback = checkGuess(code, clientGuess);
                    //INSERISCI LA LOGICA CHE SE I COLORI CHE INSERISCE IL CLIENT è GIUSTO MA NON NELLA POSIZIONE FAI IUN MESSAGGIO CON IL NUMERO  DEI PIORLINI NERI E BIANCHI A SECONDO SE IL CLIENT HA INSERITO  COLORE GIUSTO E POSIZIONE GIUSTA O COLORE GIUSTO E POSIZIONE SBAGLIATA
                    



                    out.println(feedback);

                    if (feedback.equals("4 pirolini neri")) {
                        out.println("Congratulazioni! Hai indovinato la sequenza. Il gioco termina.");
                        break;
                    }
                }

                if (attempts == 10) {
                    out.println("Hai esaurito i tentativi. La sequenza corretta era: " + Arrays.toString(code));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    in.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String[] generateCode() {
            Random random = new Random();
            String[] code = new String[4];
            for (int i = 0; i < 4; i++) {
                code[i] = COLORS[random.nextInt(COLORS.length)];
            }
            return code;
        }

        private String checkGuess(String[] code, String guess) {
            int blackPegs = 0; 
            int whitePegs = 0; 

            for (int i = 0; i < 4; i++) {
                if (guess.equals(code[i])) {
                    blackPegs++;
                } else if (Arrays.asList(code).contains(guess)) {
                    whitePegs++;
                }
            }

            return blackPegs + " pirolini neri, " + whitePegs + " pirolini bianchi";
        }
    }
}