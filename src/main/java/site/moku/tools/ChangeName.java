package site.moku.tools;

import java.io.File;
import java.util.Arrays;

public class ChangeName {
    public static void main(String[] args) {
        String path = "C:\\Users\\Louis\\Desktop\\w";
        File file = new File(path);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.asList(files).forEach(item->{
                if(item.getName().contains("-")) {
                    item.renameTo(new File(item.getAbsolutePath().replace("-","_")));
                }
            });
        }
    }
}
