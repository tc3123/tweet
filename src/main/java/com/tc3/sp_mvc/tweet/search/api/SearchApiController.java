package com.tc3.sp_mvc.tweet.search.api;

import com.tc3.sp_mvc.tweet.search.LightTweet;
import com.tc3.sp_mvc.tweet.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {
    private SearchService searchService;

    @Autowired
    public SearchApiController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
    public List<LightTweet> search(@PathVariable String searchType,
                                   @MatrixVariable List<String> keywords) {
        return searchService.search(searchType, keywords)
                .stream()
                .map(LightTweet::ofTweet)//?
                .collect(Collectors.toList());
    }
}