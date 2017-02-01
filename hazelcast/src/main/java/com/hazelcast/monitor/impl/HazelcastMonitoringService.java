package com.hazelcast.monitor.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.MemberState;
import com.hazelcast.monitor.MonitoringService;
import com.hazelcast.monitor.TimedMemberState;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.hazelcast.nio.IOUtil.closeResource;

/**
 * Created by emrah on 01/02/2017.
 */
public class HazelcastMonitoringService implements MonitoringService {
    static final int HTTP_SUCCESS = 200;
    static final int CONNECTION_TIMEOUT_MILLIS = 5000;

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void update(TimedMemberState timedMemberState, HazelcastInstance hazelcastInstance) throws Exception {
        sendState(timedMemberState, hazelcastInstance);
    }

    private void sendState(TimedMemberState memberState, HazelcastInstance instance)
            throws InterruptedException, MalformedURLException {
        URL url = new URL("http://localhost:8086/write?db=mancenter");
        if (memberState != null) {
            String clusterName = memberState.getClusterName();
            Set<String> mapNames = new HashSet<String>();
            for (String s : memberState.getInstanceNames()) {
                if (s.startsWith("c:")) {
                    mapNames.add(s.substring(2));
                }
            }

            String uuid = instance.getLocalEndpoint().getUuid();
            for (String mapName : mapNames) {
                for (String metricName : Arrays.asList("ownedEntryCount", "putCount", "heapCost", "removeCount")) {
                    HttpURLConnection connection;
                    OutputStream outputStream = null;
                    OutputStreamWriter writer = null;

                    try {
                        connection = openConnection(url);
                        outputStream = connection.getOutputStream();
                        writer = new OutputStreamWriter(outputStream, "UTF-8");

                        writer.write(createStringForMetric(clusterName, uuid, mapName, memberState.getMemberState(), metricName));
                        writer.flush();
                        outputStream.flush();

                        post(connection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        closeResource(writer);
                        closeResource(outputStream);
                    }
                }
            }
        }
    }

    private String createStringForMetric(String clusterName, String uuid, String mapName, MemberState memberState,
                                         String metricName) throws Exception {
        StringBuilder builder = new StringBuilder();
        Field field = LocalMapStatsImpl.class.getDeclaredField(metricName);
        field.setAccessible(true);
        builder.append("map.").append(metricName)
                .append(",mapName=").append(mapName)
                .append(",clusterName=").append(clusterName)
                .append(",memberUuid=").append(uuid)
                .append(" value=")
                .append(field.get(memberState.getLocalMapStats(mapName)));
        return builder.toString();
    }

    private boolean post(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode != 204) {
            System.out.println("Failed to send response, responseCode:" + responseCode + " url:" + connection.getURL());
        }
        return responseCode == 204;
    }

    private HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
        connection.setReadTimeout(CONNECTION_TIMEOUT_MILLIS);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        return connection;
    }
}
