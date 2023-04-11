package com.autosale.shop.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CacheConfiguration {
    @Caching(
            evict = {
                    @CacheEvict("user"),
                    @CacheEvict("userByUsername")
            }
    )
    @Scheduled(cron = "* 0 * * * *")
    void cacheClear() {
    }
}
