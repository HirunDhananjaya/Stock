package com.stocks.entity;

import com.stocks.Constant.CommonStatus;
import com.stocks.util.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stocks {

    @Id
    @Column
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = "com.stocks.util.SnowflakeIdGenerator")
    private Long id;

    @Column
    private String stockName;

    @Column
    private Double price;

    @Enumerated
    private CommonStatus commonStatus;


}
