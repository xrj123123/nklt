package com.xrj.service;

import com.xrj.mapper.UserMapper;
import com.xrj.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * ClassName: UserService
 * Package: com.xrj.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/25 22:19
 * @Version 1.0
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
