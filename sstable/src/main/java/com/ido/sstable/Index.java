package com.ido.sstable;

/**
 * @author Ido
 * @date 2020/9/1 13:23
 */
public interface Index {

    /**
     * 索引内容
     */
    void index(String dataPath);

    /**
     * 获取data 在数据文件中的off set
     *
     * @param key
     * @return
     */
    DataOffSetRange getDataOffset(String key);


    Block search(String key);

}
