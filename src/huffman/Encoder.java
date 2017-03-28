package huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by shivanggupta on 27/03/17.
 */
public class Encoder {
    private HashMap<String, Integer> freqTable = new HashMap<>();
    private Map<String, Integer> sortedFreqTable;
    private MinHeapNode top = null;
    private String INPUT_FILE = "/Users/shivanggupta/Desktop/sample_input_small.txt";
    private HashMap<String, String> codeTable = new HashMap<>();
    private void buildFreqTable() {

        // Read the input file and build the frequency table
        try (Stream<String> stream = Files.lines(Paths.get(INPUT_FILE))) {
            stream.forEach(entry -> {
                if (freqTable.containsKey(entry)) {
                    freqTable.put(entry, freqTable.get(entry) + 1);
                } else {
                    freqTable.put(entry, 1);
                }
            });
            Long startTime = System.currentTimeMillis();
            for(int i = 0; i < 10; i++)
                buildHuffmanTree();
            System.out.println("Time in milliseconds = " + String.valueOf((System.currentTimeMillis() - startTime) / 10));
            printCodes(top, new int[10000], 0);
//            System.out.println();
//            String encodedString = encode();
//            System.out.println(encodedString);
//            System.out.println(decode(encodedString));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    MinHeapNode buildHuffmanTree() {
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

    void printCodes(MinHeapNode root, int arr[], int top) {
        if (root.left != null) {
            arr[top] = 0;
            printCodes(root.left, arr, top + 1);
        }

        if (root.right != null) {
            arr[top] = 1;
            printCodes(root.right, arr, top + 1);
        }

        if (root.left == null && root.right == null) {
//            System.out.print(root.data + " ");
            String code  = printArr(arr, top);
            codeTable.put(root.data, code);
//            System.out.println();
        }
    }

    String encode() {
        StringBuilder encodedString = new StringBuilder();
        try {
            File file = new File(INPUT_FILE);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                encodedString.append(codeTable.get(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedString.toString();
    }

    String decode(String encodedString) {
        String res = "";
        MinHeapNode node = top;
        for (int i = 0; i != encodedString.length(); ++i) {
            if (encodedString.charAt(i) == '0') {
                node = node.left;
            } else {
                node = node.right;
            }
            if (node.isLeaf()) {
                res += node.data + "\n";
                node = top;
            }
        }
        return res;
    }


    String printArr(int[] arr, int n) {
        String code = "";
        for(int i = 0; i < n; i++) {
//            System.out.print(arr[i]);
            code += arr[i];
        }
        return code;
    }

    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        encoder.buildFreqTable();
    }
}
