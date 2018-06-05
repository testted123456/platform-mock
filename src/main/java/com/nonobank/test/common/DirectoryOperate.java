package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.Config;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.DBResource.entity.ValidException;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by H.W. on 2018/5/31.
 */

@Component
public class DirectoryOperate extends AbstractPreOperate {
    @Autowired
    private PathInfoServiceImpl pathInfoService;

    public boolean operate(PathInfo info) {
        valid(info);
        ArgsValid.notEmpty(info.getPid(), "pid");
        PathInfo parent = getParentPathInfoById(info.getPid());
        if (parent == null) {
            throw new ValidException("节点id" + info.getPid() + "不存在");
        }
        info.setFullName(parent.getFullName() + "/" + info.getName());
        String config = info.getConfig();
        if (!StringUtils.isEmpty(config)) {
            Map<String, Object> configs =
                    JSONObject.parseArray(config, Config.class).stream()
                            .collect(Collectors.toMap(Config::getName, Config::getValue, (key1, key2) -> key2));
            config = JSONObject.toJSONString(configs);
            info.setConfig(config);
        }
        if (info.getNeedProxy()) {
            String ipMap = info.getIpMap();
            if (!StringUtils.isEmpty(ipMap)) {
                Map<String, Object> ipMaps =
                        JSONObject.parseArray(config, Config.class).stream()
                                .collect(Collectors.toMap(Config::getName, Config::getValue, (key1, key2) -> key2));
                ipMap = JSONObject.toJSONString(ipMaps);
                info.setIpMap(ipMap);
            }
        }
        PathInfo current = pathInfoService.add(info);
        if (current == null) {
            return false;
        }
        return true;

    }

    private PathInfo getParentPathInfoById(long id) {
        PathInfo parent = null;
        if (id == 0) {
            parent = new PathInfo();
            parent.setFullName("");
            //prePathInfo.setId(0l);
        } else {
            parent = pathInfoService.getPathInfoById(id);
        }
        return parent;
    }
}
