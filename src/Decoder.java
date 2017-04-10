import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Decoder class to decode the huffman encoded file.
 *
 * Created by shivanggupta on 29/03/17.
 */

public class decoder {
    private MinHeapNode decodeTreeRoot = null;
    private FileInputStream inputStream = null;
    private static BufferedWriter bufferedWriter;
    private static boolean isPrintEnabled = false;

    void buildDecodeTree(String codeFileName) {
        try (Stream<String> stream = Files.lines(Paths.get(codeFileName))) {
            stream.forEach(line -> {
                String data = line.split(" ")[0];
                String code = line.split(" ")[1];
                decodeTreeRoot = insertInDecodeTree(decodeTreeRoot, code, 0, data);
            });
        } catch (IOException e)  {
            e.printStackTrace();
        }
    }

    MinHeapNode insertInDecodeTree(MinHeapNode node, String code, int i, String data){
        if(node == null) {
            node = new MinHeapNode(data,0);
            if(i == code.length()){
                node.data = data;
                return node;
            }
        }
        if(code.charAt(i) == '0'){
            node.left = insertInDecodeTree(node.left, code, i + 1, data);
        } else {
            node.right = insertInDecodeTree(node.right, code, i + 1, data);
        }
        return node;
    }

    void decodeFile(String encodedFile) throws IOException{
        inputStream = new FileInputStream(encodedFile);
        MinHeapNode currentNode = decodeTreeRoot;
        int b = getByte();  // Reads a byte from the encoded file
        while (b != -1) {
            for(int i = 0 ; i < 8 ; i++) {
                int bit = b & getMask(i);
                if(bit != 0){
                    currentNode = currentNode.right;
                } else {
                    currentNode = currentNode.left;
                }
                if(currentNode.isLeaf()) {
                    writeToFile(currentNode.data);
                    currentNode = decodeTreeRoot;
                }
            }
            b = getByte();  // Reads a byte from the encoded file
        }
    }

    int getMask(int i) {
        return (1 << (7 - i));
    }

    int getByte() throws IOException {
        int b;
        if((b = inputStream.read()) != -1) {
            return b;
        }
        return -1;
    }

    void writeToFile(String data) {
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void printLog(String str) {
        if(isPrintEnabled)
            System.out.println(str);
    }

    public static void main(String[] args) throws IOException {
        Long start = System.currentTimeMillis();
        String encodedFile = args[0];
        String codeTableFile = args[1];
        decoder d = new decoder();
        FileWriter fw = new FileWriter("decoded.txt");
        bufferedWriter = new BufferedWriter(fw);
        d.buildDecodeTree(codeTableFile);
        d.decodeFile(encodedFile);
        bufferedWriter.close();
        printLog("Time to decode(milliseconds) = " + String.valueOf(System.currentTimeMillis() - start));
    }
}