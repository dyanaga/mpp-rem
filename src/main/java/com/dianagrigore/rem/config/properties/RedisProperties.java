/*
 * Copyright (c) 2019. by 8x8. Inc.
 *  _____      _____
 * |  _  |    |  _  |
 *  \ V /__  __\ V /   ___ ___  _ __ ___
 *  / _ \\ \/ // _ \  / __/ _ \| '_ ` _ \
 * | |_| |>  <| |_| || (_| (_) | | | | | |
 * \_____/_/\_\_____(_)___\___/|_| |_| |_|
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of 8x8 Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with 8x8 Inc.
 */
package com.dianagrigore.rem.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@ConfigurationProperties("pos.redis")
public class RedisProperties {
    private List<CacheSpec> cacheSpecs;

    public List<CacheSpec> getCacheSpecs() {
        return cacheSpecs;
    }

    public void setCacheSpecs(List<CacheSpec> cacheSpecs) {
        this.cacheSpecs = cacheSpecs;
    }

    public static class CacheSpec {
        private String name;
        private Duration ttl;
        private Duration idle;
        private int maxSize;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Duration getTtl() {
            return ttl;
        }

        public void setTtl(Duration ttl) {
            this.ttl = ttl;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public Duration getIdle() {
            return idle;
        }

        public void setIdle(Duration idle) {
            this.idle = idle;
        }
    }
}
