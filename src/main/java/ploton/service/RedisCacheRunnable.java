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
    private List<Note> noteList;

    public RedisCacheRunnable(List<Note> noteList) {
        this.noteList = new ArrayList<>(noteList);
    }

    @Override
    public void run() {
        logger.info("Runnable[RedisCache] is started!");
        RedisController.saveInCache(this.noteList);
        logger.info("Runnable[RedisCache] is finished - List[" + noteList.size() + "] has been save");
    }
}
