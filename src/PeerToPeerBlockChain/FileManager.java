package PeerToPeerBlockChain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ameer
 */
public class FileManager {

    public static File blockChainFile = new File("blockchain.txt");

    static {
        try {
            if (blockChainFile.createNewFile()) {
                System.out.println("New BlockChain File Created and Loaded Successfully!");
            } else {
                System.out.println("Blockchain file loaded.");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File getFile() {
        return blockChainFile;
    }

    public static ArrayList<String> readFile() {
        ArrayList<String> result = new ArrayList<>();
        try {
            Scanner reader = new Scanner(blockChainFile);
            while (reader.hasNextLine()) {
                result.add(reader.nextLine());
            }
            return result;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveData() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(blockChainFile.getName(), "UTF-8");
        for(Block block : BlockChain.blockChain) {
            Header temp = block.getHeader();
            String headerLine = temp.getVersion() + "@"
                    + temp.getPreviousHash() + "@"
                    + temp.getMerkleRoot() + "@"
                    + temp.getTimeStamp() + "@"
                    + temp.getDifficulty() + "@"
                    + temp.getNonce();
            String bodyLine = block.getIndex() + "@"
                    + block.getTransactionsCount() + "@"
                    + block.getTransactions() + "@"
                    + block.getHash();
            writer.println(headerLine);
            writer.println(bodyLine);
        }
        writer.close();
    }

}
