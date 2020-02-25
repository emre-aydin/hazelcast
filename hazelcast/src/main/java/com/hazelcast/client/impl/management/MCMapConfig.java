/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.client.impl.management;

import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MaxSizePolicy;

public class MCMapConfig {
    private boolean readBackupData;
    private int maxSize;
    private int backupCount;
    private int maxIdleSeconds;
    private int asyncBackupCount;
    private int timeToLiveSeconds;
    private String mergePolicy;
    private MaxSizePolicy maxSizePolicy;
    private EvictionPolicy evictionPolicy;
    private InMemoryFormat inMemoryFormat;

    public InMemoryFormat getInMemoryFormat() {
        return inMemoryFormat;
    }

    public int getBackupCount() {
        return backupCount;
    }

    public int getAsyncBackupCount() {
        return asyncBackupCount;
    }

    public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public int getMaxIdleSeconds() {
        return maxIdleSeconds;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public MaxSizePolicy getMaxSizePolicy() {
        return maxSizePolicy;
    }

    public boolean isReadBackupData() {
        return readBackupData;
    }

    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    public String getMergePolicy() {
        return mergePolicy;
    }

    public void setReadBackupData(boolean readBackupData) {
        this.readBackupData = readBackupData;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setBackupCount(int backupCount) {
        this.backupCount = backupCount;
    }

    public void setMaxIdleSeconds(int maxIdleSeconds) {
        this.maxIdleSeconds = maxIdleSeconds;
    }

    public void setAsyncBackupCount(int asyncBackupCount) {
        this.asyncBackupCount = asyncBackupCount;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public void setMergePolicy(String mergePolicy) {
        this.mergePolicy = mergePolicy;
    }

    public void setMaxSizePolicy(MaxSizePolicy maxSizePolicy) {
        this.maxSizePolicy = maxSizePolicy;
    }

    public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public void setInMemoryFormat(InMemoryFormat inMemoryFormat) {
        this.inMemoryFormat = inMemoryFormat;
    }
}
