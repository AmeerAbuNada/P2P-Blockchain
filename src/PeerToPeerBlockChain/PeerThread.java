package PeerToPeerBlockChain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author ameer
 */
public class PeerThread extends Thread {
    private BufferedReader bufferedReader;
    
    public PeerThread(Socket socket) throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    @Override
    public void run() {
        boolean flag = true;
        while(flag) {
            try {
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
                if(jsonObject.containsKey("username")) {
                    System.out.println("["+jsonObject.getString("username")+"]: " + jsonObject.getString("message"));
                }
            } catch(Exception ex) {
                flag = false;
                interrupt();
            }
        }
    }
}
