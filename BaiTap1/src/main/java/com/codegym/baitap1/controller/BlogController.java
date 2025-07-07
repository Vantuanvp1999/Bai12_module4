package com.codegym.baitap1.controller;

import com.codegym.baitap1.model.Post;
import com.codegym.baitap1.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blogs")
public class BlogController {
    private static final int PAGE_SIZE = 10; // Giảm xuống 10 để dễ thấy phân trang hơn

    @Autowired
    private BlogService blogService;

    // 1. Hiển thị trang chính
    @GetMapping("")
    public String home(Model model) {
        // Lấy trang đầu tiên, sắp xếp theo ID giảm dần (bài mới nhất lên đầu)
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("id").descending());
        Page<Post> postPage = blogService.findAll(pageable);
        model.addAttribute("postPage", postPage);
        return "index";
    }

    // 2. API cho cả TÌM KIẾM và TẢI THÊM
    //    Sử dụng chung một endpoint cho cả hai chức năng để code gọn hơn
    @GetMapping("/api/posts")
    public String getPostsFragment(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Pageable pageable,
            Model model) {

        Page<Post> postPage;
        // Nếu có từ khóa, thực hiện tìm kiếm. Nếu không, lấy tất cả.
        if (!keyword.isEmpty()) {
            postPage = blogService.searchByTitle(keyword, pageable);
        } else {
            postPage = blogService.findAll(pageable);
        }

        model.addAttribute("postPage", postPage);
        // Trả về fragment, không phải cả trang
        return "fragments/post-list-fragment :: postList";
    }
}