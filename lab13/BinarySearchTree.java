public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    /* Creates an empty BST. */
    public BinarySearchTree() {
        this.root = null;
    }

    /* Creates a BST with root as ROOT. */
    public BinarySearchTree(TreeNode root) {
        this.root = root;
    }

    private boolean containsHelper(TreeNode n, T key) {
        int comparison = key.compareTo(n.item);
        if (comparison == 0) {
            return true;
        }
        else if (comparison > 0) {
            return n.right != null && containsHelper(n.right, key);
        }
        else {
            return n.left != null && containsHelper(n.left, key);
        }
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        if (root == null) {
            return false;
        }
        return containsHelper(root, key);
    }

    private void addHelper(TreeNode n, T key) {
        int comparison = key.compareTo(n.item);
        if (comparison > 0) {
            if (n.right == null) {
                n.right = new TreeNode(key);
            }
            else {
                addHelper(n.right, key);
            }
        }
        else {
            if (n.left == null) {
                n.left = new TreeNode(key);
            }
            else {
                addHelper(n.left, key);
            }
        }
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        if (root == null) {
            root = new TreeNode(key);
        }
        else if (!contains(key)) {
            addHelper(root, key);
        }
    }

    /* Deletes a node from the BST. 
     * Even though you do not have to implement delete, you 
     * should read through and understand the basic steps.
    */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (((Comparable<T>) curr.item).compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }
}