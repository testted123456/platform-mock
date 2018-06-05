package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.*;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.utils.ArgsValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by H.W. on 2018/5/30.
 */
@Service
public class PreserveConfig {

    @Autowired
    private PathInfoServiceImpl pathInfoService;
    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;


    private Map<String, BaseInfo> interConfigMap;
    private Map<Long, BaseInfo> dictConfigMap;
    private Map<Long, Map<String, Object>> dictConfig;
    private Map<String, Map<String, Object>> interConfig;


    public Map<String, Object> getInterfaceInfoConfig(String url) {
        return interConfig.get(url);
    }

    public Map<String, Map<String, Object>> getInterfaceConfigList() {
        return interConfig;
    }

    public Map<Long, Map<String, Object>> getDictConfigList() {
        return dictConfig;
    }


    public void initConfig() {
        List<BaseInfo> lists = new ArrayList<>();
        //查出所有目录信息
        List<PathInfo> pathInfos = pathInfoService.getAll();
        //查出所有接口信息
        List<MockInterfaceInfo> infos = interfaceInfoService.getAll();
        //合并目录、接口信息
        lists.addAll(pathInfos.stream().map(info ->
                info.setDirectory(true)).collect(Collectors.toList()));
        lists.addAll(infos.stream().map(info -> {
            info.setPid(0l);
            return info.setDirectory(false);
        }).collect(Collectors.toList()));
        parse(lists);
    }

    private void setInterConfig() {
        interConfig = new HashMap<>();
        for (Map.Entry<String, BaseInfo> entity : interConfigMap.entrySet()) {
            String key = entity.getKey();
            BaseInfo baseInfo = entity.getValue();
            long pid = baseInfo.getPid();
            if (pid==0){
                continue;
            }
            Map<String, Object> config = new HashMap<>();
            config.putAll(dictConfig.get(pid));
            config.putAll(JSONObject.parseObject(baseInfo.getConfig()));
            if (baseInfo.getNeedProxy()) {
                Map<String, Object> ipMap = new HashMap<>();
                ipMap.put("ipMap", baseInfo.getIpMap());
                config.putAll(ipMap);
            }
            interConfig.put(key, config);
        }
    }


    private void setPathNodeConfig() {
        ArgsValid.notEmpty(dictConfigMap, "目录缓存列表");
        dictConfig = new HashMap<>();
        Map<String, Object> lowerNodeConfig;
        for (Map.Entry<Long, BaseInfo> entry : dictConfigMap.entrySet()) {
            long id = entry.getKey();
            lowerNodeConfig = new HashMap<>();
            if (id == 0) {
                dictConfig.put(id,lowerNodeConfig);
            }
            lowerNodeConfig = getNodeConfig(id, dictConfigMap, lowerNodeConfig);
            dictConfig.put(id, lowerNodeConfig);
        }
    }


    private Map<String, Object> getNodeConfig(long id, Map<Long, BaseInfo> pathConfigMap, Map<String, Object> lowerNodeConfig) {
        BaseInfo baseInfo = pathConfigMap.get(id); //获取当前节点
        Map<String, Object> current = new HashMap<>();
        current.putAll(JSONObject.parseObject(baseInfo.getConfig()));//获取当前节点配置信息
        if (baseInfo.getNeedProxy()) {
            Map<String, Object> ipMap = new HashMap<>();
            ipMap.put("ipMap", baseInfo.getIpMap());
            current.putAll(ipMap);
        }
        current.putAll(lowerNodeConfig);
        long pid = baseInfo.getPid();
        if (pid != 0) {
            current = getNodeConfig(pid, pathConfigMap, current);
        }
        return current;
    }


    /**
     * @param infos
     */
    public void parse(List<BaseInfo> infos) {
        interConfigMap = new HashMap<>();
        dictConfigMap = new HashMap<>();
        //设置根节点
        BaseInfo root = BaseInfo.createRoot();
        for (BaseInfo info : infos) {
            if (info.isDirectory()) {
                dictConfigMap.put(info.getId(), info);
            } else {
                MockInterfaceInfo mockInfo = (MockInterfaceInfo) info;
                interConfigMap.put(mockInfo.getUrl(), mockInfo);
            }
        }
        setPathNodeConfig();
        setInterConfig();
    }


}
