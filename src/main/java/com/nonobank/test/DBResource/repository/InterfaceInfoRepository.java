package com.nonobank.test.DBResource.repository;

import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by H.W. on 2018/5/23.
 */
public interface InterfaceInfoRepository extends JpaRepository<MockInterfaceInfo, Long> {
    public MockInterfaceInfo getMockInterfaceInfoByUrl(String name);

    public List<MockInterfaceInfo> findByPathInfoId(Long pathinfoId);

}
