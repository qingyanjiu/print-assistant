/**
 * @author louisliu
 */

package site.moku.printassistant.print.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import site.moku.printassistant.print.utils.Tools;
import site.moku.printassistant.print.vo.BookPrinterParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/print/book")
public class BookPrintController {

    @RequestMapping("options")
    public String bookOptions() {
        return "book-options";
    }


    @RequestMapping("printer")
    public String bookPrint(Model model, BookPrinterParams printerParams) {
        List<Map> imgs = new ArrayList();
        int page = 2 * (printerParams.getPage() - 1) + 1;

        if ("1".equals(printerParams.getSide())) {
            Map map = new HashMap();
            Map mapReverse = new HashMap();
            map.put("name", page);
            File f = new File(printerParams.getPath() + "\\" + (page > 9 ? page : "0" + page) + ".png");
            String imgBase64 = "data:image;base64," + Tools.encodeBase64File(f);
            map.put("path", imgBase64);

            mapReverse.put("name", printerParams.getPageNumber() - (page - 1));
            File fReverse = new File(printerParams.getPath() + "\\" +
                    ((printerParams.getPageNumber() - (page - 1)) > 9 ? (printerParams.getPageNumber() - (page - 1)) : "0" +
                            (printerParams.getPageNumber() - (page - 1))) + ".png");
            String imgBase64Reverse = "data:image;base64," + Tools.encodeBase64File(fReverse);
            mapReverse.put("path", imgBase64Reverse);

            imgs.add(map);
            imgs.add(mapReverse);
        } else if ("2".equals(printerParams.getSide())) {
            Map map = new HashMap();
            Map mapReverse = new HashMap();
            map.put("name", page + 1);
            File f = new File(printerParams.getPath() + "\\" + ((page + 1) > 9 ? (page + 1) : "0" + (page + 1)) + ".png");
            String imgBase64 = "data:image;base64," + Tools.encodeBase64File(f);
            map.put("path", imgBase64);

            mapReverse.put("name", printerParams.getPageNumber() - page);
            File fReverse = new File(printerParams.getPath() + "\\" +
                    ((printerParams.getPageNumber() - page) > 9 ? (printerParams.getPageNumber() - page) : "0" +
                            (printerParams.getPageNumber() - page)) + ".png");
            String imgBase64Reverse = "data:image;base64," + Tools.encodeBase64File(fReverse);
            mapReverse.put("path", imgBase64Reverse);

            imgs.add(map);
            imgs.add(mapReverse);
        }
        model.addAttribute("printerParams", printerParams);
        model.addAttribute("imgs", imgs);
        return "book-printer";
    }
}
