package com.tc3.sp_mvc.tweet.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {
    private Twitter twitter;

    @Autowired
    public SearchService(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<Tweet> search(String searchType, List<String> keywords) {
        List<SearchParameters> searchParameters = keywords
                .stream()
                .map(taste->createSearchParam(searchType,taste))
                .collect(Collectors.toList());
        List<Tweet> tweets = searchParameters.stream()
                .map(params -> twitter.searchOperations().search(params))
                .flatMap(searchResults -> searchResults.getTweets().stream())
                .collect(Collectors.toList());
        return tweets;
    }

    private SearchParameters createSearchParam(String searchType, String taste) {
        SearchParameters.ResultType resultType = getResultType(searchType);
        SearchParameters searchParameters = new SearchParameters(taste);
        searchParameters.resultType(resultType);
        searchParameters.count(3);//只查询3条
        return searchParameters;
    }

    private SearchParameters.ResultType getResultType(String searchType){
        for(SearchParameters.ResultType resultType :SearchParameters.ResultType.values()){
            //mixed、recent 或 popular
                if(resultType.name().equalsIgnoreCase(searchType)){
                    return resultType;
                }
        }
        return SearchParameters.ResultType.RECENT;
    }


}