package PeerToPeerBlockChain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import javax.json.Json;

/**
 *
 * @author ameer
 */
public class Peer {

    public static void main(String[] args) throws IOException, Exception {
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> enter username & port # for this peer[username port]: ");
        String[] setupValues = bufferReader.readLine().split(" ");
        ServerThread serverThread = new ServerThread(setupValues[1]);
        serverThread.start();
        new Peer().updateListenToPeers(bufferReader, setupValues[0], serverThread);
    }
    
    public void updateListenToPeers(BufferedReader bufferReader, String username, ServerThread serverThread) throws Exception {
        System.out.println("> enter (space separated) hostname:port#");
        System.out.println("   peers to receive message from (s to skip):");
        String input = bufferReader.readLine();
        String[] inputValues = input.split(" ");
        if (!input.equals("!exit")) {
            for (int i = 0; i < inputValues.length; i++) {
                String[] address = inputValues[i].split(":");
                Socket socket = null;
                try {
                    socket = new Socket(address[0], Integer.valueOf(address[1]));
                    new PeerThread(socket).start();
                } catch (Exception e) {
                    if (socket != null) {
                        socket.close();
                    } else {
                        System.out.println("Invalid input, skipping to next step.");
                    }
                }
            }
        }
        BlockChain blockChain = new BlockChain();
        blockChain.addNewTransaction("Ahmed-Ali-10");
        blockChain.addNewTransaction("Mohammed-Khalil-7");
        blockChain.addNewTransaction("Ali-Mohammed-4");
        blockChain.mineBlock();
        FileManager.saveData();
        communicate(bufferReader, username, serverThread);
    }

    public void communicate(BufferedReader bufferReader, String username, ServerThread serverThread) throws Exception {
        try {
            System.out.println("> you can now communicate (!exit to exit, c to change)");
            boolean flag = true;
            while (flag) {
                String message = bufferReader.readLine();
                if (message.equals("!exit")) {
                    flag = false;
                    break;
                } else if (message.equals("c")) {
                    updateListenToPeers(bufferReader, username, serverThread);
                } else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                            .add("username", username)
                            .add("message", message)
                            .build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        } catch (Exception e) {
            
        }
    }
}
