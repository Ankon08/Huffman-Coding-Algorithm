/**
 * Huffman Coding Implementation: This program reads character probabilities from "LettersProbability.txt," constructs
 * a Huffman tree, and encodes/decodes input text using the tree. The program assumes UPPERCASE LETTERS in the input text.
 * After reading the probability file, it constructs the tree using priority queues, encodes the user-input text, and
 * displays the encoded and decoded lines. Each character's encoding is based on its frequency in the probability file.
 *
 * @author - Ankon Biswas
 *
 */

import java.io.*;
import java.util.*;

public class Huffman {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("HUFFMAN CODING ~ THE GREEDY ALGORITHM!");
        // Prompt user for the filename
        System.out.print("Enter the filename ('src/LettersProbability.txt') to read from: ");
        String filename = in.nextLine();

        System.out.println("Building the Huffman tree....");
        System.out.println("Huffman coding completed!!!");

        // Create a File object and Scanner to read the file
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);
        String[] fileContent;

        // Create queues for trees S and T
        Queue<BinaryTree<Pair>> queueS = new LinkedList<>();
        Queue<BinaryTree<Pair>> queueT = new LinkedList<>();

        // Read probability values from the file and create initial trees
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            fileContent = line.split("\t");

            if (fileContent.length == 2) {
                // Extract character and probability from each line
                char value = fileContent[0].charAt(0);
                double probability = Double.parseDouble(fileContent[1]);

                // Create a new tree node and add it to queue S
                BinaryTree<Pair> temp = new BinaryTree<Pair>();
                temp.makeRoot(new Pair(value, probability));
                queueS.add(temp);
            } else {
                // Handle invalid lines or provide an error message
                System.out.println("Invalid line: " + line);
            }
        }

        // Declare variables for tree nodes
        BinaryTree<Pair> A;
        BinaryTree<Pair> B;
        BinaryTree<Pair> first;
        BinaryTree<Pair> second;
        BinaryTree<Pair> finalHuffmanTree = new BinaryTree<>();

        // Build the Huffman tree using the algorithm
        while (!queueS.isEmpty()) {
            // Get the next nodes A and B
            A = getNextNode(queueS, queueT);
            B = getNextNode(queueS, queueT);

            // Create a new tree node P, set it as the root, and attach A and B
            BinaryTree<Pair> P = new BinaryTree<>();
            P.makeRoot(new Pair('0', A.getData().getProb() + B.getData().getProb()));
            P.attachLeft(A);
            P.attachRight(B);

            // Add P to queue T
            queueT.add(P);
        }

        // Merge trees until a single tree remains
        while (queueT.size() > 1) {
            List<BinaryTree<Pair>> trees = new ArrayList<>();

            // Poll two trees from queue T, create a new tree, and add it to the list
            while (queueT.size() >= 2) {
                first = queueT.poll();
                second = queueT.poll();

                BinaryTree<Pair> newTree = new BinaryTree<>();
                newTree.makeRoot(new Pair('0', first.getData().getProb() + second.getData().getProb()));
                newTree.attachLeft(first);
                newTree.attachRight(second);

                trees.add(newTree);
            }

            // Add all new trees back to queue T
            queueT.addAll(trees);
        }

        // The final Huffman tree is the remaining tree in queue T
        finalHuffmanTree = queueT.poll();

        // Get user input for encoding and decoding
        System.out.print("Enter a line (UPPERCASE LETTERS ONLY): ");
        String test = in.nextLine();
        String[] encoded = findEncoding(finalHuffmanTree);

        // Encode the input line
        String encodedLine = displayEncodedLine(test, encoded);

        System.out.println("Here's the encoded line: " + encodedLine);

        // Decode the encoded line
        String decodedLine = displayDecodedLine(encodedLine, finalHuffmanTree);

        System.out.println("The decoded line is: " + decodedLine);

        // Close the file and scanner
        inputFile.close();
        in.close();
    }

    /**
     * Gets the next node from the queues.
     *
     * @param queueS The source queue.
     * @param queueT The target queue.
     * @return The next node.
     */
    private static BinaryTree<Pair> getNextNode(Queue<BinaryTree<Pair>> queueS, Queue<BinaryTree<Pair>> queueT) {
        BinaryTree<Pair> node;
        // Check if queueT is not empty and its front node has a lower or equal
        // probability than the front node of queueS
        if (!queueT.isEmpty() && (compareNodes(queueT.peek(), queueS.peek()) <= 0)) {
            // Poll the front node from queueT
            node = queueT.poll();
        } else {
            // Poll the front node from queueS
            node = queueS.poll();
        }
        return node;
    }

    /**
     * Compares two nodes based on their probabilities.
     *
     * @param node1 The first node.
     * @param node2 The second node.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     */
    private static int compareNodes(BinaryTree<Pair> node1, BinaryTree<Pair> node2) {
        return Double.compare(node1.getData().getProb(), node2.getData().getProb());
    }

    /**
     * Displays the encoded line based on the provided encoding array.
     *
     * @param line          The original line to encode.
     * @param encodedString The array containing Huffman encoding for each uppercase letter.
     * @return The displayed encoded line.
     */
    public static String displayEncodedLine(String line, String[] encodedString) {
        // Variable to store the displayed encoded line
        String displayEncoded = "";

        // Iterate through each character in the original line
        for (char c : line.toCharArray()) {
            // Check if the character is an uppercase letter
            if (Character.isUpperCase(c)) {
                // Index of the character in the encoding array
                int i = c - 'A';
                // Append the Huffman encoding for the uppercase letter
                displayEncoded += encodedString[i];
            } else {
                // Leave spaces as they are
                displayEncoded += " ";
            }
        }

        // Return the final displayed encoded line
        return displayEncoded;
    }

    /**
     * Displays the decoded line based on the provided Huffman tree.
     *
     * @param encodedLine The encoded line to decode.
     * @param ht          The Huffman tree.
     * @return The decoded line.
     */
    public static String displayDecodedLine(String encodedLine, BinaryTree<Pair> ht) {
        // Variable to store the decoded line
        String displayDecoded = "";

        // Current node in the Huffman tree
        BinaryTree<Pair> current = ht;

        // Iterate through each bit in the encoded line
        for (char bit : encodedLine.toCharArray()) {
            // Traverse left for '0' and right for '1'
            if (bit == '0') {
                current = current.getLeft();
            } else if (bit == '1') {
                current = current.getRight();
            } else {
                // Handle space character
                displayDecoded += " ";
            }

            // Check if the current node is a leaf
            if (current.getLeft() == null && current.getRight() == null) {
                // Append the decoded character to the result
                displayDecoded += current.getData().getValue();
                // Reset to the root for the next character
                current = ht;
            }
        }
        // Return the final decoded line
        return displayDecoded;
    }

    /**
     * Finds the encoding for each leaf node in the Huffman tree.
     *
     * @param bt    The Huffman tree.
     * @return      An array of strings representing the encoding for each leaf node.
     *
     */
    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[26];
        findEncoding(bt, result, "");
        return result;
    }

    /**
     * Recursively finds the encoding for each leaf node in the Huffman tree.
     *
     * @param bt        The current node in the Huffman tree.
     * @param a         The array to store the encoding.
     * @param prefix    The current prefix.
     *
     */
    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (bt.getLeft() == null && bt.getRight() == null){
            a[bt.getData().getValue() - 65] = prefix;
        }
        // recursive calls
        else{
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }
}
