package com.example.seckill_shop.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tyz
 * @since 2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nickname;

    /**
     * MD5（MD5pass明文 + 固定slat） + slat
     */
    private String password;

    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 最后一次登录时间
     */
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;


}
