package com.hazelcast.monitor;

import com.hazelcast.core.HazelcastInstance;

/**
 * Created by emrah on 01/02/2017.
 */
public interface MonitoringService {

    void start();
    void stop();
    void update(TimedMemberState timedMemberState, HazelcastInstance instance) throws Exception;

}
