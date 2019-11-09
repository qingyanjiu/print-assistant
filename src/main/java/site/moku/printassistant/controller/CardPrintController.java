/**
 * @author louisliu
 */

package site.moku.printassistant.controller;

import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
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

    @RequestMapping("save-data")
    @ResponseBody
    public Map saveData(String printKey, String imgListJsonStr) {
        Map map = new HashMap();
        printInfoService.saveToRedis(printKey, imgListJsonStr);
        map.put("success",true);
        return map;
    }

    @RequestMapping("auto-printer")
    public String cardAutoPrint(Model model, CardPrinterParams printerParams) {
        List<Map<String, String>> imgs = new ArrayList();
        List imgList = new ArrayList();
        File back = null;
        if(!StringUtil.isNullOrEmpty(printerParams.getBackPath()))
            back = new File(printerParams.getBackPath());
        String history = printerParams.getHistory();
        if (!StringUtils.isEmpty(history)) {
            String hStr = printInfoService.getFromRedis(history);
            imgList = JSON.parseObject(hStr, List.class);
        } else {
            int startIndex = printerParams.getStart() - 1;
            int numberPerRow = printerParams.getNumberPerRow();
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
//                                    map.put("order", order);
                                    imgs.add(map);
                                } else {
                                    Map map = new HashMap();
                                    int number = Integer.valueOf(child.getName().split("-")[1].split("\\.")[0]);
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
                    if(back != null) {
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
        }
        model.addAttribute("imgs", imgList);
        model.addAttribute("printerParams", printerParams);
        return "card-auto-printer";
    }

}
