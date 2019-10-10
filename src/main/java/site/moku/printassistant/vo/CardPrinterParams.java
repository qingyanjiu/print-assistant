/**
 * @author louisliu
 */

package site.moku.printassistant.vo;

public class CardPrinterParams {

    private String path;
    private int imgNumber;
    private int numberPerRow;
    private String imgPrefix;
    private String width;
    private String height;
    private String history;
    private int times;
    private int start;

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getImgNumber() {
        return imgNumber;
    }

    public void setImgNumber(int imgNumber) {
        this.imgNumber = imgNumber;
    }

    public int getNumberPerRow() {
        return numberPerRow;
    }

    public void setNumberPerRow(int numberPerRow) {
        this.numberPerRow = numberPerRow;
    }

    public String getImgPrefix() {
        return imgPrefix;
    }

    public void setImgPrefix(String imgPrefix) {
        this.imgPrefix = imgPrefix;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
