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

    public Block(int num, int amount) throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = null;
        calculateNonceAndHash();
    }

    public Block(int num, int amount, Hash prevHash) throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = prevHash;
        calculateNonceAndHash();
    }

    public Block(int num, int amount, Hash prevHash, long nonce) throws NoSuchAlgorithmException, IOException {
        number = num;
        data = amount;
        prev = prevHash;
        this.nonce = nonce;
        getHashGivenNonce();
    }

    public int getNum() {
        return number;
    }

    public int getAmount() {
        return data;
    }
    
    public long getNonce() {
        return nonce;
    }

    public Hash getPrevHash() {
        return prev;
    }

    public Hash getHash() {
        return hash;
    }

    private int charToint(char ch) {
        int num = (int) ch;
        if(num > 57) {
            num -= 55;
        } else {
            num -= 48;
        }
        return num;
    }

    public void addHashStringToBuffer(ByteBuffer buf, Hash hash) {
        String str = hash.toString();
        for(int i=0; i<prev.sizeOf(); i++) {
            String hexVal = str.substring(i*2,(i*2)+2);
            int num1 = charToint(hexVal.charAt(0));
            int num2 = charToint(hexVal.charAt(1));
            int total = (num1*16) + num2;
            buf.putInt(total);
        }
    }

    public void getValidNonceWithoutPrevHash() throws IOException, NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer newBuf = ByteBuffer.allocate(24);
        for(long num = 0; num < 9223372036854775807L ; num++) {
            newBuf.putInt(number);
            md.update(newBuf.array());
            newBuf.putInt(data);
            md.update(newBuf.array());
            newBuf.putLong(num);
            md.update(newBuf.array());
            Hash newhash = new Hash(md.digest());
            if(newhash.isValid()) {
                hash = newhash;
                nonce = num;
                return;
            }
            newBuf.clear();
            md.reset();
        }
    }

    //

    public void getValidNonce() throws IOException, NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer newBuf = ByteBuffer.allocate(prev.sizeOf()*16 + 16);
        addHashStringToBuffer(newBuf, prev);
        for(long num = 0; num < 9223372036854775807L ; num++) {
            newBuf.putInt(number);
            md.update(newBuf.array());
            newBuf.putInt(data);
            md.update(newBuf.array());
            newBuf.putLong(num);
            md.update(newBuf.array());
            Hash newhash = new Hash(md.digest());
            if(newhash.isValid()) {
                hash = newhash;
                nonce = num;
                return;
            }
            newBuf.clear();
            md.reset();
        }
    }

    public void getHashGivenNonce() throws NoSuchAlgorithmException, IOException{
        MessageDigest md = MessageDigest.getInstance("sha-256");
        ByteBuffer buf = ByteBuffer.allocate(prev.sizeOf()*16 + 16);
        buf.putInt(number);
        buf.putInt(data);
        addHashStringToBuffer(buf, prev);
        buf.putLong(nonce);
        md.update(buf);
        byte[] arr = md.digest();
        hash = new Hash(arr);
    }

    public void calculateNonceAndHash() throws NoSuchAlgorithmException, IOException{
        // sizeOf(int num) + sizeOf(int data) + sizeOf(long nonce)
        
        if(prev != null) {
            getValidNonce();
        } else {
            // sizeOf(int num) + sizeOf(int data) + sizeOf(long nonce)
            getValidNonceWithoutPrevHash();
        }
    }

}
