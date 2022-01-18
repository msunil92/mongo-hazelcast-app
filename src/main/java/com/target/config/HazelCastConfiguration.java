package com.target.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/09 10:48 AM
 */

@Configuration
public class HazelCastConfiguration {

    @Value("${hazel.instance.name}")
    String instanceName;

    @Value("${hazel.instance.ttl}")
    Integer ttl;

    @Bean
    public HazelcastInstance hazelcastInstance() {

        Config config = new Config();
        config.setInstanceName(instanceName);
        config.addMapConfig(getConfig());
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    MapConfig getConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(instanceName);
        mapConfig.setEvictionConfig(new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
        );
        mapConfig.setTimeToLiveSeconds(ttl);
        return mapConfig;
    }
}
