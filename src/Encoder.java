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
 * Created by shivanggupta on 27/03/17.
 */
public class encoder {
    private HashMap<String, Integer> freqTable = new HashMap<>();
    private MinHeapNode top = null;
//    private String INPUT_FILE = "/Users/shivanggupta/Desktop/sample_input_small.txt";
    private HashMap<String, String> codeTable = new HashMap<>();
    private void buildFreqTable(String inputFileName) {

        // Read the input file and build the frequency table
        try (Stream<String> stream = Files.lines(Paths.get(inputFileName))) {
            stream.forEach(entry -> {
                if (freqTable.containsKey(entry)) {
                    freqTable.put(entry, freqTable.get(entry) + 1);
                } else {
                    freqTable.put(entry, 1);
                }
            });
//            Long startTime = System.currentTimeMillis();
//            for(int i = 0; i < 10; i++)
//                buildHuffmanTree();
//            System.out.println("Time using binary heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));
//
//            startTime = System.currentTimeMillis();
//            for(int i = 0; i < 10; i++)
                buildHuffmanTreeUsingFourWayHeap();
//            System.out.println("Time using 4-way heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));
//
//            startTime = System.currentTimeMillis();
//            for(int i = 0; i < 10; i++)
//                buildHuffmanTreeUsingPairingHeap();
//            System.out.println("Time using pairing heap (millisecond): " + String.valueOf((System.currentTimeMillis() - startTime) / 10.0));

            createCodeTable(top, new int[10000], 0);
            saveCodeTable();
//            System.out.println();
            encode(inputFileName);
//            System.out.println(encodedString);
//            System.out.println(decode1(encodedString));
            decoder decoder = new decoder();
//            decoder.decode(encodedString, codeTable);


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

    MinHeapNode buildHuffmanTreeUsingFourWayHeap() {
        MinHeapNode left, right;
        FourWayHeap<MinHeapNode> fourWayHeap = new FourWayHeap<>(freqTable.size());

        for(Map.Entry<String, Integer> entry : freqTable.entrySet()) {
            fourWayHeap.insert(new MinHeapNode(entry.getKey(), entry.getValue()));
        }

        while (fourWayHeap.getSize() != 1) {
            left = fourWayHeap.removeMin();
            right = fourWayHeap.removeMin();

            top = new MinHeapNode("$Internal$", left.frequency + right.frequency);
            top.left = left;
            top.right = right;
            fourWayHeap.insert(top);
        }

        return fourWayHeap.removeMin();
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

        if (root.left == null && root.right == null) {
//            System.out.print(root.data + " ");
            String code  = printArr(arr, top);
            codeTable.put(root.data, code);
//            System.out.println();
        }
    }

    void saveCodeTable() {
        try {
            File file = new File("codeTable.txt");
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
            while ((line = bufferedReader.readLine()) != null) {
                encoded.append(codeTable.get(line));
            }
//            System.out.println(encoded.toString());
            BitWriter bw = new BitWriter(encoded.length());
            for( int i = 0; i < encoded.length(); i++) {
                bw.writeBit(encoded.charAt(i) == '1');
            }
            byte[] b = bw.toArray();
            FileOutputStream fos = new FileOutputStream("encoded.bin");
            fos.write(b);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    String decode1(String encodedString) {
        HashMap<String, String> map = new HashMap<>();
        for(Map.Entry<String,String> entry : codeTable.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        StringBuilder res = new StringBuilder();
        String temp = "";
        for(int i = 0; i < encodedString.length(); i++) {
            temp += encodedString.charAt(i);
            if(map.containsKey(temp)) {
                res.append(map.get(temp) + "\n");
                temp = "";
            }
        }
        return res.toString();
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
        String inputFileName = args[0];
        encoder encoder = new encoder();
        encoder.buildFreqTable(inputFileName);
    }
}
