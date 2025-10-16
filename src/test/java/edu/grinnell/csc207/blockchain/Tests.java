package edu.grinnell.csc207.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tests {
    // TODO: fill me in with tests that you write for this project!
    
    @Test
    @DisplayName("Placeholder Test")
    public void placeholderTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    @DisplayName("hash valid Test")
    public void testHashValid() {
        byte[] arr = {(byte) 0, (byte) 0, (byte) 0, (byte) 2,(byte) 11, (byte) 17};
        Hash hash = new Hash(arr);
        assertEquals(true, hash.isValid());
    }

    @Test
    @DisplayName("hash valid Test")
    public void testHashToString() {
        byte[] arr = {(byte) 0, (byte) 0, (byte) 0, (byte) 2,(byte) 11, (byte) 17, (byte) 30, (byte) 255};
        Hash hash = new Hash(arr);
        assertEquals("000000020B111EFF", hash.toString());
    }

    @Test
    @DisplayName("hash equal to identical hash")
    public void testHashEqualToIdenticalhash() {
        byte[] arr = {(byte) 0, (byte) 0, (byte) 0, (byte) 2,(byte) 11, (byte) 17, (byte) 30, (byte) 255};
        Hash hash1 = new Hash(arr);
        Hash hash2 = new Hash(arr);

        assertEquals(true, hash1.equals(hash2));
    }

    @Test
    @DisplayName("hash not equal to different hash")
    public void testHashNotEqualToHash() {
        byte[] arr1 = {(byte) 0, (byte) 0, (byte) 0, (byte) 2,(byte) 11, (byte) 17, (byte) 30, (byte) 255};
        byte[] arr2 = {(byte) 0, (byte) 0, (byte) 0, (byte) 15,(byte) 27, (byte) 4, (byte) 1, (byte) 127};
        Hash hash1 = new Hash(arr1);
        Hash hash2 = new Hash(arr2);

        assertEquals(false, hash1.equals(hash2));
    }

    @Test
    @DisplayName("test that a block with no given nonce or prev hash generates and is valid")
    public void testBlockNoNonce() throws NoSuchAlgorithmException, IOException {
        byte[] arr = {(byte) 0, (byte) 0, (byte) 0, (byte) 2,(byte) 11, (byte) 17, (byte) 30, (byte) 255};
        Hash hash = new Hash(arr);
        Block b = new Block(0, 50, hash);

        assertEquals(true, b.getHash().isValid());
    }

    @Test
    @DisplayName("test that a block a given prev hash generates and is valid")
    public void testBlockGivenPrevHashNoNonce() throws NoSuchAlgorithmException, IOException {
        Block b = new Block(0, 50, null);

        assertEquals(true, b.getHash().isValid());
    }

    @Test
    @DisplayName("test append works for BlockChain")
    public void testBlockChainAppend() throws NoSuchAlgorithmException, IOException {
        BlockChain bChain = new BlockChain(200);
        bChain.append(bChain.mine(-100));
        assertEquals(2, bChain.getSize());
    }

    @Test
    @DisplayName("test that printBalances works for BlockChain")
    public void testBlockChainPrintBalances() throws NoSuchAlgorithmException, IOException {
        BlockChain bChain = new BlockChain(200);
        bChain.append(bChain.mine(-100));
        assertEquals("Alice: 100, Bob: 100", bChain.printBalancesHelper());
    }

}
