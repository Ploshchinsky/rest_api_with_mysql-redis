package ploton.controller;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.json.DefaultGsonObjectMapper;
import redis.clients.jedis.json.JsonObjectMapper;

public class RedisController {
    @Value("${redis.url}")
    private static String redisUrl;
    @Value("${redis.port}")
    private static int redisPort;
    private static final int TIME_TO_LIVE = 300;
    private static JedisPool jedisPool = new JedisPool(redisUrl, redisPort);
    private static JsonObjectMapper jsonObjectMapper = new DefaultGsonObjectMapper();
}
