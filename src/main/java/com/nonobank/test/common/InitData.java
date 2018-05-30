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
public class InitData {

    @Autowired
    private PathInfoServiceImpl pathInfoService;
    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;


    private Map<String, BaseInfo> interConfigMap;
    private Map<Long, BaseInfo> pathConfigMap;
    /* private List<BaseInfo> parentNodeList;
     private List<BaseInfo> discardList;*/
    private Map<Long, Map<String, Object>> pathConfig;
    private Map<String, Map<String, Object>> interConfig;


    public Map<String, Object> getInterfaceInfoConfig(String url) {
        return interConfig.get(url);
    }


    public void initConfig() {
        List<BaseInfo> lists = new ArrayList<>();
        //查出所有目录信息
        List<PathInfo> pathInfos = pathInfoService.getAll();
        //查出所有接口信息
        List<MockInterfaceInfo> infos = interfaceInfoService.getAll();
        //合并目录、接口信息
        lists.addAll(pathInfos.stream().map(info -> info.setDirectory(true)).collect(Collectors.toList()));
        lists.addAll(infos.stream().map(info -> {
            info.setPid(0l);
            return info.setDirectory(false);
        }).collect(Collectors.toList()));
        parse(lists);
    }

    public void setInterConfig() {
        interConfig = new HashMap<>();
        for (Map.Entry<String, BaseInfo> entity : interConfigMap.entrySet()) {
            String key = entity.getKey();
            BaseInfo baseInfo = entity.getValue();
            long pid = baseInfo.getPid();
            Map<String, Object> config = new HashMap<>();
            config.putAll(pathConfig.get(pid));
            config.putAll(JSONObject.parseObject(baseInfo.getConfig()));
            if (baseInfo.getNeedProxy()) {
                Map<String, Object> ipMap = new HashMap<>();
                ipMap.put("ipMap", baseInfo.getIpMap());
                config.putAll(ipMap);
            }
            interConfig.put(key, config);
        }
    }


    public void setPathNodeConfig() {
        ArgsValid.notEmpty(pathConfigMap, "目录缓存列表");
        pathConfig = new HashMap<>();
        Map<String, Object> lowerNodeConfig;
        for (Map.Entry<Long, BaseInfo> entry : pathConfigMap.entrySet()) {
            long id = entry.getKey();
            if (id == 0) {
                continue;
            }
            lowerNodeConfig = new HashMap<>();
            lowerNodeConfig = getNodeConfig(id, pathConfigMap, lowerNodeConfig);
            pathConfig.put(id, lowerNodeConfig);
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
        // parentNodeList = new ArrayList<>();
        pathConfigMap = new HashMap<>();
        //discardList = new ArrayList<>();
        //设置根节点
        BaseInfo root = BaseInfo.createRoot();
        //parentNodeList.add(root);   //填加根节点到目录列表
        for (BaseInfo info : infos) {
           /* if (info.getPid().equals(root.getId())) { //判断是否为根节点的子节点
               // root.getChildren().add(info); //添加节点信息到根节点的子节点列表
                parentNodeList.add(info);
            } else {
                //父节点不是根节点
                getChildNode(parentNodeList, info);
            }*/
            if (info.isDirectory()) {
                pathConfigMap.put(info.getId(), info);
            } else {
                MockInterfaceInfo mockInfo = (MockInterfaceInfo) info;
                interConfigMap.put(mockInfo.getUrl(), mockInfo);
            }
        }
        setPathNodeConfig();
        setInterConfig();
    }


    private void getChildNode(List<BaseInfo> plist, BaseInfo info) {
        for (BaseInfo base : plist) {
            //判断当前节点的上一节点是否在父目录列表中
            if (base.isDirectory() && base.getId().equals(info.getPid())) {  //只有目录才可以有子节点
                base.getChildren().add(info);//添加该节点为子节点
                //parentNodeList.add(info);
                return;
            }
        }
        // discardList.add(info);//未在列表中找到父节点，添加该节点到失效列表

    }
}
