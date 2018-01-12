package com.infinera.metro.dnam.acceptance.test.setup;


import com.infinera.metro.dnam.acceptance.test.node.Node;
import com.infinera.metro.dnam.acceptance.test.node.NodeImpl;
import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import lombok.Getter;
import org.junit.Before;
import org.junit.ClassRule;

import java.io.IOException;

;

public class AbstractTwoNodesSetup {
    @Getter protected Node nodeA;
    @Getter protected Node nodeZ;

    @ClassRule
    public static DockerComposeRule dockerComposeRule = DockerComposeRule.builder()
        .file("src/test/resources/docker-compose.yml")
        .waitingForService("nodeA", HealthChecks.toHaveAllPortsOpen())
        .waitingForService("nodeZ", HealthChecks.toHaveAllPortsOpen())
        .shutdownStrategy(ShutdownStrategy.GRACEFUL)
        .build();

    @Before
    public void setup() throws IOException {
        String ipAddressNodeA = DockerUtil.DOCKER_UTIL.getContainerIpAddress(dockerComposeRule, "nodeA");
        String ipAddressNodeZ = DockerUtil.DOCKER_UTIL.getContainerIpAddress(dockerComposeRule, "nodeZ");
        this.nodeA = NodeImpl.createDefault(ipAddressNodeA);
        this.nodeZ = NodeImpl.createDefault(ipAddressNodeZ);
    }
}
