/**
 * @author louisliu
 */

package site.moku.download;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestDownload {

    static final ExecutorService es = Executors.newFixedThreadPool(10);

    static void downFiles(long packageStartNumber, String fileExtension, String saveFileExtension, int fileNumbers) {
        for (int i = 0; i < fileNumbers; i++) {
            int k = i;
            Thread t = new Thread(() -> {
                InputStream fis = null;
                FileOutputStream fos = null;
                try {
                    String path = "C:\\tmp\\ahlcg\\";
                    String httpUrl = "https://arkhamdb.com/bundles/cards/";
                    long no = packageStartNumber + k + 1;
                    String noStr = String.valueOf(no).length() > 4 ? String.valueOf(no) : "0" + String.valueOf(no);
                    httpUrl += noStr + fileExtension;
                    path += noStr + saveFileExtension;
                    URL url = new URL(httpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置超时间为3秒
                    conn.setConnectTimeout(3 * 1000);
                    //防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    fis = conn.getInputStream();
                    byte[] b = new byte[1024];
                    int length = 0;
                    fos = new FileOutputStream(path);
                    while ((length = fis.read(b)) != -1) {
                        fos.write(b, 0, length);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            es.execute(t);
        }
    }

    static void downPngs(long packageStartNumber, int fileNumbers) {
        downFiles(packageStartNumber, ".png", ".png", fileNumbers);
        downFiles(packageStartNumber, "b.png", "b.png", fileNumbers);
    }

    static void downJpgs(long packageStartNumber, int fileNumbers) {
        downFiles(packageStartNumber, ".jpg", ".png", fileNumbers);
        downFiles(packageStartNumber, "b.jpg", "b.png", fileNumbers);
    }

    public static void main(String[] args) {
        long packageStartNumber = 2100L;
        int fileNumbers = 20;
        downPngs(packageStartNumber, fileNumbers);
        downJpgs(packageStartNumber, fileNumbers);

        es.shutdown();
        try {
            if (!es.awaitTermination(120, TimeUnit.SECONDS)) {
                es.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
