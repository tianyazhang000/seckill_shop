package com.example.seckill_shop.vo;

import com.example.seckill_shop.utils.ValidatorUtil;
import com.example.seckill_shop.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(value);
        } else {
            if(StringUtils.isEmpty(value)){
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
