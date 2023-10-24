package com.xrj.entity;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: User
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 20:37
 * @Version 1.0
 */
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status; // 拥护是否激活，默认0未激活，拥护注册后，点击邮箱链接激活拥护
    private String activationCode;  // 激活码
    private String headerUrl;   // 用户头像
    private Date createTime;

}
