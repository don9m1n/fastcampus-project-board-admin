package com.fastcampus.projectboardamdin.controller;

import com.fastcampus.projectboardamdin.dto.ArticleDto;
import com.fastcampus.projectboardamdin.dto.response.ArticleResponse;
import com.fastcampus.projectboardamdin.service.ArticleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/management/articles")
@RequiredArgsConstructor
public class ArticleManagementController {

    private final ArticleManagementService articleManagementService;

    @GetMapping
    public String articles(Model model) {

        model.addAttribute(
                "articles",
                articleManagementService.getArticles().stream().map(ArticleResponse::withoutContent).toList()
        );

        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ArticleResponse article(@PathVariable Long id) {

        return ArticleResponse.withContent(articleManagementService.getArticle(id));
    }

    @PostMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {

        articleManagementService.deleteArticle(id);

        return "redirect:/management/articles";
    }

}
