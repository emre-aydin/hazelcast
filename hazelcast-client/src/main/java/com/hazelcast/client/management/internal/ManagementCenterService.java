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

package com.hazelcast.client.management.internal;

import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.protocol.codec.MCExecuteOperationCodec;
import com.hazelcast.client.impl.protocol.codec.MCReadTimedMemberStateCodec;
import com.hazelcast.client.spi.impl.ClientInvocation;
import com.hazelcast.client.util.ClientDelegatingFuture;
import com.hazelcast.cluster.Member;
import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.nio.Address;

public class ManagementCenterService {

    private final HazelcastClientInstanceImpl client;
    private final InternalSerializationService serializationService;

    public ManagementCenterService(HazelcastClientInstanceImpl client,
                                   InternalSerializationService serializationService) {

        this.client = client;
        this.serializationService = serializationService;
    }

    public ClientDelegatingFuture<String> fetchState(Member member) {
        ClientInvocation invocation = new ClientInvocation(
                client,
                MCReadTimedMemberStateCodec.encodeRequest(),
                null,
                member.getAddress()
        );
        return new ClientDelegatingFuture<>(invocation.invoke(),
                serializationService,
                clientMessage -> {
                    MCReadTimedMemberStateCodec.ResponseParameters responseParameters =
                            MCReadTimedMemberStateCodec.decodeResponse(clientMessage);
                    return serializationService.<String>toObject(responseParameters.response);
                },
                true);
    }

    public ClientDelegatingFuture<Object> executeOperationJson(String data, Address address) {
        ClientInvocation invocation = new ClientInvocation(
                client,
                MCExecuteOperationCodec.encodeRequest(data),
                null,
                address
        );
        return new ClientDelegatingFuture<>(
                invocation.invoke(),
                serializationService,
                clientMessage -> {
                    MCExecuteOperationCodec.ResponseParameters responseParameters =
                            MCExecuteOperationCodec.decodeResponse(clientMessage);
                    return responseParameters.response;
                }
        );
    }

//    public ClientDelegatingFuture<Void> changeWanState(Member member, String scheme,
//                                                       String publisher, byte stateId) {
//
//        ClientInvocation invocation = new ClientInvocation(
//                client,
//                MCChangeWanStateCodec.encodeRequest(scheme, publisher, stateId),
//                null,
//                member.getAddress()
//        );
//
//        return new ClientDelegatingFuture<>(
//                invocation.invoke(),
//                serializationService,
//                clientMessage -> null,
//                true);
//    }
}
