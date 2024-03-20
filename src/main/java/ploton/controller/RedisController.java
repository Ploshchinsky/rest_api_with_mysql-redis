package ploton.controller;

import ploton.main.model.Note;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.json.DefaultGsonObjectMapper;
import redis.clients.jedis.json.JsonObjectMapper;

import java.util.ArrayList;
import java.util.List;

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
            //Redis Set
            redis.setex(noteId, TIME_TO_LIVE, jsonFromNote);
            //Redis List Update
            redis.rpush(KEY_NAME_REDIS, jsonFromNote);
            redis.expire(KEY_NAME_REDIS, TIME_TO_LIVE);

        }
    }

    public static void saveInCache(List<Note> noteList) {
        try (Jedis redis = jedisPool.getResource()) {
            for (Note note : noteList) {
                String jsonFromNote = jsonObjectMapper.toJson(note);
                redis.rpush(KEY_NAME_REDIS, jsonFromNote);
            }
            redis.expire(KEY_NAME_REDIS, TIME_TO_LIVE);
        }
    }

    //Read
    public static List<Note> getList() {
        try (Jedis redis = jedisPool.getResource()) {
            List<String> noteListJson = redis.lrange(KEY_NAME_REDIS, 0, -1);
            List<Note> noteList = new ArrayList<>();
            for (String s : noteListJson) {
                noteList.add(jsonObjectMapper.fromJson(s, Note.class));
            }
            return noteList;
        }
    }

    //JSON
    public static Note noteFromJson(int id) {
        try (Jedis redis = jedisPool.getResource()) {
            String noteId = String.valueOf(id);
            String jsonFromNote = redis.get(noteId);
            return jsonObjectMapper.fromJson(jsonFromNote, Note.class);
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


}
