package ploton.controller;

import ploton.main.model.Note;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.json.DefaultGsonObjectMapper;
import redis.clients.jedis.json.JsonObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RedisController {
    private static String redisHost = "localhost";
    private static int redisPort = 6379;
    private static final String KEY_NAME_REDIS = "List";
    private static final int TIME_TO_LIVE = 300;
    private static JedisPool jedisPool = new JedisPool(redisHost, redisPort);
    private static JsonObjectMapper jsonObjectMapper = new DefaultGsonObjectMapper();

    //Create
    public static void saveInCache(Note note) {
        try (Jedis redis = jedisPool.getResource()) {
            String jsonFromNote = jsonObjectMapper.toJson(note);
            String noteId = String.valueOf(note.getId());
            //Redis Set \ Update
            redis.setex(noteId, TIME_TO_LIVE, jsonFromNote);
            //Redis List Update
            update(note);
        }
    }

    public static void saveInCache(List<Note> noteList) {
        try (Jedis redis = jedisPool.getResource()) {
            for (Note note : noteList) {
                String noteId = String.valueOf(note.getId());
                String jsonFromNote = jsonObjectMapper.toJson(note);
                redis.hset(KEY_NAME_REDIS, noteId, jsonFromNote);
            }
            redis.expire(KEY_NAME_REDIS, TIME_TO_LIVE);
        }
    }

    //Read
    public static Note getById(int id) {
        try (Jedis redis = jedisPool.getResource()) {
            String noteId = String.valueOf(id);
            String jsonFromNote = redis.get(noteId);
            return jsonObjectMapper.fromJson(jsonFromNote, Note.class);
        }
    }

    public static List<Note> getList() {
        try (Jedis redis = jedisPool.getResource()) {
            Map<String, String> jsonNoteMap = new HashMap<>(redis.hgetAll(KEY_NAME_REDIS));
            return jsonNoteMap
                    .values()
                    .stream()
                    .map(string -> jsonObjectMapper.fromJson(string, Note.class))
                    .collect(Collectors.toList());
        }
    }

    //Update
    public static void update(Note note) {
        try (Jedis redis = jedisPool.getResource()) {
            String jsonFromNote = jsonObjectMapper.toJson(note);
            String noteId = String.valueOf(note.getId());
            redis.hset(KEY_NAME_REDIS, noteId, jsonFromNote);
            redis.expire(KEY_NAME_REDIS, TIME_TO_LIVE);
        }
    }

    //Delete by ID
    public static boolean delete(int id) {
        try (Jedis redis = jedisPool.getResource()) {
            String noteId = String.valueOf(id);
            redis.del(noteId);
            redis.hdel(KEY_NAME_REDIS, noteId);
            return true;
        }
    }

    //Other
    public static boolean isExist(int id) {
        String noteId = String.valueOf(id);
        try (Jedis redis = jedisPool.getResource()) {
            return redis.exists(noteId);
        }
    }

    public static boolean listExist() {
        try (Jedis redis = jedisPool.getResource()) {
            return redis.exists(KEY_NAME_REDIS);
        }
    }

    private static long getIndexFromRedisList(Jedis redis, String searchingJsonNote) {
        List<String> jsonNotesFromRedis = redis.lrange(KEY_NAME_REDIS, 0, -1);
        for (int i = 0; i < jsonNotesFromRedis.size(); i++) {
            String tempJsonNote = jsonNotesFromRedis.get(i);
            if (tempJsonNote.equals(searchingJsonNote)) {
                return i;
            }
        }
        return -1;
    }
}
