package com.tc3.sp_mvc.tweet.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class TweetController {

    @Autowired
    private Twitter twitter;


//    @RequestMapping("/")
    public String home(){
        return "searchPage";
    }

    //POST表单 RedirectAttributes redirect addFlashAttribute
    @RequestMapping(value="postSearch",method=RequestMethod.POST)
    public String postSearch(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String search = httpServletRequest.getParameter("search");
        if (search.toLowerCase().contains("struts")) {
//            在该请求的时间范围内一直存活，直到页面渲染完成才消失
            redirectAttributes.addFlashAttribute("error", "Try using spring instead!");
            return "redirect:/";
        }
        redirectAttributes.addAttribute("search",search);
        return "redirect:result";
    }


    @RequestMapping(value="result")
    public String result(@RequestParam(defaultValue = "MVC") String search, Model model) {
        SearchOperations searchOperations = twitter.searchOperations();
        SearchResults searchResults = searchOperations.search(search);
        List<Tweet> tweets = searchResults.getTweets();
        model.addAttribute("tweets", tweets);
        model.addAttribute("search", search);
        return "resultPage";
    }


}