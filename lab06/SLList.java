/**
 * An SLList is a list of integers, which encapsulates the
 * naked linked list structure.
 */
public class SLList {

    /**
     * IntListNode is a nested class that represents a single node in the
     * SLList, storing an item and a reference to the next IntListNode.
     */
    private static class IntListNode {
        /*
         * The access modifiers inside a private nested class are irrelevant:
         * both the inner class and the outer class can access these instance
         * variables and methods.
         */
        public int item;
        public IntListNode next;

        public IntListNode(int item, IntListNode next) {
            this.item = item;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntListNode that = (IntListNode) o;
            return item == that.item;
        }

        @Override
        public String toString() {
            return item + "";
        }

    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntListNode sentinel;
    private int size;

    /** Creates an empty SLList. */
    public SLList() {
        sentinel = new IntListNode(42, null);
        sentinel.next = sentinel;
        size = 0;
    }

    public SLList(int x) {
        sentinel = new IntListNode(42, null);
        sentinel.next = new IntListNode(x, null);
        sentinel.next.next = sentinel;
        size = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SLList slList = (SLList) o;
        if (size != slList.size) return false;

        IntListNode l1 = sentinel.next;
        IntListNode l2 = slList.sentinel.next;

        while (l1 != sentinel && l2 != slList.sentinel) {
            if (!l1.equals(l2)) return false;
            l1 = l1.next;
            l2 = l2.next;
        }
        return true;
    }

    @Override
    public String toString() {
        IntListNode l = sentinel.next;
        String result = "";

        while (l != sentinel) {
            result += l + " ";
            l = l.next;
        }

        return result.trim();
    }

    /** Returns an SLList consisting of the given values. */
    public static SLList of(int... values) {
        SLList list = new SLList();
        for (int i = values.length - 1; i >= 0; i -= 1) {
            list.addFirst(values[i]);
        }
        return list;
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }

    /** Adds x to the front of the list. */
    public void addFirst(int x) {
        sentinel.next = new IntListNode(x, sentinel.next);
        size += 1;
    }

    /** Return the value at the given index. */
    public int get(int index) {
        IntListNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Adds x to the list at the specified index. */
    public void add(int index, int x) {
        // TODO
        IntListNode prev = sentinel;
        IntListNode curr = sentinel.next;
        while (index > 0 && curr != sentinel) {
            index -= 1;
            prev = curr;
            curr = curr.next;
        }
        prev.next = new IntListNode(x, curr);
        size += 1;
    }

    private IntListNode reverseHelper(IntListNode L) {
        // Base case: either 0 or 1 nodes left
        if (L.next == sentinel) {
            // The base case means the new first node so sentinel should point to it
            sentinel.next = L;
            return L;
        }
        // Current reversed list (leap of faith),
        // e.g. [L, 3, 5] becomes L=[L] and temp=[5, 3]
        IntListNode temp = reverseHelper(L.next);
        // Get the last node in the reversed list, e.g. [3]
        IntListNode last = temp;
        while (last.next != sentinel) {
            last = last.next;
        }
        // Set the last node to point to this node, e.g. [3]->[L]
        last.next = L;
        // Set this node to point to sentinel, e.g. [L]->[sentinel]
        L.next = sentinel;
        return temp;
    }

    /** Destructively reverses this list. */
    public void reverse() {
        // TODO
        reverseHelper(sentinel.next);
    }
}
