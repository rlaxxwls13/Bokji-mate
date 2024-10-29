package mover.bokji_mate.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SearchController {

    @GetMapping("search/popular")
    public void getPopularSearches() {

    }

    @GetMapping("/search")
    public void getSearches(@RequestParam String keyword) {

    }
}
