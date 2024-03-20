package ploton.service;

import ploton.controller.RedisController;
import ploton.main.model.Note;

import java.util.ArrayList;
import java.util.List;

public class RedisCacheRunnable implements Runnable {
    private List<Note> noteList;

    public RedisCacheRunnable(List<Note> noteList) {
        this.noteList = new ArrayList<>(noteList);
    }

    @Override
    public void run() {
        RedisController.saveInCache(this.noteList);
    }
}
