package com.stocks.dto;

import com.stocks.Constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StocksDTO {

    private String id;
    private String stockName;
    private String price;
    private CommonStatus commonStatus;

}
