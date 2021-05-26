package com.ido.sstable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ido
 * @date 2020/8/31 16:31
 */
class Block {
    protected static int KEY_LEN_SIZE = 8;
    protected static int VAL_LEN_SIZE = 8;

    private String key;
    private long keyLen;
    private long valLen;
    private String val;
    /**
     * 内容在文件中的offset
     */
    private int offset;

    public int getOffset() {
        return offset;
    }

    public int getBlockLength() {
        int velSize = this.getVal().getBytes().length;
        int keySize = this.getKey().getBytes().length;
        return velSize + keySize + KEY_LEN_SIZE + VAL_LEN_SIZE;

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Block(String key, String val) {
        this.key = key;
        this.val = val;
        this.keyLen = key.getBytes().length;
        this.valLen = val.getBytes().length;

    }

    Block() {
    }

    /**
     * 将一个block变成 bytes
     *
     * @return
     */
    public byte[] bytes() {
        int velSize = this.val.getBytes().length;
        int keySize = this.key.getBytes().length;
        ByteBuffer bf = ByteBuffer.allocate(KEY_LEN_SIZE + VAL_LEN_SIZE + velSize + keySize);
        bf.putLong(this.keyLen);
        bf.putLong(this.valLen);
        bf.put(this.key.getBytes());
        bf.put(this.val.getBytes());
        return bf.array();
    }

    /**
     * @param fileData 整个文件的data
     * @return
     */
    static List<Block> read(byte[] fileData) {
        ByteBuffer fileBf = ByteBuffer.allocate(fileData.length);
        fileBf.put(fileData);
        fileBf.rewind();

        List<Block> blocks = new ArrayList<>();
        while (true) {
            int offset = fileBf.position();
            int keySize = (int) fileBf.getLong();
            int valSize = (int) fileBf.getLong();
            Block b = getBlock(fileBf, keySize, valSize, offset);
            blocks.add(b);
            if (fileBf.remaining() < (KEY_LEN_SIZE + VAL_LEN_SIZE)) {
                break;
            }
        }


        return blocks;

    }

    private static Block getBlock(ByteBuffer fileBf, int keySize, int valSize, int offset) {
        byte[] key = new byte[keySize];//获取到key 的 offset
        byte[] val = new byte[valSize];//获取到val 的 offset
        fileBf.get(key);
        fileBf.get(val);


        return toBlock(keySize, valSize, key, val, offset);
    }

    private static Block toBlock(int klen, int vlen, byte[] key, byte[] val, int offset) {
        Block b = new Block();
        b.keyLen = klen;
        b.valLen = vlen;
        b.key = new String(key);
        b.val = new String(val);
        b.offset = offset;
        return b;
    }


}
