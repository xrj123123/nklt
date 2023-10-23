package com.xrj.test;

import com.xrj.entity.DiscussPost;
import com.xrj.mapper.DiscussPostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author XRJ
 * @version 1.0
 * @Data 2023/10/23
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectPost() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101);
        System.out.println(discussPosts);

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
