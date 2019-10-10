/**
 * @author louisliu
 */

package site.moku.printassistant.utils;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Tools {

    public static String encodeBase64File(File file) {
        byte[] buffer = null;
        try (FileInputStream inputFile = new FileInputStream(file)) {
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(buffer);

    }
}
