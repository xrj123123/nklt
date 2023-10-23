package com.xrj.entity;

import lombok.Data;
import java.util.Date;

/**
 * ClassName: LoginTicket
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/28 16:11
 * @Version 1.0
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
