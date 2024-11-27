package com.luzhu.truck.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.service.car.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CarCache {
    private LoadingCache<String, List<CarInfo>> carCache;
    @Autowired
    private CarService carService;

    public CarCache() {
        carCache = CacheBuilder.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(30000)
                .build(new CacheLoader<String, List<CarInfo>>() {
                    @Override
                    public List<CarInfo> load(String key) throws Exception {
                        return carService.getAllCarForDropDown();
                    }
                });
    }

    public List<CarInfo> getAllCars(String key) throws ExecutionException {
        List<CarInfo> result = new ArrayList<>();
        try {
            result = carCache.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
    public void invalidate() {
        carCache.invalidateAll();
    }


}
