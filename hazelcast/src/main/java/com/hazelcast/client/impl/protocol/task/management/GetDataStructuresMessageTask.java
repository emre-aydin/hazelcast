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

package com.hazelcast.client.impl.protocol.task.management;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.MCGetDataStructuresCodec;
import com.hazelcast.client.impl.protocol.codec.MCGetDataStructuresCodec.RequestParameters;
import com.hazelcast.client.impl.protocol.task.AbstractCallableMessageTask;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.config.MapConfigReadOnly;
import com.hazelcast.internal.management.dto.DataStructure;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.map.impl.MapService;

import java.security.Permission;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GetDataStructuresMessageTask extends AbstractCallableMessageTask<RequestParameters> {
    public GetDataStructuresMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected Object call() throws Exception {
        return nodeEngine.getHazelcastInstance().getDistributedObjects().stream()
                .map(distObj -> new DataStructure(distObj.getName(), ThreadLocalRandom.current().nextInt(10)))
                .collect(Collectors.toList());
    }

    @Override
    protected RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return MCGetDataStructuresCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        //noinspection unchecked
        List<DataStructure> dataStructures = (List<DataStructure>) response;

        return MCGetDataStructuresCodec.encodeResponse(dataStructures);
    }

    @Override
    public String getServiceName() {
        return MapService.SERVICE_NAME;
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    public String getDistributedObjectName() {
        return null;
    }

    @Override
    public String getMethodName() {
        return "getDataStructures";
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }
}
