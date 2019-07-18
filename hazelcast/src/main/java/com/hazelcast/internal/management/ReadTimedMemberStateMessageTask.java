package com.hazelcast.internal.management;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.MCReadTimedMemberStateCodec;
import com.hazelcast.client.impl.protocol.task.AbstractMessageTask;

import com.hazelcast.instance.impl.Node;
import com.hazelcast.nio.Connection;
import com.hazelcast.spi.ExecutionService;

import java.security.Permission;
import java.util.concurrent.Executor;

public class ReadTimedMemberStateMessageTask
        extends AbstractMessageTask<MCReadTimedMemberStateCodec.RequestParameters> {

    public ReadTimedMemberStateMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected MCReadTimedMemberStateCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return MCReadTimedMemberStateCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        return MCReadTimedMemberStateCodec.encodeResponse((String) response);
    }

    @Override
    protected void processMessage() {
        final ManagementCenterService managementCenterService = nodeEngine.getManagementCenterService();
        ExecutionService executionService = nodeEngine.getExecutionService();
        Executor executor = executionService.getExecutor(ExecutionService.ASYNC_EXECUTOR);
        executor.execute(() -> sendResponse(managementCenterService.getTimedMemberState().toJson().toString()));
    }

    @Override
    public String getServiceName() {
        return ManagementCenterService.SERVICE_NAME;
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
        return null;
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }
}
