package com.example.toutiao.controller;

import com.example.toutiao.aspect.LogAspect;
import com.example.toutiao.model.User;
import com.example.toutiao.service.toutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private toutiaoService toutiao;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public  String index(HttpSession session){
        logger.info("visit index");
        return "hello world, "+session.getAttribute("msg")+"<br> Say:"+toutiao.say();
    }

    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value ="key",defaultValue = "nowcoder") String key){
    return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
    }
    @RequestMapping(value={"/vm"})
    public  String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});
        Map<String,String> map = new HashMap<String,String>();
        for(int i=0;i<4;++i){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Jim"));
        return "news";
    }
    @RequestMapping(value={"/request"})
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
            //sb.append(headerNames.nextElement());
        }
        for(Cookie cookie:request.getCookies()){
            sb.append("cookie");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
        }
        sb.append("<br>");
        sb.append("getMethod:"+request.getMethod()+"<br>");
        sb.append("getPathInfo:"+request.getPathInfo()+"<br>");
        sb.append("getQueryString:"+request.getQueryString()+"<br>");
        sb.append("getRequestURI:"+request.getRequestURI()+"<br>");
        sb.append("getAuthType:"+request.getAuthType()+"<br>");
        return sb.toString();
    }



    @RequestMapping(value={"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderId",defaultValue = "a") String nowcoderId,
                           @RequestParam(value ="key",defaultValue = "key") String key,
                           @RequestParam(value="value",defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowcoderId From Cookie:"+nowcoderId;
    }
    @RequestMapping(value="/redirect/{code}")
    //@ResponseBody
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){
        /*RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }*/
        session.setAttribute("msg","jump from redirect");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false)String key){
    if("admin".equals(key)){
        return "hello admin";
    }
    throw new IllegalArgumentException("key 错误了");
    }
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

}
