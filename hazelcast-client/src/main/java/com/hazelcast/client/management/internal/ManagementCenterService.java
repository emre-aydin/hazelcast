package com.hazelcast.client.management.internal;

import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.protocol.codec.MCChangeWanStateCodec;
import com.hazelcast.client.impl.protocol.codec.MCReadTimedMemberStateCodec;
import com.hazelcast.client.spi.impl.ClientInvocation;
import com.hazelcast.client.util.ClientDelegatingFuture;
import com.hazelcast.cluster.Member;
import com.hazelcast.internal.serialization.InternalSerializationService;

public class ManagementCenterService {

    private final HazelcastClientInstanceImpl client;
    private final InternalSerializationService serializationService;

    public ManagementCenterService(HazelcastClientInstanceImpl client,
                                   InternalSerializationService serializationService) {

        this.client = client;
        this.serializationService = serializationService;
    }

    public ClientDelegatingFuture<Void> changeWanState(Member member, String scheme,
                                                       String publisher, byte stateId) {

        ClientInvocation invocation = new ClientInvocation(
                client,
                MCChangeWanStateCodec.encodeRequest(scheme, publisher, stateId),
                null,
                member.getAddress()
        );

        return new ClientDelegatingFuture<>(
                invocation.invoke(),
                serializationService,
                clientMessage -> null,
                true);
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
}
