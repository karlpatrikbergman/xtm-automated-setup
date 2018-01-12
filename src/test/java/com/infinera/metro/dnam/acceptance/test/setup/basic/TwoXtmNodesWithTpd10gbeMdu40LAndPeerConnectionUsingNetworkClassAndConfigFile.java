package com.infinera.metro.dnam.acceptance.test.setup.basic;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeConfiguration;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.dnam.acceptance.test.setup.AbstractTwoNodesSetup;
import com.infinera.metro.dnam.acceptance.test.util.ThreadSleepWrapper;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TwoXtmNodesWithTpd10gbeMdu40LAndPeerConnectionUsingNetworkClassAndConfigFile extends AbstractTwoNodesSetup {

    @Ignore
    @Test
    public void applyNetworkFromConfigFile() {
        final NodeConfiguration nodeNetwork = ObjectFromFileUtilJackson.INSTANCE.getObject("simple_layer1_example_network.yaml", NodeConfiguration.class);

        NodeAccessData accessDataNodeA = NodeAccessData.createDefault(nodeA.getIpAddress());
        NodeAccessData accessDataNodeZ = NodeAccessData.createDefault(nodeZ.getIpAddress());
        Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", accessDataNodeA);
        nodeAccessDataMap.put("nodeZ", accessDataNodeZ);

        nodeNetwork.apply(nodeAccessDataMap);

        ThreadSleepWrapper.sleep(3);

        nodeNetwork.delete(nodeAccessDataMap);
    }
}
