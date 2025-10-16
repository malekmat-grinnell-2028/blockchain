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

    public BlockChain(int initial) throws NoSuchAlgorithmException, IOException {
        size++;
        Block b = new Block(size, initial);
        first = new Node(b);
        last = first;
    }


    public Block mine(int amount) throws NoSuchAlgorithmException, IOException {
        Block b = new Block(size+1, amount, last.value.getHash());
        return b;
    }

    public int getSize() {
        return size;
    }

    public void append(Block b) {
        if(b.getPrevHash().toString().equals(last.value.getHash().toString())) {
            last.next = new Node(b);
            last = new Node(b);
            size++;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Hash getHash() {
        return last.value.getHash();
    }

    public boolean isValidBlockChain() {
        Node curr_node = first;
        while(curr_node.next != null) {
            if(!curr_node.value.getHash().toString().equals(curr_node.next.value.getPrevHash().toString())) {
                return false;
            }
        }
        return true;
    }

    public String printBalancesHelper() {
        Node curr_node = first;
        int balanceAlice = curr_node.value.getAmount();
        int balanceBob = 0;
        curr_node = curr_node.next;
        while(curr_node != null) {
            int amount = curr_node.value.getAmount();
            if(amount <= 0) {
                balanceAlice += amount;
                balanceBob -= amount;
            } else {
                balanceAlice -= amount;
                balanceBob += amount;
            }
            curr_node = curr_node.next;
        }
        String str = "Alice: " + balanceAlice + ", Bob: " + balanceBob;
        return str;
    }

    public void printBalances() {
        System.out.println(printBalancesHelper());
    }



    public String toString() {
        StringBuffer buf = new StringBuffer();
        Node curr_node = first;
        while(curr_node != null) {
            buf.append(curr_node.value.toString());
            if(curr_node.next != null) {
                buf.append("\n");
            }
        }
        return buf.toString();
    }

}
