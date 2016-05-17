package ro.cs.systems.pdsd.practicaltest02.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.cs.systems.pdsd.practicaltest02.utils.Constants;
import ro.cs.systems.pdsd.practicaltest02.utils.Utilities;
import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {


    private String address;
    private int port;
    private String word;
    private TextView wordDefTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String word,
            TextView wordDefTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        Log.i(Constants.TAG, "[CLIENT THREAD] address: " + address + " port: "+ port + "word: "+ word+"!");
        this.wordDefTextView = wordDefTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(word);
                printWriter.flush();

                String wordDef;
                while ((wordDef = bufferedReader.readLine()) != null) {
                    final String finalizedWordDef = wordDef;
                    wordDefTextView.post(new Runnable() {
                        @Override
                        public void run() {
                        	wordDefTextView.append(finalizedWordDef + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
