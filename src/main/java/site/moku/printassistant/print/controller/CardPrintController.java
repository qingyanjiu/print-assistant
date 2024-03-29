/**
 * @author louisliu
 */

package site.moku.printassistant.print.controller;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;
import site.moku.printassistant.print.service.IPrintInfoService;
import site.moku.printassistant.print.utils.Tools;
import site.moku.printassistant.print.vo.CardPrinterParams;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/print/card")
public class CardPrintController {

    @Autowired
    private IPrintInfoService printInfoService;

    @RequestMapping("options")
    public String cardOptions() {
        return "card-options";
    }

    @RequestMapping("get-keys")
    @ResponseBody
    public Set<String> getKeys() {
        return printInfoService.getHistoryFromRedis();
    }

    @RequestMapping(path = "get-history", produces = "text/html")
    @ResponseBody
    public String getHistory(String printKey) {
        return printInfoService.getFromRedis(printKey);
    }

    @RequestMapping("save-data")
    @ResponseBody
    public Map saveData(String printKey, String pageHtml) {
        Map map = new HashMap();
        printInfoService.saveToRedis(printKey, pageHtml);
        map.put("success", true);
        return map;
    }

    @RequestMapping("auto-printer")
    public String cardAutoPrint(Model model, CardPrinterParams printerParams) {
        List<Map<String, String>> imgs = new ArrayList();
        List imgList = new ArrayList();
        File back = null;
        int total = printerParams.getImgNumber();

        if (!StringUtil.isNullOrEmpty(printerParams.getBackPath()))
            back = new File(printerParams.getBackPath());
        int startIndex = printerParams.getStart() - 1;
        int numberPerRow = printerParams.getNumberPerRow();
        String numSplitter = printerParams.getSplitter();
        File file = new File(printerParams.getPath());
        if (file.isDirectory()) {
            File[] children = file.listFiles(name -> name.getName().startsWith(printerParams.getImgPrefix()));
            List<File> childList = Arrays.asList(children).stream().filter(i -> !i.getName().contains("Thumbs"))
                    .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replaceAll("\\D", ""))))
                    .collect(Collectors.toList());
            for (int index = 0; index < Math.min(childList.size(), total); index++) {
                File child = childList.get(index);
                if (child.isFile()) {
                    for (int i = 0; i < printerParams.getTimes(); i++) {
                        if (index >= startIndex) {
                            // 如果没有数字分隔符，就打印一张
                            if (child.getName().indexOf(numSplitter) == -1) {
                                Map map = new HashMap();
                                String order = child.getName().split("\\.")[0];
                                if (!StringUtils.isEmpty(printerParams.getImgPrefix())) {
                                    order = order.split(printerParams.getImgPrefix())[1];
                                }
                                map.put("src", "data:image;base64," + Tools.encodeBase64File(child));
//                                    map.put("order", order);
                                imgs.add(map);
                            } else {
                                // 如果有数字分隔符，根据数字打印多张重复的
                                Map map = new HashMap();
                                int number = Integer.valueOf(child.getName().split(numSplitter)[1].split("\\.")[0]);
                                String order = child.getName().split("-")[0];
                                if (!StringUtils.isEmpty(printerParams.getImgPrefix())) {
                                    order = order.split(printerParams.getImgPrefix())[1];
                                }
                                map.put("src", "data:image;base64," + Tools.encodeBase64File(child));
//                                    map.put("order", order);
                                for (int j = 0; j < number; j++) {
                                    imgs.add(map);
                                }
                            }
                        }
                    }
                }
                //每到每行最后一个图片出现时，加一行卡背，或者最后一行最后一个元素出现时，加一行卡背
                if (back != null) {
                    if ((index + 1) % numberPerRow == 0 || index == childList.size() - 1) {
                        Map map = new HashMap();
                        map.put("src", "data:image;base64," + Tools.encodeBase64File(back));
                        int n = (index + 1) % numberPerRow;
                        if (n == 0)
                            n = numberPerRow;
                        //补足剩余的几个卡位为空图片
                        for (int e = 0; e < numberPerRow - n; e++) {
                            Map empty = new HashMap();
                            empty.put("src", "");
                            imgs.add(empty);
                        }
                        for (int i = 0; i < n; i++) {
                            imgs.add(map);
                        }
                    }
                }
            }
        }

//            imgs = imgs.stream().sorted((m1, m2) -> m1.get("order").compareTo(m2.get("order"))).collect(Collectors.toList());
        imgList = imgs.stream().map(m -> m.get("src")).collect(Collectors.toList());
        model.addAttribute("imgs", imgList);
        model.addAttribute("printerParams", printerParams);
        return "card-auto-printer";
    }

}
