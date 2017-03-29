package huffman;

public class PairingHeap
{
    public PairingHeap() {
        root = null;
        size = 0;
    }

    public MinHeapNode insert(MinHeapNode node) {
        if( root == null )
            root = node;
        else
            root = compareAndLink(root, node);

        size++;
        return node;
    }

    public MinHeapNode findMin() {
        if(isEmpty())
            throw new RuntimeException("Pairing heap is empty");
        return root;
    }

    public MinHeapNode deleteMin() {
        if(isEmpty())
            throw new RuntimeException( "Pairing heap is empty" );

        MinHeapNode min = findMin();
        if(root.child == null)
            root = null;
        else
            root = combineSiblings(root.child);

        size--;
        return min;
    }

    public boolean isEmpty( ) {
        return root == null;
    }

    public int getSize() {
        return size;
    }

    private MinHeapNode root;
    private int size;

    private MinHeapNode compareAndLink( MinHeapNode first, MinHeapNode second ) {
        if( second == null )
            return first;

        if( second.compareTo( first ) < 0 ) {
            // Attach first as leftmost child of second
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.child;
            if( first.nextSibling != null )
                first.nextSibling.prev = first;
            second.child = first;
            return second;
        }
        else {
            // Attach second as leftmost child of first
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if( first.nextSibling != null )
                first.nextSibling.prev = first;
            second.nextSibling = first.child;
            if( second.nextSibling != null )
                second.nextSibling.prev = second;
            first.child = second;
            return first;
        }
    }

    private MinHeapNode [ ] doubleIfFull( MinHeapNode [ ] array, int index ) {
        if( index == array.length ) {
            MinHeapNode [ ] oldArray = array;

            array = new MinHeapNode[ index * 2 ];
            System.arraycopy(oldArray, 0, array, 0, index);
        }
        return array;
    }

    // The tree array for combineSiblings
    private MinHeapNode [ ] treeArray = new MinHeapNode[ 5 ];

    private MinHeapNode combineSiblings( MinHeapNode firstSibling ) {
        if( firstSibling.nextSibling == null )
            return firstSibling;

        // Store the subtrees in an array
        int numSiblings = 0;
        for( ; firstSibling != null; numSiblings++ ) {
            treeArray = doubleIfFull( treeArray, numSiblings );
            treeArray[ numSiblings ] = firstSibling;
            firstSibling.prev.nextSibling = null;  // break links
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull( treeArray, numSiblings );
        treeArray[ numSiblings ] = null;

        // Combine subtrees two at a time, going left to right
        int i = 0;
        for( ; i + 1 < numSiblings; i += 2 )
            treeArray[ i ] = compareAndLink( treeArray[ i ], treeArray[ i + 1 ] );

        int j = i - 2;

        // j has the result of last compareAndLink.
        // If an odd number of trees, get the last one.
        if( j == numSiblings - 3 )
            treeArray[ j ] = compareAndLink( treeArray[ j ], treeArray[ j + 2 ] );

        // Now go right to left, merging last tree with
        // next to last. The result becomes the new last.
        for( ; j >= 2; j -= 2 )
            treeArray[ j - 2 ] = compareAndLink( treeArray[ j - 2 ], treeArray[ j ] );

        return treeArray[ 0 ];
    }
}