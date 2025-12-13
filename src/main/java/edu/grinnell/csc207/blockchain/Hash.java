package edu.grinnell.csc207.blockchain;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {
    private byte[] data;

    /**
     * constructs new hash
     * 
     * @param data
     */
    public Hash(byte[] data) {
        this.data = data;
    }

    /**
     * gets the data from the hash
     * 
     * @return data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * checks whether the hash is valid
     * 
     * @return true if valid, false if not
     */
    public boolean isValid() {
        if (data.length < 3) {
            return false;
        }
        if ((Byte.toUnsignedInt(data[0]) == 0)
                && (Byte.toUnsignedInt(data[1]) == 0)
                && (Byte.toUnsignedInt(data[2]) == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Helper for toString, makes the hex work
     * 
     * @param num
     * @param index
     * @param data
     */
    private void setHexAtIndex(int num, int index, char[] data) {
        num = num % 16;
        if (num < 10) {
            data[index] = (char) (num + 48);
        } else {
            data[index] = (char) ((num - 10) + 65);
        }
    }

    /**
     * toString method
     * 
     * @return the hash as a string
     */
    public String toString() {
        char[] chars = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            int num = Byte.toUnsignedInt(data[i]);
            setHexAtIndex(num / 16, i * 2, chars);
            setHexAtIndex(num, (i * 2) + 1, chars);
        }
        String str = new String(chars);
        return str;
    }

    /**
     * gets the size of the hash
     * 
     * @return the size of the hash
     */
    public int sizeOf() {
        return data.length;
    }

    /**
     * checks whether to hashes are equal
     * 
     * @param other the hash being compared to
     * @return true if the hashes are equal, false if not
     */
    public boolean equals(Object other) {
        if (other instanceof Hash) {
            Hash o = (Hash) other;
            return o.toString().equals(toString());
        }
        return false;
    }

}
