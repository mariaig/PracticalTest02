package ro.cs.systems.pdsd.practicaltest02.network;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import ro.cs.systems.pdsd.practicaltest02.utils.Constants;
import ro.cs.systems.pdsd.practicaltest02.utils.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunicationThread extends Thread {
	private ServerThread serverThread;
	private Socket socket;

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket = socket;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter printWriter = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG,
							"[COMMUNICATION THREAD] Waiting for parameters from client (word)!");
					String word = bufferedReader.readLine();
					if (word != null && !word.isEmpty()) {
						Log.i(Constants.TAG,
								"[COMMUNICATION THREAD] Getting the information from the webservice...");
						HttpClient httpClient = new DefaultHttpClient();
						HttpGet httpGet = new HttpGet(
								Constants.GET_WEB_SERVICE_ADDRESS + word);
						HttpResponse httpGetResponse = httpClient
								.execute(httpGet);
						HttpEntity httpGetEntity = httpGetResponse.getEntity();
						if (httpGetEntity != null) {
							// do something with the response
							String result = EntityUtils.toString(httpGetEntity);
							Log.i (Constants.TAG, "Received def:");
							Log.i(Constants.TAG, result);
							serverThread.setWordDef("maria test");
                            printWriter.println("maria test");
                            printWriter.flush();
						}
					}
				}
				socket.close();
			} catch (Exception ex) {
				Log.e(Constants.TAG, "An exception has occurred: " + ex.getMessage());
	            if (Constants.DEBUG) {
	            	ex.printStackTrace();
	            }
			}
		}
	}

}