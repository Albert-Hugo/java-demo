package com.ido.sstable;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Ido
 * @date 2020/9/1 10:08
 */
public class SegmentFileTest {



    @Test
    public  void testGetPut() {
        String key = null;
        String val= null;
        try {
            SegmentFile segmentFile = new SegmentFile("test.seg");
            for(int i = 0; i < 10; i++){
                String k = RandomStringUtils.randomAlphanumeric(8);
                String v = RandomStringUtils.randomAlphanumeric(20);
                if(i == 5){
                    key = k;
                    val = v;
                    System.out.println(k);
                    System.out.println(v);
                }
                segmentFile.put(k,v);
            }
            segmentFile.write();

            Assert.assertEquals(val,segmentFile.get(key));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
