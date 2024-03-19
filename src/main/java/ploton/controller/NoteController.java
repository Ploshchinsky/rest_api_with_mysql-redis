package ploton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ploton.main.model.Note;
import ploton.main.model.NoteRepository;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private static NoteRepository noteRepository;

    @GetMapping("")
    @ResponseBody
    public String hello() {
        return "Hello World!";
    }

    //ADD
    @PostMapping("/")
    public ResponseEntity<Note> save(Note note) {
        noteRepository.save(note);
        RedisController.saveInCache(note);
        return ResponseEntity.ok(note);
    }
}
