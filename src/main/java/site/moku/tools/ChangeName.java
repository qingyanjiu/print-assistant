package site.moku.tools;

import java.io.File;
import java.util.Arrays;

public class ChangeName {
    public static void main(String[] args) {
        String path = args[0];
        String origin = args[1];
        String dist = args[2];
        File file = new File(path);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.asList(files).forEach(item->{
                if(item.getName().contains("20120601")) {
                    item.renameTo(new File(item.getAbsolutePath().replace(origin, dist)));
                }
            });
        }
    }
}
