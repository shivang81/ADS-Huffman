/**
 * Created by shivanggupta on 27/03/17.
 */
public class MinHeapNode implements Comparable<MinHeapNode>{
    String data;
    Integer frequency;
    MinHeapNode left;
    MinHeapNode right;
    MinHeapNode child;
    MinHeapNode nextSibling;
    MinHeapNode prev;

    public MinHeapNode(String data, Integer frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
        this.child = null;
        this.nextSibling = null;
        this.prev = null;
    }

    public MinHeapNode() {
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
    public int compareTo(MinHeapNode node) {
        int ret = this.frequency - node.frequency;
        if(ret != 0)
            return ret;
        if(this.data.equals("$Internal$"))
            return 1;
        else
            return -1;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "MinHeapNode{" +
                "data='" + data + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
