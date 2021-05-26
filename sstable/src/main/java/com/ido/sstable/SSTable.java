package com.ido.sstable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ido
 * @date 2020/8/31 16:13
 */
public class SSTable {

    private SegmentFile segmentFile;
    /**
     * index file 的内容映射
     */
    private Map<String, String> indexMemTable = new ConcurrentHashMap<>();

    public SSTable() throws IOException {
        this.segmentFile = new SegmentFile("data.seg");
    }

    /**
     * 将内容按照key 在内容中排序之后，保存到file 中
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        segmentFile.put(key, value);
        byte[] content = segmentFile.read();
        mapIntoMemTable(content);
        this.segmentFile.write();
        indexMemTable.put(key, value);

    }

    private void mapIntoMemTable(byte[] content) {

    }

    /**
     * 获取文件中的内容呢，映射到memtable中之后，直接获取
     *
     * @param k
     * @return
     */
    public String get(String k) {
        return indexMemTable.get(k);
    }

}
