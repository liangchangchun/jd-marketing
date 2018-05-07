package com.mk.convention.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mk.convention.entity.ItemDocument;
import com.mk.convention.respository.es.ItemDocumentRepository;

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

}
