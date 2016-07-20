package yanjiacheng.com.mobilesafe2.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 出来流
 * Created by yan on 2016/7/18.
 */
public class StreamUtil {
    /**
     * 流转换成字符串
     * @param is 流对象
     * @return 流转换成的字符串  返回null代表异常
     */
    public static String streamToString(InputStream is) {
       //1.在读取的过程中，将读取的内容存储在缓冲中，最后一次性转换成字符串返回
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
       // 2.读取流的操作，（循环读尽）
        byte[] buffer=new byte[1024];
        //3.记录读取内容的临时变量
        int temp=-1;
        try {
            while((temp=is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            //4.返回读取的数据
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
