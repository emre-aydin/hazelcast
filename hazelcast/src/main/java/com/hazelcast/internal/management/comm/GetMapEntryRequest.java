/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.internal.management.comm;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.internal.json.JsonObject;
import com.hazelcast.map.IMap;
import com.hazelcast.spi.NodeEngine;

import static com.hazelcast.util.JsonUtil.getString;

public class GetMapEntryRequest implements MCRequest {
    private final String mapName;
    private final String type;
    private final String key;

    public GetMapEntryRequest(JsonObject json) {
        mapName = getString(json, "mapName");
        type = getString(json, "type");
        key = getString(json, "key");
    }

    public GetMapEntryRequest(String type, String mapName, String key) {
        this.type = type;
        this.mapName = mapName;
        this.key = key;
    }

    @Override
    public void processMessage(NodeEngine nodeEngine, ExecutionCallback<MCResponse> executionCallback) {
        IMap<Object, Object> map = nodeEngine.getHazelcastInstance().getMap(mapName);
        EntryView entry = null;
        switch (type) {
            case "string":
                entry = map.getEntryView(key);
                break;
            case "long":
                entry = map.getEntryView(Long.valueOf(key));
                break;
            case "integer":
                entry = map.getEntryView(Integer.valueOf(key));
                break;
        }

        if (entry == null) {
            executionCallback.onResponse(GetMapEntryResponse.notFound());
            return;
        }

        Object value = entry.getValue();

        MCResponse response = new GetMapEntryResponse(
                true,
                value != null ? value.toString() : null,
                value != null ? value.getClass().getName() : null,
                entry.getCost(),
                entry.getCreationTime(),
                entry.getExpirationTime(),
                entry.getHits(),
                entry.getLastAccessTime(),
                entry.getLastUpdateTime(),
                entry.getVersion());
        executionCallback.onResponse(response);
    }
}
