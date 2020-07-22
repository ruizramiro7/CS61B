public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }

        if (r.getItemCount() == 1) {
            RBTreeNode tree = new RBTreeNode(true,r.getItemAt(0));

            tree.left = buildRedBlackTree(r.getChildAt(0));
            tree.right = buildRedBlackTree(r.getChildAt(1));
            return tree;
            // TODO: Replace with code to create a 2 node equivalent
        } else if (r.getItemCount() == 2) {
            RBTreeNode tree = new RBTreeNode(true, r.getItemAt(1));
            tree.left = new RBTreeNode(false, r.getItemAt(0));

            tree.right = buildRedBlackTree(r.getChildAt(2));
            tree.left.left = buildRedBlackTree(r.getChildAt(0));
            tree.left.right = buildRedBlackTree(r.getChildAt(1));
            // TODO: Replace with code to create a 3 node equivalent
            return tree;
        } else {
            RBTreeNode tree = new RBTreeNode(true,r.getItemAt(1));
            tree.left = new RBTreeNode(false,r.getItemAt(0));
            tree.right = new RBTreeNode(false, r.getItemAt(2));

            tree.left.left = buildRedBlackTree(r.getChildAt(0));
            tree.left.right = buildRedBlackTree(r.getChildAt(1));
            tree.right.left = buildRedBlackTree(r.getChildAt(2));
            tree.right.right = buildRedBlackTree(r.getChildAt(3));
            // TODO: Replace with code to create a 4 node equivalent
            return tree;
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        if (node == null){
            return null;
        }
        else if (node.left == null){
            return node;
        }
        RBTreeNode temp = node.left.right;
        RBTreeNode rotate = node.left;//set as the root
        rotate.isBlack = node.isBlack;
        rotate.right = node;
        node.left = temp;
        node.isBlack = false;

        // TODO: YOUR CODE HERE
        return rotate;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        if (node == null){
            return null;
        }
        else if (node.right == null){
            return node;
        }
        RBTreeNode temp = node.right.left;
        RBTreeNode rotate = node.right;
        rotate.isBlack = node.isBlack;
        rotate.left = node;
        node.right = temp;
        node.isBlack = false;
        // TODO: YOUR CODE HERE
        return rotate;
    }

    public void insert(T item) {   
        root = insert(root, item);
        root.isBlack = true;
    }

    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        if (node == null){
            return new RBTreeNode<>(false, item);
        }

        int compare = item.compareTo(node.item);
        if (compare == 0){
            return node;
        }
        else if (compare < 0){
            node.left = insert(node.left ,item);
        }
        else if (compare > 0) {
            node.right = insert(node.right, item);
        }

        // Case B
        if (isRed(node.left) && isRed(node.left.left)){
            node = rotateRight(node);
        }
        // Case C
        if (isRed(node.left) && isRed(node.left.right)){
            node = rotateLeft(node);
        }
        // Case A
        if (isRed(node.left) && isRed(node.right)){
            flipColors(node);
        }

        // TODO: YOUR CODE HERE
        return node;
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
