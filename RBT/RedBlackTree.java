// Red-Black Tree

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

enum Color {
    RED, BLACK
}

// Node Class
class Node<T extends Comparable<T>> {
    T data;
    Node<T> left;
    Node<T> right;
    Node<T> parent;
    Color color;

    // Constructor to create a new node
    Node(T data) {
        this.data = data;
      
          // New nodes are always red when inserted
        this.color = Color.RED; 
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}

public class RedBlackTree<T extends Comparable<T>> {
    private Node<T> root;
    private final Node<T> TNULL; // Sentinel node for null references

    // Constructor to initialize the Red-Black Tree
    public RedBlackTree() {
        TNULL = new Node<>(null);
        TNULL.color = Color.BLACK;
        root = TNULL;
    }

        // Preorder traversal helper function that appends to a StringBuilder
    private void preOrderHelper(Node<T> node, StringBuilder sb) {
        if (node != TNULL) {
            sb.append(node.data).append(" ");  // Append node value
            preOrderHelper(node.left, sb);
            preOrderHelper(node.right, sb);
        } else {
            sb.append("N ");  // Append "N" for null to capture structure
        }
    }

    // Function to return preorder traversal as a string
    public String preOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        preOrderHelper(this.root, sb);
        return sb.toString().trim(); // Convert to a string and remove trailing space
    }


    // Function to perform left rotation
    private void leftRotate(Node<T> x) {
        Node<T> y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Function to perform right rotation
    private void rightRotate(Node<T> x) {
        Node<T> y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Function to insert a new node
    public void insert(T key) {
        Node<T> node = new Node<>(key);
        node.parent = null;
        node.left = TNULL;
        node.right = TNULL;
        node.color = Color.RED; // New node must be red

        Node<T> y = null;
        Node<T> x = this.root;

        // Find the correct position to insert the new node
        while (x != TNULL) {
            y = x;
            if (node.data.compareTo(x.data) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data.compareTo(y.data) < 0) {
            y.left = node;
        } else {
            y.right = node;
        }

        // Fix the tree if the properties are violated
        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    // Function to fix violations after insertion
    private void fixInsert(Node<T> k) {
        Node<T> u;
        while (k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;

                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = Color.BLACK;
    }

    // Function to generate all permutations
    public static void generatePermutations(int[] arr, int l, int r, List<int[]> permutations) {
        if (l == r) {
            permutations.add(arr.clone()); // Store a new copy of the array
        } else {
            for (int i = l; i <= r; i++) {
                swap(arr, l, i);
                generatePermutations(arr, l + 1, r, permutations);
                swap(arr, l, i); // Backtrack
            }
        }
    }

    // Helper function to swap elements
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
    // Generate all 120 permutations of {1,2,3,4,5}
    List<int[]> permutations = new ArrayList<>();
    int[] keys = {1, 2, 3, 4, 5};
    generatePermutations(keys, 0, keys.length - 1, permutations);

    Set<String> uniqueTrees = new HashSet<>();

    // Generate RBTs
    for (int[] perm : permutations) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        
        for (int key : perm) {
            tree.insert(key);
        }

        // Use preorder traversal as a unique identifier
        String treeStructure = tree.preOrderTraversal();
        uniqueTrees.add(treeStructure);
    }

    System.out.println("Unique Red-Black Trees Created: " + uniqueTrees.size());

        // Print the structures
        for (String structure : uniqueTrees) {
            System.out.println(structure);
        }
    }
}
