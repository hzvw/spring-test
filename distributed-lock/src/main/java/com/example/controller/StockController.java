package com.example.controller;

import com.example.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("check/lock")
    public String checkAndLock(){

        //this.stockService.checkAndLock();
        stockService.deduct();
        return "验库存并锁库存成功！";
    }
}