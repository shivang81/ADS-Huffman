import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Encoder to create huffman codes table and encoded file.
 *
 * Created by shivanggupta on 27/03/17.
 */
public class encoder {
    private HashMap<String, Integer> freqTable = new HashMap<>();
    private MinHeapNode top = null;
    private HashMap<String, String> codeTable = new HashMap<>();
    private boolean isPrintEnabled = false;

    private void buildFreqTableAndEncode(String inputFileName) {

        // Read the input file and build the frequency table
        try (Stream<String> stream = Files.lines(Paths.get(inputFileName))) {
            stream.forEach(entry -> {
                if (freqTable.containsKey(entry)) {
                    freqTable.put(entry, freqTable.get(entry) + 1);
                } else {
                    freqTable.put(entry, 1);
                }
            });
            Long startTime = System.currentTimeMillis();
            for(int i = 0; i < 10; i++)
                buildHuffmanTreeUsingBinaryHeap();
            System.out.println("Time using binary heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));

            startTime = System.currentTimeMillis();
            for(int i = 0; i < 10; i++)
            buildHuffmanTreeUsingFourWayHeap();
            System.out.println("Time using 4-way heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));

            startTime = System.currentTimeMillis();
            for(int i = 0; i < 10; i++)
                buildHuffmanTreeUsingPairingHeap();
            System.out.println("Time using pairing heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));

            createCodeTable(top, new int[10000], 0);
            saveCodeTable();
            encode(inputFileName);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    MinHeapNode buildHuffmanTreeUsingBinaryHeap() {
        MinHeapNode left, right;
        MinHeapNode[] heapNodes = new MinHeapNode[freqTable.size()];
        int i = 0;
        for(Map.Entry<String, Integer> entry : freqTable.entrySet()) {
            heapNodes[i++] = new MinHeapNode(entry.getKey(), entry.getValue());
        }

        BinaryHeap<MinHeapNode> binaryHeap = new BinaryHeap<>(heapNodes);

        while (binaryHeap.getSize() != 1) {
            left = binaryHeap.removeMin();
            right = binaryHeap.removeMin();

            top = new MinHeapNode("$Internal$", left.frequency + right.frequency);
            top.left = left;
            top.right = right;
            binaryHeap.insert(top);
        }
        return binaryHeap.removeMin();
    }

    MinHeapNode buildHuffmanTreeUsingFourWayHeap() {
        MinHeapNode left, right;
        MinHeapNode[] heapNodes = new MinHeapNode[freqTable.size()];
        int i = 0;
        for(Map.Entry<String, Integer> entry : freqTable.entrySet()) {
            heapNodes[i++] = new MinHeapNode(entry.getKey(), entry.getValue());
        }
        FourWayHeap fourWayHeap = new FourWayHeap(heapNodes);

        fourWayHeap.buildMinHeap(heapNodes);

        while (fourWayHeap.getSize() != 1) {
            left = fourWayHeap.extractMin();
            right = fourWayHeap.extractMin();

            top = new MinHeapNode("$Internal$", left.frequency + right.frequency);
            top.left = left;
            top.right = right;
            fourWayHeap.insert(top);
        }
        return fourWayHeap.extractMin();
    }

    MinHeapNode buildHuffmanTreeUsingPairingHeap() {
        MinHeapNode left, right;
        PairingHeap pairingHeap = new PairingHeap();

        for(Map.Entry<String, Integer> entry : freqTable.entrySet()) {
            pairingHeap.insert(new MinHeapNode(entry.getKey(), entry.getValue()));
        }

        while (pairingHeap.getSize() != 1) {
            left = pairingHeap.deleteMin();
            right = pairingHeap.deleteMin();

            top = new MinHeapNode("$Internal$", left.frequency + right.frequency);
            top.left = left;
            top.right = right;
            pairingHeap.insert(top);
        }
        return pairingHeap.deleteMin();
    }


    void createCodeTable(MinHeapNode root, int arr[], int top) {
        if (root.left != null) {
            arr[top] = 0;
            createCodeTable(root.left, arr, top + 1);
        }

        if (root.right != null) {
            arr[top] = 1;
            createCodeTable(root.right, arr, top + 1);
        }

        if (root.isLeaf()) {
            printLog(root.data + " ");
            String code  = buildCode(arr, top);
            codeTable.put(root.data, code);
            printLog("");
        }
    }

    void printLog(String str) {
        if(isPrintEnabled)
            System.out.println(str);
    }

    void saveCodeTable() {
        try {
            File file = new File("code_table.txt");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for(Map.Entry<String,String> entry : codeTable.entrySet()) {
                bw.write(entry.getKey() + " " + entry.getValue());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void encode(String inputFileName) {
        try {
            File file = new File(inputFileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder encoded = new StringBuilder();
            FileOutputStream fout = new FileOutputStream("encoded.bin");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fout);

            String byteStr;
            Integer parsedByte;

            while ((line = bufferedReader.readLine()) != null &&
                    !line.trim().equals("")){
                int val = Integer.parseInt(line);
                encoded.append(codeTable.get(String.valueOf(val)));
                while(encoded.length() >= 8){
                    byteStr = encoded.toString().substring(0, 8);
                    parsedByte = Integer.parseInt(byteStr, 2);
                    byte b = parsedByte.byteValue();
                    bufferedOutputStream.write(b);
                    encoded.delete(0,8);
                }
            }
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String buildCode(int[] arr, int n) {
        String code = "";
        for(int i = 0; i < n; i++) {
            printLog(String.valueOf(arr[i]));
            code += arr[i];
        }
        return code;
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        String inputFileName = args[0];
        encoder encoder = new encoder();
        encoder.buildFreqTableAndEncode(inputFileName);
        System.out.println("Time to encode = " + String.valueOf(System.currentTimeMillis() - start));
    }
}
