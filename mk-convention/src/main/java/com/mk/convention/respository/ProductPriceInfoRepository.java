package com.mk.convention.respository;

import com.mk.convention.model.ProductPriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: 邓双雄
 * @Descritioon:
 * @Date: 2018/4/28 18:31
 **/
public interface ProductPriceInfoRepository extends JpaRepository<ProductPriceInfo, String> {

    @Query(value = "INSERT INTO `product_price_info` " +
            "(`id`, `sku_id`, `market_price`, `naked_price`, `price`, `tax_price`, `tax`, `jd_price`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)",nativeQuery = true
    )
    void save1(ProductPriceInfo productPriceInfo1);
}
