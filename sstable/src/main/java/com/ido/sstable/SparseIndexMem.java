package com.ido.sstable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ido
 * @date 2020/9/1 10:44
 */
public class SparseIndexMem implements Index {
    /* 根据内容文件中的大小，拆分索引的因子*/
    private int index_sparse_factor = 128;
    private TreeMap<String, Block> indexTable = new TreeMap<>();
    private SegmentFile segmentFile;

    public SparseIndexMem(SegmentFile segmentFile) {
        this.segmentFile = segmentFile;

        this.index(segmentFile.getName());
    }

    @Override
    public void index(String dataPath) {
        try {
//            File f = new File(dataPath);
//            MappedByteBuffer mbp = new FileInputStream(f).getChannel()
//                    .map(FileChannel.MapMode.READ_ONLY, 0, f.length());
            //将文件中的内容，按照一定的拆分逻辑，拆分变成内存映射
            List<Block> bls = new SegmentFile(dataPath).getBlockList();
            int idxIncreator = 4;
            idxIncreator = Math.max(idxIncreator, bls.size() / index_sparse_factor);

            for (int i = 0; i < idxIncreator; i ++) {
                Block b = bls.get(i * idxIncreator);
                indexTable.put(b.getKey(), b);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public DataOffSetRange getDataOffset(String key) {
        Block b = indexTable.get(key);
        if (b == null) {
            RangeDataInner rangeDataInner = new RangeDataInner(key).invoke();
            int begin = rangeDataInner.getBegin();
            int end = rangeDataInner.getEnd();
            DataOffSetRange range = new DataOffSetRange();
            range.start = begin;
            range.end = end;
            return range;


        }
        DataOffSetRange range = new DataOffSetRange();
        range.start = b.getOffset();
        range.end = b.getOffset() + b.getBlockLength();
        return range;
    }

    @Override
    public Block search(String key) {
        //todo 索引先偏序，然后获取key 中的两个 值的offset之间的值，在data file中查找
        Block b = indexTable.get(key);
        if (b == null) {
            RangeDataInner rangeDataInner = new RangeDataInner(key).invoke();
            int begin = rangeDataInner.getBegin();
            int end = rangeDataInner.getEnd();

            String fileName = segmentFile.getName();
            File f = new File(fileName);
            try {
                MappedByteBuffer mbp = new FileInputStream(f).getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, begin, end - begin);
                byte[] data = new byte[end - begin];

                mbp.get(data);

                List<Block> targetBlocksInRange = Block.read(data);

                for (Block bInRange : targetBlocksInRange) {
                    if (bInRange.getKey().equals(key)) {
                        return bInRange;
                    }
                }
                return null;


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return b;
    }

    private class RangeDataInner {
        private String key;
        private int begin;
        private int end;

        public RangeDataInner(String key) {
            this.key = key;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }

        public RangeDataInner invoke() {
            Map.Entry<String, Block> lbEn = indexTable.lowerEntry(key);
            Map.Entry<String, Block> hbEn = indexTable.higherEntry(key);
            //文件内容中的查找offset 的开始和结束位置
            begin = lbEn.getValue().getOffset();
            end = hbEn.getValue().getOffset() + hbEn.getValue().getBlockLength();
            return this;
        }
    }
}
