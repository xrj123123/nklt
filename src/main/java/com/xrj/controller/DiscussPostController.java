package com.xrj.controller;

import com.github.pagehelper.PageInfo;
import com.xrj.entity.Comment;
import com.xrj.entity.DiscussPost;
import com.xrj.entity.User;
import com.xrj.service.CommentService;
import com.xrj.service.DiscussPostService;
import com.xrj.service.LikeService;
import com.xrj.service.UserService;
import com.xrj.util.CommunityConstant;
import com.xrj.util.CommunityUtil;
import com.xrj.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.xrj.util.CommunityConstant.ENTITY_TYPE_COMMENT;
import static com.xrj.util.CommunityConstant.ENTITY_TYPE_POST;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 Model model) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 帖子的赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 是否点赞
        int likeStatus = likeService.findEntityLikeStatus(post.getUserId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        PageInfo<Comment> commentsPage = commentService.
                findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), pageNum);
        List<Comment> commentList = commentsPage.getList();

        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 赞的数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 是否点赞
                likeStatus = likeService.findEntityLikeStatus(post.getUserId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复列表
                PageInfo<Comment> replyPage = commentService.
                        findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0);
                List<Comment> replyList = replyPage.getList();
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 回复的赞
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        // 回复是否点赞
                        likeStatus = likeService.findEntityLikeStatus(post.getUserId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVo.put("replyPage", replyPage);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        model.addAttribute("page", commentsPage);

        return "/site/discuss-detail";
    }


}
