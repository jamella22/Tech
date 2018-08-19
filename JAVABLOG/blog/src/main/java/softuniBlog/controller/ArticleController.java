package softuniBlog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.UserRepository;

import java.security.Principal;

@Controller
public class ArticleController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(Model model){

        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreateConfirm(ArticleBindingModel articleBindingModel, Principal principal) {

        User userfromDb = this.userRepository.findByEmail(principal.getName());
        Article articleEntity = new Article(articleBindingModel.getTitle()
                , articleBindingModel.getContent()
                , userfromDb);

        this.articleRepository.saveAndFlush(articleEntity);

        return "redirect:/";

    }

    @GetMapping("/article/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleDetails(@PathVariable(name = "id")Integer id,Model model){

        Article articleFromDb = this.articleRepository.findOne(id);

        model.addAttribute("article", articleFromDb);
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleEdit(@PathVariable(name = "id")Integer id,Model model,Principal principal){

        Article articleFromDb = this.articleRepository.findOne(id);
        if(!principal.getName().equals(articleFromDb.getAuthor().getEmail())){

            return "redirect:/article/" + articleFromDb.getId();
        }

        model.addAttribute("article", articleFromDb);
        model.addAttribute("view", "article/edit");

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleEditConfirm(@PathVariable(name = "id")Integer id,ArticleBindingModel articleBindingModel,Principal principal){

        Article articleFromDb = this.articleRepository.findOne(id);
        if(!principal.getName().equals(articleFromDb.getAuthor().getEmail())){

            return "redirect:/article/" + articleFromDb.getId();
        }

        articleFromDb.setTitle(articleBindingModel.getTitle());
        articleFromDb.setContent(articleBindingModel.getContent());

        this.articleRepository.saveAndFlush(articleFromDb);

        return "redirect:/";
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleDelete(@PathVariable(name = "id")Integer id,Model model,Principal principal){

        Article articleFromDb = this.articleRepository.findOne(id);
        if(!principal.getName().equals(articleFromDb.getAuthor().getEmail())){

            return "redirect:/article/" + articleFromDb.getId();
        }

        model.addAttribute("article", articleFromDb);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }


    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String articleDeleteConfirm(@PathVariable(name = "id")Integer id,Principal principal){

        this.articleRepository.delete(id);
        return "redirect:/";
    }

}
