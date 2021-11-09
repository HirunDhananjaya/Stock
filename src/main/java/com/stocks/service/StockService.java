package com.stocks.service;

import com.stocks.Constant.CommonMsg;
import com.stocks.Constant.CommonStatus;
import com.stocks.dto.StocksDTO;
import com.stocks.entity.Stocks;
import com.stocks.repository.StockRepository;
import com.stocks.util.CommonResponse;
import com.stocks.util.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public CommonResponse saveUpdate(StocksDTO stocksDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Stocks stocks;

        try {
            List<String> validations = validateStockDTO(stocksDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

             stocks = CastStockDTOIntoStocks(stocksDTO);
             stockRepository.save(stocks);
             commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(getStockIntoStockDTO(stocks)));

        }catch (Exception e) {
            LOGGER.error("/**************** Exception in StockService -> saveUpdateStock()" + e);
        }
        return commonResponse;
    }

    private StocksDTO getStockIntoStockDTO(Stocks stocks) {
        return StocksDTO.builder()
                .id(String.valueOf(stocks.getId()))
                .stockName(stocks.getStockName())
                .price(String.valueOf(stocks.getPrice()))
                .commonStatus(stocks.getCommonStatus())
                .build();
    }

    public Stocks CastStockDTOIntoStocks(StocksDTO stocksDTO) {
        Stocks stocks = new Stocks();
        stocks.setId(Long.parseLong(stocksDTO.getId()));
        stocks.setStockName(stocksDTO.getStockName());
        stocks.setPrice(Double.parseDouble(stocksDTO.getPrice()));
        stocks.setCommonStatus(stocksDTO.getCommonStatus());

        return stocks;
    }

    private List<String> validateStockDTO(StocksDTO stocksDTO) {
        List<String> validations = new ArrayList<>();
        if (stocksDTO.getId().equals("") && stockRepository.existsById(Long.parseLong(stocksDTO.getId()))){
            validations.add(CommonMsg.RECORDS_ALREADY_EXIT);
        }if(CommonValidation.stringNullValidation(stocksDTO.getStockName())){
            validations.add(CommonMsg.ENTER_STORE_NAME);
        }if(CommonValidation.stringNullValidation(stocksDTO.getPrice())){
            validations.add(CommonMsg.ENTER_STORE_PRICE);
        }
        return validations;

    }

    public CommonResponse deleteStock(String stockId) {
        CommonResponse commonResponse = new CommonResponse();
        Stocks stocks;
        try{
            stocks = findById(stockId);
            stocks.setCommonStatus(CommonStatus.DELETED);
            stockRepository.save(stocks);
            commonResponse.setStatus(true);

        }catch (Exception e){
            LOGGER.error("/**************** Exception in StockService -> saveUpdateStock()" + e);
        }
        return commonResponse;
    }

    public Stocks findById(String stockId) {
        return stockRepository.findById(Long.parseLong(stockId)).get();
    }

    @Transactional
    public CommonResponse getStockById(String stockId) {
        CommonResponse commonResponse = new CommonResponse();
        Stocks stocks;
        try {
            stocks = findById(stockId);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(getStockIntoStockDTO(stocks)));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in StocksService -> getStocks()" + e);
        }
        return commonResponse;
    }

    @Transactional
    public CommonResponse getAllStocks() {
        CommonResponse commonResponse = new CommonResponse();

        try {
            Predicate<Stocks> filterOnStatus = stocks -> stocks.getCommonStatus() != CommonStatus.DELETED;
            List<StocksDTO> stocksDTO = stockRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::getStockIntoStockDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(stocksDTO));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in StocksService -> getAllStocks()" + e);
        }
        return commonResponse;
    }

}
