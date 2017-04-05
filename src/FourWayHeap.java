import java.util.NoSuchElementException;

public class FourWayHeap {

    private int d = 4;
    private int size;
    private MinHeapNode[] array;	// The heap array
    private int shift = 3;

    public FourWayHeap(MinHeapNode [] array){
        size = array.length;
        buildMinHeap(array);
    }

    private int parent(int i) {
        i = i - shift;
        return (i-1)/d + shift;
    }

    private int kthChild(int i, int k) {
        i -= shift;
        return d*i + k + shift;
    }

    public void insert(MinHeapNode x) {
        if(isFull())
            throw new NoSuchElementException("Heap is full");

        // heapify up
        int hole = size + shift;
        size++;
        array[hole] = x;
        heapifyUp(hole);
    }

    public MinHeapNode findMin( ) {
        if( isEmpty( ) )
            return null;
        return array[shift];
    }

    public int getSize() {
        return size;
    }

    public MinHeapNode extractMin() {
        if(isEmpty())
            return null;

        MinHeapNode minItem = findMin();
        array[shift] = array[size - 1 + shift];
        size--;
        heapifyDown(shift);
        return minItem;
    }

    public void buildMinHeap(MinHeapNode[] array) {
        this.array = new MinHeapNode[array.length + shift];
        int k = shift;
        for(MinHeapNode node : array) {
            this.array[k++] = node;
        }
        this.size = array.length;
        for(int i = size + shift - 1; i >= shift; i--)
            heapifyDown(i);
    }


    public boolean isEmpty( ) {
        return size == 0;
    }


    public boolean isFull( ) {
        return size == array.length;
    }

    private void heapifyDown(int hole) {
        int child;
        MinHeapNode tmp = array[hole];

        while(kthChild(hole, 1) < size + shift) {
            child = smallestChild(hole);

            if( array[child].compareTo(tmp) < 0 )
                array[hole] = array[child];
            else
                break;
            hole = child;
        }
        array[hole] = tmp;
    }

    private int smallestChild(int hole) {
        int bestChildYet = kthChild(hole, 1);
        int k = 2;
        int candidateChild = kthChild(hole, k);
        while ((k <= d) && (candidateChild < size + shift)) {
            if (array[candidateChild].compareTo(array[bestChildYet]) < 0)
                bestChildYet = candidateChild;
            k++;
            candidateChild = kthChild(hole, k);
        }
        return bestChildYet;
    }

    private void heapifyUp(int hole) {
        MinHeapNode tmp = array[hole];
        for (; hole > shift && tmp.compareTo( array[parent(hole)] ) < 0; hole = parent(hole) )
            array[hole] = array[parent(hole)];
        array[hole] = tmp;
    }
}
