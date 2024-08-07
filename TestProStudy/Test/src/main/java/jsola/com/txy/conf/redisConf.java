package jsola.com.txy.conf;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class redisConf {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis的模板对象");
        RedisTemplate objectObjectRedisTemplate = new RedisTemplate();
        //设置连接工厂对象
        objectObjectRedisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置redis key的序列化器
        objectObjectRedisTemplate.setStringSerializer(new StringRedisSerializer());
        return objectObjectRedisTemplate;
    }
}
