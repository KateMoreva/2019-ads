import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedBlackTree<Key extends Comparable<Key>, Value> implements Bst<Key, Value> {

    static boolean BLACK = false;
    static boolean RED = true;


    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;
        boolean color;
        private int height;

        private Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.color = RED;
            this.height = 1;
        }

        private int getHeight() {
            return height;
        }
    }

    private Node root;
    private Node removed;
    private int size;

    private boolean isRed(Node node)
    {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        right.color = node.color;
        node.color = RED;
        return right;
    }

    private Node rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        left.color = node.color;
        node.color = RED;
        return left;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if (node.left != null) {
            node.left.color = !node.left.color;
        }
        if (node.right != null) {
            node.right.color = !node.right.color;
        }
    }

    private Node fixUp(Node node) {
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (node.right != null && isRed(node.right.right)) {
            node.right = rotateLeft(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    private Node max(Node node) {
        if (node.right == null) {
            return node;
        }
        return max(node.right);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return null;
        }
        if (!isRed(node.left) || !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    @Nullable
    @Override
    public Value get(@NotNull Key key) {
        Node node = findNode(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }
    private Node findNode(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        }
        else if (key.compareTo(node.key) < 0) {
            return findNode(node.left, key);
        } else {
            return findNode(node.right, key);
        }
    }

    @Override
    public void put(@NotNull Key key,@NotNull Value value){
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node node, Key key, Value value){
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) == 0) {
            node.value = value;
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.left = put(node.left, key, value);
        }
         node = fixUp(node);
        return node;
    }

    @Nullable
    @Override
    public Value remove(@NotNull Key key) {
        if (root == null) {
            return null;
        }
        root = remove(root, key);
        Node removed = this.removed;
        this.removed = null;
        if (removed != null) {
            if (root != null){
                root.color = BLACK;
            }
            size--;
            return removed.value;
        }
        return null;
    }

    private Node remove(Node node, Key key) {
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (!isRed(node.left) || !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = remove(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (cmp == 0 && node.right == null) {
                removed = node;
                return null;
            }

            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            cmp = key.compareTo(node.key);
            if (cmp == 0) {
                removed = node;
                Node min = min(node.right);
                min.left = node.left;
                min.right = deleteMin(node.right);
                node = min;
            } else {
                node.right = remove(node.right, key);
            }
        }
        return fixUp(node);
    }

    @Nullable
    @Override
    public Key min() {
        if (root == null) {
            return null;
        }
        return min(root).key;
    }

    @Nullable
    @Override
    public Value minValue() {
        if (root == null) {
            return null;
        }
        return min(root).value;
    }

    @Nullable
    @Override
    public Key max() {
        if (root == null) {
            return null;
        }
        return max(root).key;
    }

    @Nullable
    @Override
    public Value maxValue() {
        if (root == null) {
            return null;
        }
        return max(root).value;
    }

    @Nullable
    @Override
    public Key floor(@NotNull Key key) {
        if (root == null) {
            return null;
        }
        Node floor = floor(root, key);
        if (floor == null) {
            return null;
        }
        return floor.key;
    }

    private Node floor(Node node, Key key) {
        if (node.right != null && key.compareTo(node.right.key) > 0) {
            return floor(node.right, key);
        }
        if (node.left != null && key.compareTo(node.key) < 0) {
            return floor(node.left, key);
        }
        if (key.compareTo(node.key) < 0) {
            return null;
        }
        return node;
    }

    @Nullable
    @Override
    public Key ceil(@NotNull Key key) {
        if (root == null) {
            return null;
        }
        Node ceil = ceil(root, key);
        if (ceil == null) {
            return null;
        }
        return ceil.key;
    }

    private Node ceil(Node node, Key key) {
        if (node.left != null && key.compareTo(node.left.key) < 0) {
            return ceil(node.left, key);
        }
        if (node.right != null && key.compareTo(node.key) > 0) {
            return ceil(node.right, key);
        }
        if (key.compareTo(node.key) > 0) {
            return null;
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        if (root == null) {
            return 0;
        }
        return root.getHeight();
    }
}
