/**
 * Created by shivanggupta on 28/03/17.
 */
public class FourWayHeap<MinHeapNode extends Comparable<MinHeapNode>> {

    private final static int EXPAND_RATIO = 2; //how many times should be the underlying heap expanded
    private final static double COLLAPSE_RATIO = 0.25; //how empty must the heap be, to be the underlying collaped
    private MinHeapNode[] heap;
    private int d = 4; //parameter d
    private int size; //size of the heap
    private int initialSize;

    /**
     * Constructor
     * @param initialSize initial capacity of the heap
     */
    public FourWayHeap(int initialSize) {
        this.heap = (MinHeapNode[]) new Comparable[initialSize + 3];
        this.initialSize = initialSize;
        this.size = 0;
    }

    /**
     * Insert element into the heap
     * Complexity: O(log(n))
     * @param i element to be inserted
     */
    public void insert(MinHeapNode i) {
//        if (heap.length == size) {
//            expand();
//        }
        size++;
        int index = size + 2;
        int parentIndex = getParentIndex(index);
        while (index > 3 && i.compareTo(heap[parentIndex]) < 0) { //while the element is less then its parent
            heap[index] = heap[parentIndex]; //place parent one level down
            index = parentIndex; //and repeat the procedure on the next level
            parentIndex = getParentIndex(index);
        }
        heap[index] = i; //insert the element at the appropriate place
    }

    /**
     * Return the top element and remove it from the heap
     * Complexity: O(log(n))
     * @return top element
     */
    public MinHeapNode removeMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        MinHeapNode tmp = heap[3];
        heap[3] = heap[size + 2];
        size--;
//        if (size < heap.length * COLLAPSE_RATIO && heap.length / EXPAND_RATIO > initialSize) {
//            collapse();
//        }
        repairTop(3);
        return tmp;
    }

    /**
     * Return index of the parent element
     * @param index index of element, for which we want to return index of its parent
     * @return index of the parent element
     */
    private int getParentIndex(int index) {
        return (index - 1) / d + 3;
    }

    /**
     * Place the top of the heap at a correct place withing the heap (repair the heap)
     * @param topIndex index of the top of the heap
     */
    private void repairTop(int topIndex) {
        MinHeapNode tmp = heap[topIndex];
        int succ = findSuccessor((topIndex-3) * d + 4, (topIndex-3) * d + d + 3);
        while (succ < size + 3 && tmp.compareTo(heap[succ]) > 0) {
            heap[topIndex] = heap[succ];
            topIndex = succ;
            succ = findSuccessor((succ-3) * d + 4, (succ-3) * d + d + 3);
        }
        heap[topIndex] = tmp;
    }

    /**
     * Return descendant with the least value
     * @param from index of the first descendant
     * @param to index of the last descendant
     * @return index of the descendant with least value
     */
    private int findSuccessor(int from, int to) {
        int succ = from;
        for (int i = from + 1; i <= to && i < size; i++) {
            if ((heap[succ]).compareTo(heap[i]) > 0) {
                succ = i;
            }
        }
        return succ;
    }

    /**
     * Expand the underlying heap
     */
//    private void expand() {
//        heap = Arrays.copyOf(heap, heap.length * EXPAND_RATIO);
//    }
//
//    /**
//     * Collapse the underlying heap
//     */
//    private void collapse() {
//        heap = Arrays.copyOf(heap, heap.length / EXPAND_RATIO);
//    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(heap[i]).append(" ");
        }
        return builder.toString();
    }
}