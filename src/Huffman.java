/**
 * This class creates an encoding of an input and decodes it back
 * to its initial message.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Huffman {
    public static void main(String[] args) {
        System.out.println("Huffman Coding");
        ArrayList<BinaryTree<Pair>> pairs = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        System.out.print("Enter the name of the file with letters and probability: ");
        String fileName = in.nextLine();
        File inFile = new File(fileName);
        Scanner input = null;

        // Step 1: taking input from text file
        try {
            input = new Scanner(inFile);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split("\t");
                char value = parts[0].charAt(0);
                double prob = Double.parseDouble(parts[1]);

                // making a tree for each pair
                BinaryTree<Pair> pair = new BinaryTree<>();
                pair.setData(new Pair(value, prob));
                pairs.add(pair);
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found.");
        }

        // Step 3: making the Huffman tree
        System.out.println("\nBuilding the Huffman tree ….");
        BinaryTree<Pair> temp;
        // sorting by probabilities
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (pairs.get(j).getData().compareTo(pairs.get(j + 1).getData()) > 0) {
                    temp = pairs.get(j);
                    pairs.set(j, pairs.get(j + 1));
                    pairs.set(j + 1, temp);
                }
            }
        }

        // creating queues
        Queue<BinaryTree<Pair>> S = new LinkedList<>(pairs);
        Queue<BinaryTree<Pair>> T = new LinkedList<>();

        // rest of Huffman algorithm
        while (!S.isEmpty()) {
            // part 1: pick two smallest weight trees
            BinaryTree<Pair> A;
            BinaryTree<Pair> B;
            if (T.isEmpty()) {
                A = S.poll();
                B = S.poll();
            } else {
                if (S.peek().getData().getProb() < T.peek().getData().getProb()) {
                    A = S.poll();
                } else {
                    A = T.poll();
                }
                if (S.isEmpty()) {
                    B = T.poll();
                } else if (T.isEmpty()) {
                    B = S.poll();
                } else {
                    if (S.peek().getData().getProb() < T.peek().getData().getProb()) {
                        B = S.poll();
                    } else {
                        B = T.poll();
                    }
                }
            }

            // parts 2-4: updating new tree
            BinaryTree<Pair> P = new BinaryTree<>();
            constructNewTree(P, A, B);
            T.add(P);
        }

        // part 5: getting queue size to 1
        if (T.size() > 1) {
            while (T.size() != 1) {
                // repeating part 2
                BinaryTree<Pair> A = T.poll();
                BinaryTree<Pair> B = T.poll();
                BinaryTree<Pair> P = new BinaryTree<>();
                constructNewTree(P, A, B);
                T.add(P);
            }
        }
        BinaryTree<Pair> huffmanTree = T.peek();
        System.out.println("Huffman coding completed.\n");

        // Step 4: encoding and decoding
        // taking in input
        System.out.print("Enter a line of text (uppercase letters only): ");
        String phrase = in.nextLine();
        String[] codes = findEncoding(huffmanTree);

        // encoding to binary
        String encoded = "";
        for (char character : phrase.toCharArray()) {
            if (character == ' ') {
                encoded += " ";
            }
            else {
                encoded += codes[character - 'A'];
            }
        }
        System.out.println("Here's the encoded line: " + encoded);

        // decoding back
        String decoded = "";
        BinaryTree<Pair> current = huffmanTree;
        for (char character : encoded.toCharArray()) {
            if (character == ' ') {
                decoded += " ";
            }
            else {
                if (character == '0') {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                if (current.getLeft() == null && current.getRight() == null) {
                    decoded += current.getData().getValue();
                    current = huffmanTree;
                }
            }
        }
        System.out.println("The decoded line is: " + decoded);
    }

    /**
     * Method to make new tree from other trees
     * @param P     new empty tree
     * @param A     first intended subtree
     * @param B     second intended subtree
     */
    private static void constructNewTree(BinaryTree<Pair> P, BinaryTree<Pair> A, BinaryTree<Pair> B) {
        P.setLeft(A);
        P.setRight(B);
        P.makeRoot(new Pair('0', A.getData().getProb() + B.getData().getProb()));
    }

    /**
     * Method to make codes for all the letters needed
     * @param bt    Binary tree
     * @return      array of codes
     */
    private static String[] findEncoding(BinaryTree<Pair> bt) {
        String[] result = new String[26];
        findEncoding(bt, result, "");
        return result;
    }

    /**
     * Recursive method to translate to binary
     * @param bt    Binary tree
     * @param a     String array
     * @param prefix     prefix
     */
    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix) {
        // test is node/tree is a leaf
        if (bt.getLeft()==null && bt.getRight()==null) {
            a[bt.getData().getValue() - 65] = prefix;
        }
        // recursive calls
        else {
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }

}