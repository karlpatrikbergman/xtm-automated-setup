package com.infinera.metro.dnam.acceptance.test.setup.basic;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.*;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.board.BoardSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.client.ClientPortConfigAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.client.ClientPortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.line.LinePortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.Mdu40EvenL;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.Tpd10gbe;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.ClientPort;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.LinePort;
import com.infinera.metro.dnam.acceptance.test.node.configuration.topology.InternalConnection;
import com.infinera.metro.dnam.acceptance.test.node.configuration.topology.PeerConnection;
import com.infinera.metro.dnam.acceptance.test.setup.AbstractTwoNodesSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
public class TwoXtmNodesWithTpd10gbeMdu40LInternalConnectionAndPeerConnectionUsingNetworkDslClass extends AbstractTwoNodesSetup {

    @Ignore
    @Test
    public void applyNetworkUsingDslClassNetwork() throws IOException {

        final LinePort linePortTx3Rx4 = LinePort.builder()
            .transmitPort(3)
            .receivePort(4)
            .linePortAttribute(LinePortSetAttributes.of("expectedFrequency", "ch939"))
            .build();

        final ClientPort clientPortTx1Rx2 = ClientPort.builder()
            .transmitPort(1)
            .receivePort(2)
            .clientPortAttribute(ClientPortSetAttributes.of("clientIfExpectedTxFrequency", "w1530"))
            .clientPortAttribute(ClientPortConfigAttributes.of("clientIfConfigurationCommand", "wan10GbE yes"))
            .build();

        final Tpd10gbe tpd10gbe = Tpd10gbe.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot2)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up")) //Admin status could be set to up by default?
            .clientPort(clientPortTx1Rx2)
            .linePort(linePortTx3Rx4)
            .build();

        final ClientPort clientPortTx41Rx42 = ClientPort.builder()
            .transmitPort(41)
            .receivePort(42)
//            .clientPortAttribute(ClientPortSetAttributes.of("descr", "My description")) //TODO: Rest api has changed?
            .build();

        final LinePort linePortTx81Rx82 = LinePort.builder()
            .transmitPort(81)
            .receivePort(82)
            .linePortAttribute(LinePortSetAttributes.of("descr", "My description"))
            .build();

        final Mdu40EvenL mdu40EvenL = Mdu40EvenL.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot3)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .clientPort(clientPortTx41Rx42)
            .linePort(linePortTx81Rx82)
            .build();

        //Same equipment and configuration on both two nodes
        final NodeEquipment tdb10bgeAndMdu40EvenlNodeEquipment = NodeEquipment.builder()
            .board(tpd10gbe)
            .board(mdu40EvenL)
            .build();

        final InternalConnection internalConnectionTpd10gbeTx3ToMdu40EvenLRx42 = InternalConnection.builder()
            .fromPeer(tpd10gbe.getPeer(linePortTx3Rx4.getTransmitPort()))
            .toPeer(mdu40EvenL.getPeer(clientPortTx41Rx42.getReceivePort()))
            .build();

        final PeerConnection peerConnectionNodeANodeZ = PeerConnection.builder()
            .localPeer(mdu40EvenL.getPeer(linePortTx81Rx82.getTransmitPort()))
            .remotePeer(mdu40EvenL.getPeer(linePortTx81Rx82.getReceivePort()))
            .build();

        final String NODE_A = "nodeA", NODE_Z = "nodeZ";
        final Peers FROM_NODEA_TO_NODEZ = Peers.of(NODE_A, NODE_Z), FROM_NODEZ_TO_NODEA = FROM_NODEA_TO_NODEZ.invert();

        NodeConfiguration nodeNetwork = NodeConfiguration.builder()
            .accessDataForNode(NODE_A, NodeAccessData.createDefault(nodeA.getIpAddress()))
            .accessDataForNode(NODE_Z, NodeAccessData.createDefault(nodeZ.getIpAddress()))
            .nodeEquipmentForNode(NODE_A, tdb10bgeAndMdu40EvenlNodeEquipment)
            .nodeEquipmentForNode(NODE_Z, tdb10bgeAndMdu40EvenlNodeEquipment)
            .internalConnectionForNode(NODE_A, Arrays.asList(internalConnectionTpd10gbeTx3ToMdu40EvenLRx42, internalConnectionTpd10gbeTx3ToMdu40EvenLRx42.invert()))
            .internalConnectionForNode(NODE_Z, Arrays.asList(internalConnectionTpd10gbeTx3ToMdu40EvenLRx42, internalConnectionTpd10gbeTx3ToMdu40EvenLRx42.invert()))
            .peerConnectionForPeers(FROM_NODEA_TO_NODEZ, Collections.singletonList(peerConnectionNodeANodeZ))
            .peerConnectionForPeers(FROM_NODEZ_TO_NODEA, Collections.singletonList(peerConnectionNodeANodeZ.invert()))
            .build();
//        nodeNetwork.apply();

        nodeNetwork.writeToFile("simple_layer1_example_network.yaml");
    }
}