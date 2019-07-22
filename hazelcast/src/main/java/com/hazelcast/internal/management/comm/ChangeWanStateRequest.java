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

import com.hazelcast.config.WanPublisherState;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.internal.json.JsonObject;
import com.hazelcast.internal.management.operation.ChangeWanStateOperation;
import com.hazelcast.spi.InternalCompletableFuture;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.wan.impl.WanReplicationService;

import static com.hazelcast.util.JsonUtil.getString;

public class ChangeWanStateRequest implements MCRequest {

    private final String schemeName;
    private final String publisherName;
    private final WanPublisherState state;

    public ChangeWanStateRequest(JsonObject json) {
        this(
                getString(json, "schemeName"),
                getString(json, "publisherName"),
                WanPublisherState.valueOf(getString(json, "state"))
        );
    }

    public ChangeWanStateRequest(String schemeName, String publisherName, WanPublisherState state) {
        this.schemeName = schemeName;
        this.publisherName = publisherName;
        this.state = state;
    }

    @Override
    public void processMessage(NodeEngine nodeEngine, ExecutionCallback<MCResponse> callback) {
        InternalCompletableFuture<Void> future = nodeEngine.getOperationService().invokeOnTarget(
                WanReplicationService.SERVICE_NAME,
                new ChangeWanStateOperation(schemeName, publisherName, state),
                nodeEngine.getThisAddress());
        future.join();
        callback.onResponse(MCResponse.empty());
    }
}
