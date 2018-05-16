package com.mk.convention.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mk.convention.config.DataSource;
import com.mk.convention.model.entity.CategoryDocument;
import com.mk.convention.model.entity.ItemDocument;
import com.mk.convention.model.entity.ProductDetailDocument;
import com.mk.convention.persistence.model.Order;
import com.mk.convention.respository.es.CategoryRepository;
import com.mk.convention.respository.es.ItemDocumentRepository;
import com.mk.convention.respository.es.ProductDetailRepository;

/**
 * Created by lijingyao on 2018/1/19 10:10.
 */
@RestController
@RequestMapping("/items")
public class SearchController {

    @Autowired
    private ItemDocumentRepository repository;


    @RequestMapping(value = "/{id}",method = {RequestMethod.GET})
    public ResponseEntity getItem(@PathVariable("id") String id) {
    	System.out.println(id);
        ItemDocument com = repository.findById(id).get();
        return new ResponseEntity(com.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "create",method = {RequestMethod.POST})
    public ResponseEntity createItem(@RequestBody ItemDocument document) {
        repository.save(document);

        return new ResponseEntity(document.toString(), HttpStatus.OK);
    }
    
    @Autowired
    private ProductDetailRepository productDetailRepository;
    
    @PostMapping("/searchProducDetail")
    @ResponseBody
    public  Map searchProducDetail(String skuId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("skuId", skuId);
        Optional<ProductDetailDocument> productDetailDocumentSearch = productDetailRepository.findById(skuId);
        ProductDetailDocument productDetailDocument = productDetailDocumentSearch.get();
        result.put("info", productDetailDocument.getInfo());
        return result;
    }
    
    
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    private DataSource dataSource = new DataSource();
    
    @GetMapping("/createProductCategory")
    @ResponseBody
    public  Map createProductCategory() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("ok", "数据库数据同步到索引");
        elasticsearchTemplate.deleteIndex(ProductDetailDocument.class);
        //查询数据
        ArrayList results = this.dataSource.executeQuery("select count(*) cnt from new_category", null);
		 Object[] obj = (Object[]) results.get(0);
		 Integer count = obj[0]==null? 0 : Integer.parseInt(String.valueOf(obj[0]));
		 final int index = 10000;
		 int page = 0;
		 int pageCount = count/index;
		 if (count%index>0) {
			 pageCount++; 
		 }
		 ArrayList categorys = null;
		 List<CategoryDocument> categoryDocs = new ArrayList<CategoryDocument>();
		 for (int i=0;i<pageCount;i++) {
			int pageNum = i*index;
			categorys = this.dataSource.executeQuery("select id,sku_id,category_id from new_category  where id > "+pageNum+" order by id asc limit 0,"+index, null);
			for(int j=0,length=categorys.size();j<length;j++) {
				 Object[] categoryO= (Object[]) categorys.get(j);
				 String sku =  String.valueOf(categoryO[1]);
				 String categoryId =  String.valueOf(categoryO[2]);
				CategoryDocument category = new CategoryDocument();
				category.setSkuId(sku);
				category.setCategoryId(categoryId);
				categoryDocs.add(category);
			}
			categoryRepository.saveAll(categoryDocs);
		 }
        return result;
    }

}
