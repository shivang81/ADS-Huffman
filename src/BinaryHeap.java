/**
 * Created by shivanggupta on 27/03/17.
 */
public class BinaryHeap<MinHeapNode extends Comparable<MinHeapNode>> {
    private static final int CAPACITY = 2;

    private int size;
    private MinHeapNode[] heap;

    public BinaryHeap() {
        size = 0;
        heap = (MinHeapNode[]) new Comparable[CAPACITY];
    }

    public BinaryHeap(MinHeapNode[] array) {
        size = array.length;
        heap = (MinHeapNode[]) new Comparable[array.length+1];

        System.arraycopy(array, 0, heap, 1, array.length);
        buildHeap();
    }

    private void buildHeap() {
        for (int k = size/2; k > 0; k--) {
            heapify(k);
        }
    }
    private void heapify(int k) {
        MinHeapNode tmp = heap[k];
        int child;

        for(; 2*k <= size; k = child) {
            child = 2*k;

            if(child != size &&
                    heap[child].compareTo(heap[child + 1]) > 0) child++;

            if(tmp.compareTo(heap[child]) > 0)  heap[k] = heap[child];
            else
                break;
        }
        heap[k] = tmp;
    }

    public MinHeapNode removeMin() throws RuntimeException {
        if (size == 0) throw new RuntimeException();
        MinHeapNode min = heap[1];
        heap[1] = heap[size--];
        heapify(1);
        return min;
    }

    public void insert(MinHeapNode x) {
        if(size == heap.length - 1) doubleSize();

        int pos = ++size;

        for(; pos > 1 && x.compareTo(heap[pos/2]) < 0; pos = pos/2 )
            heap[pos] = heap[pos/2];

        heap[pos] = x;
    }
    private void doubleSize() {
        MinHeapNode [] old = heap;
        heap = (MinHeapNode []) new Comparable[heap.length * 2];
        System.arraycopy(old, 1, heap, 1, size);
    }

    public int getSize() {
        return this.size;
    }

    public String toString() {
        String out = "";
        for(int k = 1; k <= size; k++) out += heap[k]+" ";
        return out;
    }
}
