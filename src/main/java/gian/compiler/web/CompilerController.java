package gian.compiler.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * Created by gaojian on 2019/4/15.
 */
@Controller
public class CompilerController {

    @RequestMapping("/")
    public String helloHtml(HashMap<String, Object> map) {
        return "/index";
    }

    @RequestMapping("/{target}")
    public String redirect(@PathVariable("target") String target){
        return target;
    }

}