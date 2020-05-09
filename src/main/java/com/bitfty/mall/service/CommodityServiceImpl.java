package com.bitfty.mall.service;

import com.bitfty.mall.dto.CommodityDto;
import org.apache.dubbo.config.annotation.Service;

@Service
public class CommodityServiceImpl implements CommodityService {


    @Override
    public CommodityDto findById(Long id) {
        CommodityDto commodity = new CommodityDto();
        return commodity;
    }
}
