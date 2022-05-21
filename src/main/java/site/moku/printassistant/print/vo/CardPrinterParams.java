/**
 * @author louisliu
 */

package site.moku.printassistant.print.vo;

public class CardPrinterParams {

    private String path;
    private String backPath;
    private int imgNumber;
    private int numberPerRow;
    private String imgPrefix;
    private String width;
    private String height;
    private String history;
    private int times;
    private String lineWidth;
    private int start;
    private int bleedWidth;
    private int leftOffset;
    private String reverse;
    private String bleedColor;
    private String splitter;

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

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

    public String getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(String lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getBleedWidth() {
        return bleedWidth;
    }

    public void setBleedWidth(int bleedWidth) {
        this.bleedWidth = bleedWidth;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    public String getBleedColor() {
        return bleedColor;
    }

    public void setBleedColor(String bleedColor) {
        this.bleedColor = bleedColor;
    }

    public String getSplitter() {
        return splitter;
    }

    public void setSplitter(String splitter) {
        this.splitter = splitter;
    }
}
