package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.NodeInfo;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.entity.Code;
import com.nonobank.test.utils.ArgsValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by H.W. on 2018/5/28.
 */

@Service
public class AcquireInfo {

    @Autowired
    private PathInfoServiceImpl pathInfoService;
    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;


    public String getNodeList(String id) {
        ArgsValid.notEmpty(id, "节点Id");
        Long nodeId = Long.valueOf(id);
        List<PathInfo> paths = pathInfoService.findByPid(nodeId);
        List<MockInterfaceInfo> mocks = interfaceInfoService.findByPathInfoId(nodeId);
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setId(nodeId);
        List<NodeInfo> list = new ArrayList<>();
        if (paths != null && paths.size() > 0) {
            List<NodeInfo> list1 = paths.parallelStream().map(pathInfo ->
            {
                return new NodeInfo().setId(pathInfo.getId()).setName(pathInfo.getPathName()).setType("0");
            })
                    .collect(Collectors.toList());
            list.addAll(list1);
        }
        if (mocks != null && mocks.size() > 0) {
            List<NodeInfo> list1 = mocks.parallelStream().map(info ->
            {
                return new NodeInfo().setId(info.getId()).setName(info.getName()).setType("1");
            })
                    .collect(Collectors.toList());
            list.addAll(list1);
        }
        nodeInfo.setList(list);
        return JSONObject.toJSONString(nodeInfo);
    }


    public String getInterfaceInfo(String pid) {
        ArgsValid.notEmpty(pid, "接口id");
        Long id = Long.valueOf(pid);
        MockInterfaceInfo info = interfaceInfoService.getMockInterfaceInfoById(id);
        return JSONObject.toJSONString(info);
    }
}
