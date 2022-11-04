package com.example.seckill_shop.exception;

import com.example.seckill_shop.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{

    private RespBeanEnum respBeanEnum;

}
