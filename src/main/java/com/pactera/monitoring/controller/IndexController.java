package com.pactera.monitoring.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.SysMenu;
import com.pactera.monitoring.entity.TestServer;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.service.*;
import com.pactera.monitoring.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    MenuService menuService;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;
    @Autowired
    MonHardwareCpuInfoService monHardwareCpuInfoService;
    @Autowired
    MonHardwareIoInfoService monHardwareIoInfoService;
    @Autowired
    MonHardwareMemInfoService monHardwareMemInfoService;
    @Autowired
    MonHardwareDiskInfoService monHardwareDiskInfoService;

    @GetMapping("/index")
    public String index(ModelMap mmap){
        List<SysMenu> menus = menuService.selectMenuNormalAll();
        mmap.put("menus",menus);
        return "index";
    }

    /**
     * 主页
     * @param mmap
     * @return
     * @throws Exception
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfoToDto();
        mmap.put("servers",servers);
        return "main";
    }

    /**
     * 进入硬盘信息监控页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/disk")
    public String diskIndex(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfoToDto();
        mmap.put("servers",servers);
        return "hardware/disk";
    }
    /**
     * 进入硬盘信息图表页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/disk/create-chart")
    public String diskCreateChart(ModelMap mmap,SearchBaseEntity searchBaseEntity) throws Exception {

        Map<Object,Object> map = monHardwareDiskInfoService.diskChartData(searchBaseEntity);
        /*JSONObject rawData = JSONObject.parseObject("{\n" +
                "  \"overlay\":[2228,12281,12287,228172,28172,28172,222672,22172,22172,222812,22272,28172,62282,9228172,822812,228172,222872,228172,222172,228172,228172,228172,222172,222872],\n" +
                "  \"tmpfs\":[10041,60001,10004,700041,100041,10600041,10060041,10700041,10001,10041,100041,8000041,600041,500041,100041,600041,1087041,400041,2000041,100041,1060041,100001,100041,100041],\n" +
                "  \"shm\":[100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004,100004],\n" +
                "  \"/dev/sda1\":[222810,222720,21720,281720,8551720,9281720,281720,2228120,2281720,22281720,22281720,22281720,22281720,22281620,222820,2221720,2281720,2221720,2228120,2221720,22281720,22281720,22281720,22281720]\n" +
                "}");
        JSONArray legendData = JSONArray.parseArray("['overlay','tmpfs','shm','/dev/sda1']");
        JSONArray xAxisData = JSONArray.parseArray("['2020-04-10 00:00','2020-04-10 01:00','2020-04-10 02:00','2020-04-10 03:00','2020-04-10 04:00','2020-04-10 05:00','2020-04-10 06:00','2020-04-10 07:00','2020-04-10 08:00','2020-04-10 09:00','2020-04-10 10:00','2020-04-10 11:00','2020-04-10 12:00','2020-04-10 13:00','2020-04-10 14:00','2020-04-10 15:00','2020-04-10 16:00','2020-04-10 17:00','2020-04-10 18:00','2020-04-10 19:00','2020-04-10 20:00','2020-04-10 21:00','2020-04-10 22:00','2020-04-10 23:00']");
*/
        mmap.put("rawData",map.get("rawData"));
        mmap.put("legendData",map.get("legendData"));
        mmap.put("xAxisData",map.get("xAxisData"));
        return "hardware/disk-chart";
    }
    /**
     * 进入cpu信息监控页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/cpu")
    public String cpuIndex(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfoToDto();
        mmap.put("servers",servers);
        return "hardware/cpu";
    }

    /**
     * 进入cpu信息图表页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/cpu/create-chart")
    public String cpuCreateChart(ModelMap mmap,SearchBaseEntity searchBaseEntity) throws Exception {

        List dataSetSource = monHardwareCpuInfoService.cpuChartData(searchBaseEntity);
        /*JSONArray dataSetSource = JSONArray.parseArray("[\n" +
                "                    ['product', '2020-04-13 00:00', '2020-04-13 01:00', '2020-04-13 02:00', '2020-04-13 03:00', '2020-04-13 04:00', '2020-04-13 05:00'],\n" +
                "                    ['用户空间占用', 41.1, 30.4, 65.1, 53.3, 83.8, 98.7],\n" +
                "                    ['内核空间占用', 86.5, 92.1, 85.7, 83.1, 73.4, 55.1],\n" +
                "                    ['改变优先级进程占用', 24.1, 67.2, 79.5, 86.4, 65.2, 82.5],\n" +
                "                    ['IO等待占用', 55.2, 67.1, 69.2, 72.4, 53.9, 39.1],\n" +
                "                    ['空闲cpu占用', 56.2, 68.1, 65.2, 70.4, 13.9, 39.1]\n" +
                "                ]");*/

        mmap.put("dataSetSource",dataSetSource);
        return "hardware/cpu-chart";
    }

    /**
     * 进入io信息监控页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/io")
    public String ioIndex(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfoToDto();
        mmap.put("servers",servers);
        return "hardware/io";
    }

    /**
     * 进入io信息图表页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/io/create-chart")
    public String ioCreateChart(ModelMap mmap,SearchBaseEntity searchBaseEntity) throws Exception {

        Map<Object,Object> map = monHardwareIoInfoService.ioChartData(searchBaseEntity);
        /*JSONArray xAxisData = JSONArray.parseArray("['Apr 13, 2020 11:16:06 AM', 'Apr 13, 2020 11:16:20 AM', 'Apr 13, 2020 11:16:40 AM', 'Apr 13, 2020 11:17:00 AM', 'Apr 13, 2020 11:17:20 AM', 'Apr 13, 2020 11:17:40 AM', 'Apr 13, 2020 11:18:00 AM', 'Apr 13, 2020 11:18:20 AM', 'Apr 13, 2020 11:18:40 AM', 'Apr 13, 2020 11:19:00 AM']");
        JSONObject seriesData = JSONObject.parseObject("{\"diskTrans\":[\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\",\"10.070\"],\n" +
                "\"diskRead\":[\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\",\"2.960\"],\n" +
                "\"diskWrite\":[\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\",\"7.110\"],\n" +
                "\"diskUseRate\":[\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\",\"3.545\"],\n" +
                "\"diskAvgRespond\": [\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\",\"8.820\"]}");
*/
        mmap.put("xAxisData",map.get("xAxisData"));
        mmap.put("seriesData",map.get("seriesData"));
        return "hardware/io-chart";
    }

    /**
     * 进入内存信息监控页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/mem")
    public String memIndex(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfoToDto();
        mmap.put("servers",servers);
        return "hardware/mem";
    }

    /**
     * 进入内存信息图表页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/mem/create-chart")
    public String memCreateChart(ModelMap mmap,SearchBaseEntity searchBaseEntity) throws Exception {

        Map<Object,Object> map = monHardwareMemInfoService.memChartData(searchBaseEntity);
        /*JSONArray xAxisData = JSONArray.parseArray("['Apr 13, 2020 11:16:06 AM', 'Apr 13, 2020 11:16:20 AM', 'Apr 13, 2020 11:16:40 AM', 'Apr 13, 2020 11:17:00 AM', 'Apr 13, 2020 11:17:20 AM', 'Apr 13, 2020 11:17:40 AM', 'Apr 13, 2020 11:18:00 AM', 'Apr 13, 2020 11:18:20 AM', 'Apr 13, 2020 11:18:40 AM', 'Apr 13, 2020 11:19:00 AM']");
        JSONObject seriesData = JSONObject.parseObject("{\n" +
                "  \"memUseTotal\":[\"0.287\",\"0.288\",\"0.289\",\"0.287\",\"0.297\",\"0.267\",\"0.287\",\"0.287\",\"0.287\",\"0.287\"],\n" +
                "  \"freeMemTotal\" :[\"0.845\",\"0.844\",\"0.843\",\"0.845\",\"0.835\",\"0.865\",\"0.845\",\"0.845\",\"0.845\",\"0.845\"],\n" +
                "  \"bufferCacheUseMemTotal\":[\"0.832\",\"0.822\",\"0.818\",\"0.815\",\"0.912\",\"0.712\",\"0.812\",\"0.812\",\"0.812\",\"0.812\"]\n" +
                "}");*/

        mmap.put("xAxisData",map.get("xAxisData"));
        mmap.put("seriesData",map.get("seriesData"));
        return "hardware/mem-chart";
    }
}
