package edu.grinnell.csc207.blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * A linked list of hash-consistent blocks representing a ledger of
 * monetary transactions.
 */
public class BlockChain {
    // TODO: fill me in!

    private class Node {
        Block value;
        Node next;

        public Node(Block value) {
            this.value = value;
            next = null;
        }
    }

    private Node first;
    private Node last;
    private int size = 0;

    /**
     * creates a new blockchain
     * 
     * @param initial
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public BlockChain(int initial) throws NoSuchAlgorithmException, IOException {
        size++;
        Block b = new Block(size, initial);
        first = new Node(b);
        last = first;
    }

    /**
     * mines a new block from the blockchain
     * 
     * @param amount
     * @return the mined block
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public Block mine(int amount) throws NoSuchAlgorithmException, IOException {
        Block b = new Block(size + 1, amount, last.value.getHash());
        return b;
    }

    /**
     * gets the size of the blockchain
     * 
     * @return the size of the blockchain
     */
    public int getSize() {
        return size;
    }

    /**
     * appends the given block to the blockchain
     * 
     * @param b the block to be appended
     */
    public void append(Block b) {
        last.next = new Node(b);
        last = new Node(b);
        size++;
        if (!this.isValidBlockChain()) {
            this.removeLast();
        }
    }

    /**
     * gets the hash value of the last node
     * 
     * @return the hash of last node
     */
    public Hash getHash() {
        return last.value.getHash();
    }

    /**
     * checks whether the blockchain is valid
     * 
     * @return true if valid, false if not
     */
    public boolean isValidBlockChain() {
        Node currNode = first;
        while (currNode.next != null) {
            String str1 = currNode.value.getHash().toString();
            String str2 = currNode.next.value.getPrevHash().toString();
            if (!str1.equals(str2)) {
                return false;
            }
            currNode = currNode.next;
        }
        return true;
    }

    /**
     * helper for the printBalances function
     * 
     * @return the balances of Alice and Bob
     */
    public String printBalancesHelper() {
        Node currNode = first;
        int balanceAlice = currNode.value.getAmount();
        int balanceBob = 0;
        currNode = currNode.next;
        while (currNode != null) {
            int amount = currNode.value.getAmount();
            if (amount <= 0) {
                balanceAlice += amount;
                balanceBob -= amount;
            } else {
                balanceAlice -= amount;
                balanceBob += amount;
            }
            currNode = currNode.next;
        }
        String str = "Alice: " + balanceAlice + ", Bob: " + balanceBob;
        return str;
    }

    /**
     * prints the balances of Alice and Bob
     */
    public void printBalances() {
        System.out.println(printBalancesHelper());
    }

    /**
     * removes the last block in the blockchain
     * 
     * @return true if the blockchain has more than 1 block and a block is removed,
     *         false otherwise
     */
    public boolean removeLast() {
        if (size == 1) {
            return false;
        }
        last = null;
        Node currNode = first;
        while (currNode.next != null) {
            currNode = currNode.next;
        }
        last = currNode;
        size--;
        return true;
    }

    /**
     * toString method for blockChain
     * 
     * @return the blockChain as a string
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Node currNode = first;
        while (currNode != null) {
            buf.append(currNode.value.toString());
            if (currNode.next != null) {
                buf.append("\n");
            }
            currNode = currNode.next;
        }
        return buf.toString();
    }

}
