package ploton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ploton.main.model.Note;
import ploton.main.model.NoteRepository;
import ploton.service.RedisCacheRunnable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    private Executor executor = Executors.newFixedThreadPool(10);

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

            //Created runnable class and execute inside dedicated thread
            RedisCacheRunnable redisCacheRunnable = new RedisCacheRunnable(note.get());
            executor.execute(redisCacheRunnable);

            return ResponseEntity.ok(note.get());
        }
        return ResponseEntity.notFound().build();
    }

    //Read All
    @GetMapping("/")
    public ResponseEntity<List<Note>> list() {
        List<Note> noteList;
        if (RedisController.listExist()) {
            noteList = RedisController.getList();
            return ResponseEntity.ok(noteList);
        }
        noteList = (List<Note>) noteRepository.findAll();

        //Created runnable class and execute inside dedicated thread
        RedisCacheRunnable redisCacheRunnable = new RedisCacheRunnable(noteList);
        executor.execute(redisCacheRunnable);

        return ResponseEntity.ok(noteList);
    }

}
