package com.mk.convention.respository;

import java.util.Date;
import java.util.List;

import com.mk.convention.model.ProductPriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mk.convention.persistence.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Order, String>  {
	/**
	 * like查询
	 *
	 * @param no
	 * @return
	 */
	List<Order> findAllByNoLike(String no);

	/**
	 * between查询
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Order> findAllByDateBetween(Date startDate, Date endDate);

	/**
	 * 小于查询
	 *
	 * @param quantity
	 * @return
	 */
	List<Order> findAllByQuantityLessThan(int quantity);

    @Query(value="SELECT category.category_id AS skuId FROM category ORDER BY category_id LIMIT ?1,100",nativeQuery=true)
	List<String> findProduct(@Param("start")Integer start);



}
