package com.ido.sstable;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Ido
 * @date 2020/8/31 16:13
 */
public class App {

    public static void main(String[] args) {
        try {
            SegmentFile segmentFile = new SegmentFile("test.seg");
//            segmentFile.put("123","dafagt21314");
//            segmentFile.put("af123","dafagt211123314");
//            segmentFile.put("sa22133","dafsagfagt");
//            segmentFile.put("a","3");
//            segmentFile.write();
            System.out.println(segmentFile.get("a"));
            System.out.println(segmentFile.get("123"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
