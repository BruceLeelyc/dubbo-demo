package com.bitfty.mall.service;

import com.bitfty.enums.ProductType;
import com.bitfty.mall.dao.ProductDao;
import com.bitfty.mall.dao.ProductDetailDao;
import com.bitfty.mall.dao.ProductStoreRecordDao;
import com.bitfty.mall.dto.ProductDto;
import com.bitfty.mall.dto.ProductOptDto;
import com.bitfty.mall.entity.Product;
import com.bitfty.mall.entity.ProductDetail;
import com.bitfty.mall.entity.ProductStoreRecord;
import com.bitfty.mall.enums.ProductOperate;
import com.bitfty.mall.enums.ProductStatus;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.mall.product.ProductFactory;
import com.bitfty.mall.product.handler.ProductHandler;
import com.bitfty.mall.utils.DistributedLock;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import com.bitfty.util.RedisLockUtil;
import com.bitfty.util.SnowflakeIdWorker;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ProductServiceImpl
 * @Description: 产品
 * @author: zhangmi
 * @date: 2019/12/12 10:19
 */
@Service(timeout = 3000)
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final String PRODUCT_STOCK_LOCK = "prod_stock_lock_";

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductDetailDao productDetailDao;

    @Autowired
    private ProductStoreRecordDao productStoreRecordDao;

    @Override
    public Page<ProductDto> pageGeteway(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("start", (pageNo - 1) * pageSize);
        params.put("ends", pageSize);
        List<Product> list = productDao.findPageByParams(params);
        Integer total = productDao.countByParams(params);
        List<ProductDto> productList = BeanMapper.mapList(list, ProductDto.class);
        return new Page(total, pageSize, pageNo, productList);
    }

    @Override
    public Page<ProductDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("start", (pageNo - 1) * pageSize);
        params.put("ends", pageSize);
        params.put("queryType", "pageParams");
        List<Product> list = productDao.findPage(params);
        Integer total = productDao.countByParams(params);
        List<ProductDto> productList = BeanMapper.mapList(list, ProductDto.class);
        return new Page(total, pageSize, pageNo, productList);
    }

    @Override
    public List<ProductDto> findRecommend(Map<String, Object> params) {
        List<Product> list = productDao.findRecommend(params);
        Collections.sort(list, (o1, o2) -> {
            return 0;
        });
        return BeanMapper.mapList(list, ProductDto.class);
    }

    @Override
    public int save(ProductDto product) {
        int result = productDao.insert(BeanMapper.map(product, Product.class));
        logger.info("-----product----save----" + result);
        if (result > 0 && product != null) {
            ProductDetail record = new ProductDetail();
            record.setId(product.getId());
            record.setProdId(product.getId());
            if (StringUtils.isNotBlank(product.getDetailInfo())) {
                record.setDetailInfo(product.getDetailInfo());
            }
            if (StringUtils.isNotBlank(product.getMachineParams())) {
                record.setMachineParams(product.getMachineParams());
            }
            record.setCreateAt(new Date());
            productDetailDao.insert(record);
        }
        return result;
    }

    @Override
    public int update(ProductDto product) {
        int result = productDao.update(BeanMapper.map(product, Product.class));
        logger.info("-----ProductDto----update----" + result);
        if (result > 0 && product != null) {
            ProductDetail record = new ProductDetail();
            record.setId(product.getId());
            record.setProdId(product.getId());
            logger.info("-----DetailInfo----update----" + product.getDetailInfo());
            record.setDetailInfo(product.getDetailInfo());
            record.setMachineParams(product.getMachineParams());
            record.setUpdateAt(new Date());
            productDetailDao.update(record);
        }
        return result;
    }

    @Override
    public ProductDto findById(String id) {
        ProductDto productDto = BeanMapper.map(productDao.findById(id), ProductDto.class);
        if (productDto != null && StringUtils.isNotBlank(productDto.getId())) {
            ProductDetail productDetail = productDetailDao.findByProdId(productDto.getId());
            if (productDetail != null) {
                productDto.setDetailInfo(productDetail.getDetailInfo());
                productDto.setMachineParams(productDetail.getMachineParams());
            }
        }
        return productDto;
    }

    @Override
    public List<String> findSellOutProdIdList() {
        return productDao.findSellOutProdIdList();
    }

    @Override
    public int updateStatusDownByIds(List<String> updateProdIds) {
        return productDao.updateStatusDownByIds(updateProdIds);
    }

    @Override
    public int updateStatusById(String id, String status) {
        return productDao.updateStatusById(id, status);
    }

    @Override
    public String deductStock(String productId, String orderId, int soldCount) {
        logger.info("购买产品库存操作, productId={}, orderId={}, count={}", productId, orderId, soldCount);
        if (StringUtils.isBlank(productId) || StringUtils.isBlank(orderId) || soldCount <= 0) {
            throw new ServiceException(ServiceCode.PARAMS_ERROR);
        }
        // 锁定产品
        String key = PRODUCT_STOCK_LOCK + productId;
        if (!DistributedLock.lock(key, 3L)) {
            //if (!RedisLockUtil.lock(key, "NX",3L, TimeUnit.SECONDS)) {
            throw new ServiceException(ServiceCode.REDIS_LOCK, "操作太频繁请稍后操作");
        }
        try {
            ProductHandler handler = ProductFactory.handler(ProductType.POWER);
            ProductOptDto productOptDto = ProductOptDto.builder().id(productId).orderId(orderId).sellCount(soldCount).build();
            // 减库存
            return handler.deductStock(productOptDto);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("操作库存异常.e={}", e.getMessage(), e);
            throw e;
        } finally {
            // RedisLockUtil.unLock(key);
            DistributedLock.unLock(key);
        }
    }

    @Override
    public String increaseStock(String productId, String orderId) {
        logger.info("产品库存操作, id={}, orderId={}", productId, orderId);
        if (StringUtils.isBlank(productId) || StringUtils.isBlank(orderId)) {
            throw new ServiceException(ServiceCode.PARAMS_ERROR);
        }
        // 锁定产品
        String key = PRODUCT_STOCK_LOCK + productId;
        if (!DistributedLock.lock(key, 3L)) {
            // if (!RedisLockUtil.lock(key, "NX",3L, TimeUnit.SECONDS)) {
            throw new ServiceException(ServiceCode.REDIS_LOCK, "操作太频繁请稍后操作");
        }
        try {
            ProductHandler handler = ProductFactory.handler(ProductType.POWER);
            ProductOptDto productOptDto = ProductOptDto.builder().id(productId).orderId(orderId).build();
            // 加库存
            return handler.increaseStock(productOptDto);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("操作库存异常.e={}", e.getMessage(), e);
            throw e;
        } finally {
            // RedisLockUtil.unLock(key);
            DistributedLock.unLock(key);
        }
    }

    @Override
    public Boolean validateProduct(String productId, int soldCount, boolean flag) throws ServiceException {
        try {
            if (StringUtils.isBlank(productId)) {
                throw new ServiceException(ServiceCode.PARAMS_ERROR);
            }
            ProductHandler handler = ProductFactory.handler(ProductType.POWER);
            ProductOptDto productOptDto = ProductOptDto.builder().id(productId).sellCount(soldCount).build();
            // 验证
            return handler.validateProduct(productOptDto, flag);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("操作库存异常.e={}", e.getMessage(), e);
            throw e;
        }
    }
}
