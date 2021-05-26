package com.ido.sstable;


import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Ido
 * @date 2020/8/31 17:29
 */
public class TestByteBuf {
    public static void main(String[] args) throws UnsupportedEncodingException {
        int len = "dsaa".getBytes().length;
        ByteBuffer bf = ByteBuffer.allocate(len);
        bf.put("dsaa".getBytes());
        byte[] bs = new byte[len];
        bf.rewind();
        bf.get(bs,0,len);
        System.out.println(new String(bs, Charset.defaultCharset()));
    }

    @Test
    public void test(){
        byte[][] param = new byte[][]{"1".getBytes(),"2".getBytes(),"3".getBytes(),"4".getBytes()};
        String sql = "UPDATE c_customer  SET LAST_UPDATE_DT=?,\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "AREA_CODE=?,\n" +
                "PHONE=?  WHERE ID=?   AND DELETED=0";
        for(byte[] p : param){
            sql = sql.replaceFirst("\\?",new String(p));
        }
        System.out.println(sql);
    }

    @Test
    public void test1(){
        String updateSql = "update t set v1 = 2 where param=1\n" ;
        int start = updateSql.indexOf("update");
        int end = updateSql.indexOf("set");
        String tableName = updateSql.substring(start+"update".length(),end);
        int whereIdx = updateSql.indexOf("where");
        String whereCluse = updateSql.substring(whereIdx);

        System.out.println(tableName);
        System.out.println(whereCluse);

        String fs = new File("D:\\workspace\\demo\\luancher\\src\\main\\java\\com\\baeldung\\instrumentation\\application\\MyAtm.java").getParent();
        System.out.println(fs);

    }


    @Test
    public void testCon(){
         NavigableMap<String, String> indexTable = new ConcurrentSkipListMap<>();

        indexTable.put("3","dsfagsdfa");
        indexTable.put("2","dsfagsdfa");
        indexTable.put("1","dsfagsdfa");
        indexTable.put("4","dsfagsdfa");
        indexTable.put("5","dsfagsdfa");
        indexTable.put("6","dsfagsdfa");
        indexTable.put("7","dsfagsdfa");
        System.out.println(indexTable.lowerEntry("3").getKey());;
        System.out.println(indexTable.higherKey("3"));;


    }
}
