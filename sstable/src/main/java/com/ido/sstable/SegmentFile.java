package com.ido.sstable;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ido
 * @date 2020/8/31 16:15
 */
@Slf4j
public class SegmentFile {
    private int length;
    private byte[] data;
    private String name;
    private FileInputStream file;
    private boolean updated;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private List<Block> blockList = new ArrayList<>();

    public void put(String k, String v) {
        Block b = new Block(k, v);
        blockList.add(b);
    }

    public String get(String k) {
        byte[] data = read();
        List<Block> blocks = Block.read(data);

        for (Block b : blocks) {
            if (b.getKey().equals(k)) {
                return b.getVal();
            }
        }

        return null;

    }

    public String getName() {
        return name;
    }

    public SegmentFile(String name) throws IOException {
        this.name = name;
        File f = new File(name);
        if (!f.exists()) {
            if (!f.createNewFile()) {
                log.error("文件创建失败" + name);
            }
        }
        file = new FileInputStream(f);

    }


    private byte[] blockListToBytes() {
        ByteBuffer bf = ByteBuffer.allocate(128);
        blockList.sort(Comparator.comparing(Block::getKey));
        for (Block b : blockList) {
            byte[] data = b.bytes();
            if (bf.remaining() < data.length) {
                //不足，扩容
                ByteBuffer newBf = ByteBuffer.allocate(bf.capacity() * 2);
                byte[] temp = new byte[bf.position()];
                bf.rewind();
                bf.get(temp);
                bf = newBf.put(temp);
                bf.put(data);
            } else {
                bf.put(b.bytes());
            }


        }

        return bf.array();
    }

    List<Block> getBlockList() {
        return Block.read(read());
    }


    /**
     * 整个文件的内容
     */
    public void write() {
        lock.writeLock().lock();
        try (FileOutputStream fs = new FileOutputStream(this.name)) {
            this.data = blockListToBytes();
            fs.write(data);
            updated = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 获取整个文件的内容
     *
     * @return
     */
    public byte[] read() {
        if (updated) {
            return data;
        }

        lock.readLock().lock();
        try {
            this.file = new FileInputStream(this.name);
            byte[] data = new byte[this.file.available()];
            this.length = this.file.available();
            this.file.read(data);
            this.updated = true;
            this.data = data;
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }

        return null;
    }

}
