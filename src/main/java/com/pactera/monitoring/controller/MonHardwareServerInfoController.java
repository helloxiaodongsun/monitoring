package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.constant.ErrorMsgConstants;
import com.pactera.monitoring.core.controller.BaseController;
import com.pactera.monitoring.core.page.TableDataInfo;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 服务器信息
 *
 * @author 84483
 */
@Controller
@RequestMapping("/serverInfo")
@Slf4j
public class MonHardwareServerInfoController extends BaseController {


    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 根据ip,查询该服务器的基本信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器基本信息查询")
    @ResponseBody
    public AjaxResult getServerInfo(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfoDto monHardwareServerInfoDto = monHardwareServerInfoService.queryServerInfoByIp(ip);
        return AjaxResult.success(monHardwareServerInfoDto);
    }

    /**
     * 查询服务器基本信息列表
     * @param monHardwareServerInfo
     * @return
     */
    @PostMapping("/list")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器基本信息查询")
    @ResponseBody
    public TableDataInfo list(MonHardwareServerInfo monHardwareServerInfo){
        startPage();
        List<MonHardwareServerInfo> list = monHardwareServerInfoService.queryListFromDbByCondition(monHardwareServerInfo);
        return getDataTable(list);
    }

    /**
     * 进入服务器新增页面
     * @param mmap
     * @return
     */
    @GetMapping("/add")
    public String add(ModelMap mmap){
        return "hardware/basic_add";
    }

    /**
     * 进入服务器修改页面
     * @param mmap
     * @param ip
     * @return
     */
    @GetMapping("/edit/{ip}")
    public String edit(ModelMap mmap,@PathVariable("ip") String ip) {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        mmap.put("serverInfo",monHardwareServerInfo);
        return "hardware/basic_edit";
    }
    /**
     * 验证ip唯一性
     * @param serviceIp
     * @return
     */
    @GetMapping("/checkServiceIpUnique")
    @ResponseBody
    public String checkServiceIpUnique(@RequestParam String serviceIp){
        MonHardwareServerInfo monHardwareServerInfo = new MonHardwareServerInfo();
        monHardwareServerInfo.setServiceIp(serviceIp);
        List<MonHardwareServerInfo> list = monHardwareServerInfoService.queryListFromDbByCondition(monHardwareServerInfo);
        if(list.size()==0){
            return "0";
        }
        return "1";
    }

    /**
     * 测试服务器是否连通
     * @param monHardwareServerInfo
     * @return
     */
    @PostMapping("/testConnect")
    @ResponseBody
    public AjaxResult testConnect(MonHardwareServerInfo monHardwareServerInfo){
        try {
            monHardwareServerInfoService.queryServerInfoFromRemote(monHardwareServerInfo.getServiceUser(),monHardwareServerInfo.getServicePassword(),monHardwareServerInfo.getServiceIp(),Integer.parseInt(monHardwareServerInfo.getServicePort()),new Date());
        } catch (JSchException e) {
            e.printStackTrace();
            return error(ErrorMsgConstants.TEST_SERVER_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
            return error(ErrorMsgConstants.TEST_SERVER_CONNECT);
        }
        return success();
    }
    /**
     * 新增服务器
     * @param monHardwareServerInfo
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(MonHardwareServerInfo monHardwareServerInfo){
        try {
            monHardwareServerInfoService.addServer(monHardwareServerInfo,false);
        }catch (BussinessException e){
            return error(e.getMessage());
        }
        return success();
    }
    /**
     * 修改服务器
     * @param monHardwareServerInfo
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult edit(MonHardwareServerInfo monHardwareServerInfo){
        try {
            monHardwareServerInfoService.addServer(monHardwareServerInfo,true);
        }catch (BussinessException e){
            return error(e.getMessage());
        }
        return success();
    }

    /**
     * 删除服务器信息
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        String[] id = ids.split(",",-1);
        monHardwareServerInfoService.remove(id);
        return success();
    }
}
