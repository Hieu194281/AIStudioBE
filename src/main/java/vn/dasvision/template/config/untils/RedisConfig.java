package vn.dasvision.template.config.untils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
public class RedisConfig  implements  RedisConfiguration{
    @Value("${redis.host}")
    private String redisHost ;

    @Value("${redis.port}")
    private int redisPort;


    @Bean
    public LettuceConnectionFactory redisConnectionFactory(String redisHost,int redisPort) {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setPort(6379);
//        redisStandaloneConfiguration.setHostName("localhost");

//        return new LettuceConnectionFactory(redisStandaloneConfiguration);

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));

    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(this.redisConnectionFactory(this.redisHost, this.redisPort));

        template.setDefaultSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.afterPropertiesSet();

        return template;
    }
}

