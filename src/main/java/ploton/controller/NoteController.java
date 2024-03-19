package ploton.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @GetMapping("")
    @ResponseBody
    public String hello() {
        return "Hello World!";
    }
}
