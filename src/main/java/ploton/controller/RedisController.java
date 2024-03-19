package ploton.controller;

import org.springframework.beans.factory.annotation.Value;
import ploton.main.model.Note;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.json.DefaultGsonObjectMapper;
import redis.clients.jedis.json.JsonObjectMapper;

public class RedisController {
    private static String redisHost = "localhost";
    private static int redisPort = 6379;
    private static final String KEY_NAME_REDIS = "List";
    private static final int TIME_TO_LIVE = 300;
    private static JedisPool jedisPool = new JedisPool(redisHost, redisPort);
    private static JsonObjectMapper jsonObjectMapper = new DefaultGsonObjectMapper();

    public static void saveInCache(Note note) {
        try (Jedis redis = jedisPool.getResource()) {
            String jsonFromNote = jsonObjectMapper.toJson(note);
            String noteId = String.valueOf(note.getId());
            redis.setex(noteId, TIME_TO_LIVE, jsonFromNote);
            if (redis.exists(KEY_NAME_REDIS)) {
                redis.rpush(KEY_NAME_REDIS, jsonFromNote);
                redis.expire(KEY_NAME_REDIS, TIME_TO_LIVE);
            }
        }
    }


}
