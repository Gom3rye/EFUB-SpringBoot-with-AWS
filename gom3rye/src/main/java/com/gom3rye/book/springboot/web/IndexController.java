package com.gom3rye.book.springboot.web;

import com.gom3rye.book.springboot.config.auth.LoginUser;
import com.gom3rye.book.springboot.config.auth.dto.SessionUser;
import com.gom3rye.book.springboot.service.posts.PostsService;
import com.gom3rye.book.springboot.web.dto.PostsResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){  // 기존에 (User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선되었다. 이젠 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됐다.
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) { // 세션에 저장된 값이 있을 때만 model에 userName으로 등록한다.
            model.addAttribute("loginUserName", user.getName());
        }
        return "index";
    }
    @GetMapping("/posts/save") // 머스테치로 /posts/save 페이지 주소 만들어주었으니까 이 주소에 해당하는 컨트롤러 생성
    // 페이지에 관련된 컨트롤러는 모두 IndexController를 사용한다.
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model)
    {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }

}
