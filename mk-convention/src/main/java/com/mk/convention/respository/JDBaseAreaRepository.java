package com.mk.convention.respository;

import com.mk.convention.persistence.model.JDBaseArea;
import com.mk.convention.persistence.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface JDBaseAreaRepository extends JpaRepository<JDBaseArea, String>  {

}
