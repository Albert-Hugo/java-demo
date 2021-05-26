package com.example.bloom;

import java.io.*;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

/**
 * @author Ido
 * @date 2020/8/21 9:44
 */
public class BloomFilter {
    private BitSet bitSet = new BitSet();

    public static class BitMap {
        private byte[] bytes;

        public void set(int pos) {
            int i = pos / 7;
            int d = pos % 7;

            byte b = bytes[i];
            bytes[i] = setBit(b,d);

        }


    }

    public static void printBitmap(BitMap bitMap){
        StringBuilder sb = new StringBuilder();
        for (byte b : bitMap.bytes){
//            sb.append(BitSet)
        }
        System.out.println(bitMap.bytes);

    }

    public boolean contains(Object data) {
        return bitSet.get(hash1(data)) && bitSet.get(hash2(data)) && bitSet.get(hash3(data));
    }

    public static void main(String[] args) throws IOException {
        BitSet bitSet = new BitSet(1);
        bitSet.set(Integer.MAX_VALUE,true);
        bitSet.set(1,true);
        bitSet.set(111,true);
        System.out.println(bitSet.get(5));;
        System.out.println(bitSet);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectInputStream ois = new ObjectInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(bitSet);
        System.out.println( (out.toByteArray().length/(1024*1024)));

    }

    public static byte setBit(byte b, int i) {
        return (byte) (b | 1<<i-1);
    }

    public void add(Object data) {
        setIndex(hash1(data));
        setIndex(hash2(data));
        setIndex(hash3(data));
    }

    private void setIndex(int i) {
        bitSet.set(i);
    }


    private int hash1(Object o) {
        int hash = 1677;

        hash = (hash * 31) + o.hashCode();
        hash = (hash * 31) + o.hashCode();
        hash = (hash * 31) + o.hashCode();
        hash = hash & (1 << (bitSet.size() - 1));
        return Math.abs(hash);

    }

    private int hash2(Object o) {
        int hash = 67;

        hash = (hash * 113) + o.hashCode();
        hash = (hash * 113) + o.hashCode();
        hash = (hash * 113) + o.hashCode();
        hash = hash & (1 << (bitSet.size() - 1));
        return Math.abs(hash);

    }

    private int hash3(Object o) {
        int hash = 6079;

        hash = (hash * 13421) + o.hashCode();
        hash = (hash * 13421) + o.hashCode();
        hash = (hash * 13421) + o.hashCode();
        hash = hash & (1 << (bitSet.size() - 1));
        return Math.abs(hash);

    }


}
