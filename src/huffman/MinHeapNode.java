package huffman;

/**
 * Created by shivanggupta on 27/03/17.
 */
public class MinHeapNode implements Comparable<MinHeapNode>{
    String data;
    Integer frequency;
    MinHeapNode left;
    MinHeapNode right;

    public MinHeapNode(String data, Integer frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public MinHeapNode() {
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
    public int compareTo(MinHeapNode node) {
        return this.frequency - node.frequency;
    }

    @Override
    public String toString() {
        return "MinHeapNode{" +
                "data='" + data + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
