package com.hazelcast.internal.management;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.MCExecuteOperationJSONCodec;
import com.hazelcast.client.impl.protocol.task.AbstractMessageTask;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.json.Json;
import com.hazelcast.internal.json.JsonObject;
import com.hazelcast.internal.json.JsonValue;
import com.hazelcast.internal.management.comm.ChangeClusterStateRequest;
import com.hazelcast.internal.management.comm.ChangeWanStateRequest;
import com.hazelcast.internal.management.comm.GetMapEntryRequest;
import com.hazelcast.internal.management.comm.MCRequest;
import com.hazelcast.internal.management.comm.MCResponse;
import com.hazelcast.nio.Connection;
import com.hazelcast.util.JsonUtil;

import java.security.Permission;

import static com.hazelcast.internal.management.request.ConsoleRequestConstants.REQUEST_TYPE_CHANGE_CLUSTER_STATE;
import static com.hazelcast.internal.management.request.ConsoleRequestConstants.REQUEST_TYPE_MAP_ENTRY;
import static com.hazelcast.internal.management.request.ConsoleRequestConstants.REQUEST_TYPE_WAN_PUBLISHER;

public class ExecuteOperationJSONMessageTask
        extends AbstractMessageTask<MCExecuteOperationJSONCodec.RequestParameters>
        implements ExecutionCallback<MCResponse> {

    public ExecuteOperationJSONMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected MCExecuteOperationJSONCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return MCExecuteOperationJSONCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        return MCExecuteOperationJSONCodec.encodeResponse((String) response);
    }

    @Override
    protected void processMessage() throws Throwable {
        MCRequest request = resolveRequest();
        request.processMessage(nodeEngine, this);
    }

    private MCRequest resolveRequest() {
        JsonValue jsonValue = Json.parse(parameters.data);
        if (jsonValue.isObject()) {
            JsonObject jsonObject = (JsonObject) jsonValue;
            int type = JsonUtil.getInt(jsonObject, "type", -1);
            JsonValue request = jsonObject.get("request");
            if (request == null || !request.isObject()) {
                throw new IllegalStateException("Incorrect format. Needs to be a JSON object with a 'type' and 'request' property.");
            }
            JsonObject requestObject = request.asObject();
            switch (type) {
                case REQUEST_TYPE_WAN_PUBLISHER:
                    return new ChangeWanStateRequest(requestObject);
                case REQUEST_TYPE_CHANGE_CLUSTER_STATE:
                    return new ChangeClusterStateRequest(requestObject);
                case REQUEST_TYPE_MAP_ENTRY:
                    return new GetMapEntryRequest(requestObject);
                case -1:
                default:
                    throw new UnsupportedOperationException("Unexpected value for request type: " + type);
            }
        } else {
            throw new IllegalStateException("Incorrect format. Needs to be a JSON object with a 'type' and 'request' property.");
        }
    }

    @Override
    public String getServiceName() {
        return ManagementCenterService.SERVICE_NAME;
    }

    public Object[] getParameters() {
        return new Object[]{parameters.data};
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

    @Override
    public void onResponse(MCResponse response) {
        sendResponse(response.toJson().toString());
    }

    @Override
    public void onFailure(Throwable t) {
        handleProcessingFailure(t);
    }
}
