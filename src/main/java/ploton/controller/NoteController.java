package ploton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ploton.main.model.Note;
import ploton.main.model.NoteRepository;

import java.util.Optional;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("")
    @ResponseBody
    public String hello() {
        return "Hello World!";
    }

    //Create
    @PostMapping("/")
    public ResponseEntity<Note> save(Note note) {
        noteRepository.save(note);
        RedisController.saveInCache(note);
        return new ResponseEntity(note, HttpStatus.CREATED);
    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable("id") int id) {
        //Cache checking...
        if (RedisController.isExist(id)) {
            Note tempNote = RedisController.noteFromJson(id);
            return ResponseEntity.ok(tempNote);
        }
        if (noteRepository.existsById(id)) {
            Optional<Note> note = noteRepository.findById(id);
            RedisController.saveInCache(note.get());
            return ResponseEntity.ok(note.get());
        }
        return ResponseEntity.notFound().build();
    }
}
