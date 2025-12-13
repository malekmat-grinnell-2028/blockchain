package edu.grinnell.csc207.blockchain;

import java.io.IOException;
import java.nio.ByteBuffer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A single block of a blockchain.
 */
public class Block {
    private int number;
    private int data;
    private Hash prev;
    private long nonce;
    private Hash hash;

    /**
     * creates a new Block
     * 
     * @param num    index of block
     * @param amount transaction amount
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public Block(int num, int amount) throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = null;
        calculateNonceAndHash();
    }

    /**
     * creates a new Block
     * 
     * @param num      index of block
     * @param amount   transaction amount
     * @param prevHash hash of previous Block
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = prevHash;
        calculateNonceAndHash();
    }

    /**
     * creates a new Block
     * 
     * @param num      index of block
     * @param amount   transaction amount
     * @param prevHash hash of previous Block
     * @param nonce    the nonce of the Block
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public Block(int num, int amount, Hash prevHash, long nonce) 
        throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = prevHash;
        this.nonce = nonce;
    }

    /**
     * gets the index number of Block
     * 
     * @return number of Block
     */
    public int getNum() {
        return number;
    }

    /**
     * gets transaction amount of Block
     * 
     * @return transaction amount of Block
     */
    public int getAmount() {
        return data;
    }

    /**
     * gets Nonce of Block
     * 
     * @return nonce of Block
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * gets the previos blocks hash
     * 
     * @return previous blocks hash
     */
    public Hash getPrevHash() {
        return prev;
    }

    /**
     * gets hash of block
     * 
     * @return hash of block
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * converts char to int
     * 
     * @param ch char to be converted
     * @return int value of converted char
     */
    private int charToint(char ch) {
        int num = (int) ch;
        if (num > 57) {
            num -= 55;
        } else {
            num -= 48;
        }
        return num;
    }

    /**
     * adds hash to ByteBuffer
     * 
     * @param buf  the ByteBuffer to add hash to
     * @param hash the hash to add
     */
    public void addHashStringToBuffer(ByteBuffer buf, Hash hash) {
        String str = hash.toString();
        for (int i = 0; i < prev.sizeOf(); i++) {
            String hexVal = str.substring(i * 2, (i * 2) + 2);
            int num1 = charToint(hexVal.charAt(0));
            int num2 = charToint(hexVal.charAt(1));
            int total = (num1 * 16) + num2;
            buf.putInt(total);
        }
    }

    /**
     * finds and sets nonce value of block without the previous hash
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void getValidNonceWithoutPrevHash() throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer newBuf = ByteBuffer.allocate(24);
        for (long num = 0; num < 9223372036854775807L; num++) {
            newBuf.putInt(number);
            md.update(newBuf.array());
            newBuf.putInt(data);
            md.update(newBuf.array());
            newBuf.putLong(num);
            md.update(newBuf.array());
            Hash newhash = new Hash(md.digest());
            if (newhash.isValid()) {
                hash = newhash;
                nonce = num;
                return;
            }
            newBuf.clear();
            md.reset();
        }
    }

    /**
     * finds and sets the valid nonce for the block
     * 
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void getValidNonce() throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer newBuf = ByteBuffer.allocate(prev.sizeOf() * 16 + 16);
        addHashStringToBuffer(newBuf, prev);
        for (long num = 0; num < 9223372036854775807L; num++) {
            newBuf.putInt(number);
            md.update(newBuf.array());
            newBuf.putInt(data);
            md.update(newBuf.array());
            newBuf.putLong(num);
            md.update(newBuf.array());
            Hash newhash = new Hash(md.digest());
            if (newhash.isValid()) {
                hash = newhash;
                nonce = num;
                return;
            }
            newBuf.clear();
            md.reset();
        }
    }

    /**
     * gets the hash value for a block with a given Nonce
     * 
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void getHashGivenNonce() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer buf = ByteBuffer.allocate(prev.sizeOf() * 16 + 16);
        buf.putInt(number);
        buf.putInt(data);
        addHashStringToBuffer(buf, prev);
        buf.putLong(nonce);
        md.update(buf);
        byte[] arr = md.digest();
        hash = new Hash(arr);
    }

    /**
     * calculates and sets the Nonce and hash for a block
     * 
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void calculateNonceAndHash() throws NoSuchAlgorithmException, IOException {
        // sizeOf(int num) + sizeOf(int data) + sizeOf(long nonce)

        if (prev != null) {
            getValidNonce();
        } else {
            // sizeOf(int num) + sizeOf(int data) + sizeOf(long nonce)
            getValidNonceWithoutPrevHash();
        }
    }

    /**
     * toString method for Block
     * 
     * @return the Block as a string
     */
    @Override
    public String toString() {
        if (prev != null) {
            String str = String.format("Block %d (Amount: %d, Nonce %d, prevHash: %s)",
                    number, data, nonce, prev.toString());
            return str;
        }
        String str = String.format("Block %d (Amount: %d, Nonce %d, prevHash: null)",
                number, data, nonce);
        return str;

    }
}
