/**
 * @author louisliu
 */

package site.moku.printassistant.print.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
        return Base64.encode(buffer);

    }
}
