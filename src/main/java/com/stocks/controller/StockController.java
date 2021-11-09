package com.stocks.controller;

import com.stocks.dto.StocksDTO;
import com.stocks.service.StockService;
import com.stocks.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StockController {

    private  final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/save")
    public CommonResponse saveUpdate(@RequestBody StocksDTO stocksDTO){
        return stockService.saveUpdate(stocksDTO);
    }

    @DeleteMapping("/{stockId}")
    public CommonResponse deleteStock(@PathVariable String stockId){
        return stockService.deleteStock(stockId);

    }

    @GetMapping("/{stockId}")
    public CommonResponse getStockById(@PathVariable String stockId){
        return stockService.getStockById(stockId);
    }

    @GetMapping("/")
    public CommonResponse AllStock(){
        return stockService.getAllStocks();
    }

}
