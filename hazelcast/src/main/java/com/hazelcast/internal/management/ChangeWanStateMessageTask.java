package com.hazelcast.internal.management;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.MCChangeWanStateCodec;
import com.hazelcast.client.impl.protocol.task.AbstractAddressMessageTask;
import com.hazelcast.config.WanPublisherState;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.management.operation.ChangeWanStateOperation;
import com.hazelcast.nio.Address;
import com.hazelcast.nio.Connection;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.security.Permission;

public class ChangeWanStateMessageTask
        extends AbstractAddressMessageTask<MCChangeWanStateCodec.RequestParameters> {

    public ChangeWanStateMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected Address getAddress() {
        return nodeEngine.getThisAddress();
    }

    @Override
    protected Operation prepareOperation() {
        return new ChangeWanStateOperation(
                parameters.schemeName,
                parameters.publisherName,
                WanPublisherState.getByType(parameters.stateId));
    }

    @Override
    protected MCChangeWanStateCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return MCChangeWanStateCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        return MCChangeWanStateCodec.encodeResponse();
    }

    @Override
    public String getServiceName() {
        return ManagementCenterService.SERVICE_NAME;
    }

    public Object[] getParameters() {
        return new Object[]{parameters.schemeName, parameters.publisherName, parameters.stateId};
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public String getDistributedObjectName() {
        return null;
    }
}
