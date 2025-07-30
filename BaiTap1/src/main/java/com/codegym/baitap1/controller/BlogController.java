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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class BlogController {
    private static final int PAGE_SIZE = 10; // Giảm xuống 10 để dễ thấy phân trang hơn

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String home() {
        return "index";
    }
    // 1. Hiển thị trang chính
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> searchAndPaginate(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 20);
        if (keyword == null || keyword.isEmpty()) {
            return blogService.findAll(pageable).getContent();
        } else {
            return blogService.searchByTitle(keyword, pageable).getContent();
        }
    }


}