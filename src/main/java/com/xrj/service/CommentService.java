package com.xrj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xrj.mapper.CommentMapper;
import com.xrj.entity.Comment;
import com.xrj.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import static com.xrj.util.CommunityConstant.ENTITY_TYPE_POST;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    public PageInfo<Comment> findCommentsByEntity(int entityType, int entityId, int pageNum) {
        // 开启分页功能
        PageHelper.startPage(pageNum, 5);
        // 开启分页功能后，会自动在sql语句上添加limit关键字
        List<Comment> comments = commentMapper.selectCommentsByEntity(entityType, entityId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments, 3);
        return pageInfo;
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }

}
