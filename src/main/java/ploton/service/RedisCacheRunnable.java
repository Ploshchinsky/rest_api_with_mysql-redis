package ploton.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ploton.controller.RedisController;
import ploton.main.model.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisCacheRunnable implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RedisCacheRunnable.class);
    private List<Note> noteList = new ArrayList<>();
    private Note note = new Note();

    public RedisCacheRunnable(List<Note> noteList) {
        this.noteList = new ArrayList<>(noteList);
    }

    public RedisCacheRunnable(Note note) {
        this.note = note;
    }

    @Override
    public void run() {
        logger.info("Runnable[RedisCache]: is started!");
        if (note == null) {
            logger.info("Runnable[RedisCache]: for Note's List");
            RedisController.saveInCache(noteList);
        } else {
            logger.info("Runnable[RedisCache]: for single Note");
            RedisController.saveInCache(note);
        }
        logger.info("Runnable[RedisCache]: is finished - List[" + noteList.size() + "] has been save");
    }
}
