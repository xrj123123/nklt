package com.xrj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xrj.entity.DiscussPost;
import com.xrj.mapper.DiscussPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: DiscussPostService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/25 22:15
 * @Version 1.0
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;


    public PageInfo<DiscussPost> findDiscussPosts(int userId, int pageNum) {
        // 开启分页功能
        PageHelper.startPage(pageNum, 10);
        // 开启分页功能后，会自动在sql语句上添加limit关键字
        List<DiscussPost> discussPost = discussPostMapper.selectDiscussPosts(userId);
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(discussPost, 5);

        return pageInfo;
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
