package site.moku.tools;

import java.io.File;
import java.util.Arrays;

public class ChangeName {
    public static void main(String[] args) {
        String path = "E:\\games\\board-game\\Splendor The Cities of Splendor";
        File file = new File(path);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.asList(files).forEach(item->{
                if(item.getName().contains(".png")) {
                    item.renameTo(new File(item.getAbsolutePath().replace(".png",".jpg")));
                }
                else if(!item.getName().contains(".")) {
                    item.renameTo(new File(item.getAbsolutePath()+".png"));
                }
            });
        }
    }
}
