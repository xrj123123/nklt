package com.xrj.controller;

import com.github.pagehelper.PageInfo;
import com.xrj.entity.DiscussPost;
import com.xrj.entity.Page;
import com.xrj.entity.User;
import com.xrj.service.DiscussPostService;
import com.xrj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * ClassName: HomeController
 * Package: com.xrj.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 22:26
 * @Version 1.0
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "forward:/index";
    }

    @GetMapping("/index")
    public String getIndexPage(Model model, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum) {

        PageInfo<DiscussPost> page = discussPostService.findDiscussPosts(0, pageNum);
        List<DiscussPost> list = page.getList();

        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("page", page);
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error/500";
    }

    /**
     * 拒绝访问时提示页面
     *
     * @return
     */
    @GetMapping("/denied")
    public String getDeniedPage() {
        return "/error/404";
    }
}
