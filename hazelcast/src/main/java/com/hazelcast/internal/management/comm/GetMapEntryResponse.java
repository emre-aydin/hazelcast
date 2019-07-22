package com.hazelcast.internal.management.comm;

import com.hazelcast.internal.json.JsonObject;

public class GetMapEntryResponse implements MCResponse {
    private final boolean found;
    private final String value;
    private final String clazz;
    private final long memoryCost;
    private final long creationTime;
    private final long expirationTime;
    private final long hits;
    private final long lastAccessTime;
    private final long lastUpdateTime;
    private final long version;

    public static GetMapEntryResponse notFound() {
        return new GetMapEntryResponse(false, null, null, -1, -1, -1, -1, -1, -1, -1);
    }

    public GetMapEntryResponse(boolean found, String value, String clazz, long memoryCost, long creationTime,
                               long expirationTime, long hits, long lastAccessTime, long lastUpdateTime, long version) {
        this.found = found;
        this.value = value;
        this.clazz = clazz;
        this.memoryCost = memoryCost;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.hits = hits;
        this.lastAccessTime = lastAccessTime;
        this.lastUpdateTime = lastUpdateTime;
        this.version = version;
    }

    @Override
    public JsonObject toJson() {
        JsonObject out = new JsonObject();
        out.add("found", found);
        out.add("browse_value", value);
        out.add("browse_class", clazz);
        out.add("memory_cost", memoryCost);
        out.add("date_creation_time", creationTime);
        out.add("date_expiration_time", expirationTime);
        out.add("browse_hits", hits);
        out.add("date_access_time", lastAccessTime);
        out.add("date_update_time", lastUpdateTime);
        out.add("browse_version", version);
        return out;
    }
}
