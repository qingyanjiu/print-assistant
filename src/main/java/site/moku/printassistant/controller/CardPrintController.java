/**
 * @author louisliu
 */

package site.moku.printassistant.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;
import site.moku.printassistant.service.IPrintInfoService;
import site.moku.printassistant.utils.Tools;
import site.moku.printassistant.vo.CardPrinterParams;

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

    @RequestMapping("get-history")
    @ResponseBody
    public String getHistory(String printKey) {
        return printInfoService.getFromRedis(printKey);
    }

    @RequestMapping("auto-printer")
    public String cardAutoPrint(Model model, CardPrinterParams printerParams) {
        List<Map<String, String>> imgs = new ArrayList();
        List imgList = new ArrayList();
        String history = printerParams.getHistory();
        if (!StringUtils.isEmpty(history)) {
            String hStr = printInfoService.getFromRedis(history);
            imgList = JSON.parseObject(hStr, List.class);
        } else {
            int startIndex = printerParams.getStart() - 1;
            File file = new File(printerParams.getPath());
            if (file.isDirectory()) {
                File[] children = file.listFiles(name -> name.getName().startsWith(printerParams.getImgPrefix()));
                List<File> childList = Arrays.asList(children);
                for (int index = 0; index < childList.size(); index++) {
                    File child = childList.get(index);
                    if (child.isFile()) {
                        for (int i = 0; i < printerParams.getTimes(); i++) {
                            if (index >= startIndex) {
                                if (child.getName().indexOf("-") == -1) {
                                    Map map = new HashMap();
                                    String order = child.getName().split("\\.")[0];
                                    if (!StringUtils.isEmpty(printerParams.getImgPrefix())) {
                                        order = order.split(printerParams.getImgPrefix())[1];
                                    }
                                    map.put("src", "data:image;base64," + Tools.encodeBase64File(child));
                                    map.put("order", order);
                                    imgs.add(map);
                                } else {
                                    Map map = new HashMap();
                                    int number = Integer.valueOf(child.getName().split("-")[1].split("\\.")[0]);
                                    String order = child.getName().split("-")[0];
                                    if (!StringUtils.isEmpty(printerParams.getImgPrefix())) {
                                        order = order.split(printerParams.getImgPrefix())[1];
                                    }
                                    map.put("src", "data:image;base64," + Tools.encodeBase64File(child));
                                    map.put("order", order);
                                    for (int j = 0; j < number; j++) {
                                        imgs.add(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            imgs = imgs.stream().sorted((m1, m2) -> m1.get("order").compareTo(m2.get("order"))).collect(Collectors.toList());
            imgList = imgs.stream().map(m -> m.get("src")).collect(Collectors.toList());
            printInfoService.saveToRedis(printerParams.getPath(), JSON.toJSONString(imgList));
        }
        model.addAttribute("imgs", imgList);
        model.addAttribute("printerParams", printerParams);
        return "card-auto-printer";
    }

}
