package com.url.shortener.controller;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/urls")
@AllArgsConstructor
public class UrlMappingController {

    private UrlMappingService urlMappingService;

    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShort(@RequestBody Map<String,String> request, Principal principal){

        String originalUrl=request.get("originalUrl");
        User user =userService.findByUsername(principal.getName());

        UrlMappingDTO urlMappingDTO=urlMappingService.createShortUrl(originalUrl,user);

        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        User user=userService.findByUsername(principal.getName());
        List<UrlMappingDTO> urls=urlMappingService.getUrlsByUser(user);

        return ResponseEntity.ok(urls);
    }

    //This api will give record which contain date and clickEvent occured on that date for particular shorturl
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime start=LocalDateTime.parse(startDate,formatter);
        LocalDateTime end=LocalDateTime.parse(endDate,formatter);

        List<ClickEventDTO> clickEventDTOS=urlMappingService.getClickEventByDate(shortUrl,start,end);

        return ResponseEntity.ok(clickEventDTOS);
    }

    //This api will give total clicks for url that user owns
    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal ,@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE;

        User user=userService.findByUsername(principal.getName());
        LocalDate start=LocalDate.parse(startDate,formatter);
        LocalDate end=LocalDate.parse(endDate,formatter);

        Map<LocalDate,Long> totalClicks=urlMappingService.getTotalClicksByUserAndDate(user,start,end);

        return ResponseEntity.ok(totalClicks);
    }

}
