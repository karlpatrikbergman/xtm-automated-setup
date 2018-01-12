package com.infinera.metro.dnam.acceptance.test.setup.killedboards;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.*;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.board.BoardSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.client.ClientPortConfigAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.client.ClientPortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.line.LinePortConfigAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.line.LinePortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.*;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.ClientPort;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.LinePort;
import com.infinera.metro.dnam.acceptance.test.node.configuration.topology.InternalConnection;
import com.infinera.metro.dnam.acceptance.test.node.configuration.topology.PeerConnection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//public class KilledBoardsSetup extends AbstractTwoNodesSetup {
public class KilledBoardsSetup {

    private NodeConfiguration nodeConfiguration;

    @Before
    public void before() {
        final Mxp8 mxp8 = Mxp8.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot2)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .clientPort(
                ClientPort.builder()
                    .transmitPort(1)
                    .receivePort(2)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "1"))
                    .clientPortAttribute(ClientPortConfigAttributes.of("configure", "stm1"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(3)
                    .receivePort(4)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(5)
                    .receivePort(6)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(7)
                    .receivePort(8)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(9)
                    .receivePort(10)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(11)
                    .receivePort(12)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(13)
                    .receivePort(14)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "15"))
                    .clientPortAttribute(ClientPortConfigAttributes.of("configure", "stm1"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(15)
                    .receivePort(16)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "16"))
                    .clientPortAttribute(ClientPortConfigAttributes.of("configure", "stm1"))
                    .build()
            )
            .linePort(
                LinePort.builder()
                    .transmitPort(17)
                    .receivePort(18)
                    .linePortAttribute(LinePortSetAttributes.of("muxIfExpectedTxLambda", "ch919"))
                    .build()
            )
            .build();

        Mxp028 mxp028 = Mxp028.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot3)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .clientPort(
                ClientPort.builder()
                    .transmitPort(1)
                    .receivePort(2)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "1"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(3)
                    .receivePort(4)
                    .clientPortAttribute(ClientPortSetAttributes.of("vc4", "5"))
                    .build()
            )
            .linePort(
                LinePort.builder()
                    .transmitPort(21)
                    .receivePort(22)
                    .linePortAttribute(LinePortSetAttributes.of("muxIfExpectedTxLambda", "ch920"))
                    .build()
            )
            .build();

        Gxp2500Sfp gxp2500Sfp = Gxp2500Sfp.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot5)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .linePort(
                LinePort.builder()
                    .transmitPort(21)
                    .receivePort(22)
                    .linePortAttribute(LinePortConfigAttributes.of("selectTrafficCombination", "fc2G yes"))
                    .linePortAttribute(LinePortSetAttributes.of("wdmIfExpectedTxLambda", "ch923"))
                    .build()
            )
            .build();


        Tpd10gbe tpd10gbe = Tpd10gbe.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot8)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .clientPort(
                ClientPort.builder()
                    .transmitPort(1)
                    .receivePort(2)
                    .clientPortAttribute(ClientPortConfigAttributes.of("configure", "lan10GbE yes"))
                    .build()
            )
            .clientPort(
                ClientPort.builder()
                    .transmitPort(5)
                    .receivePort(6)
                    .clientPortAttribute(ClientPortConfigAttributes.of("configure", "wan10GbE yes"))
                    .build()
            )
            .linePort(
                LinePort.builder()
                    .transmitPort(3)
                    .receivePort(4)
                    .linePortAttribute(LinePortSetAttributes.of("wdmIfExpectedTxLambda", "ch926"))
                    .build()
            )
            .linePort(
                LinePort.builder()
                    .transmitPort(7)
                    .receivePort(8)
                    .linePortAttribute(LinePortSetAttributes.of("wdmIfExpectedTxLambda", "ch927"))
                    .build()
            )
            .build();

        Mdu40EvenL mdu40EvenL = Mdu40EvenL.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot15)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .build();

        NodeEquipment nodeEquipment = NodeEquipment.builder()
            .board(mxp8)
            .board(mxp028)
            .board(gxp2500Sfp)
            .board(tpd10gbe)
            .board(mdu40EvenL)
            .build();

        List<InternalConnection> internalConnections = Arrays.asList(
            InternalConnection.builder()
                .fromPeer(mxp8.getPeer(17))
                .toPeer(mdu40EvenL.getPeer(2))
                .build(),
            InternalConnection.builder()
                .fromPeer(mdu40EvenL.getPeer(1))
                .toPeer(mxp8.getPeer(18))
                .build(),

            InternalConnection.builder()
                .fromPeer(mxp028.getPeer(21))
                .toPeer(mdu40EvenL.getPeer(4))
                .build(),
            InternalConnection.builder()
                .fromPeer(mdu40EvenL.getPeer(3))
                .toPeer(mxp028.getPeer(22))
                .build(),

            InternalConnection.builder()
                .fromPeer(gxp2500Sfp.getPeer(21))
                .toPeer(mdu40EvenL.getPeer(10))
                .build(),
            InternalConnection.builder()
                .fromPeer(mdu40EvenL.getPeer(9))
                .toPeer(gxp2500Sfp.getPeer(22))
                .build(),

            InternalConnection.builder()
                .fromPeer(tpd10gbe.getPeer(3))
                .toPeer(mdu40EvenL.getPeer(16))
                .build(),
            InternalConnection.builder()
                .fromPeer(mdu40EvenL.getPeer(15))
                .toPeer(tpd10gbe.getPeer(4))
                .build(),

            InternalConnection.builder()
                .fromPeer(tpd10gbe.getPeer(7))
                .toPeer(mdu40EvenL.getPeer(17))
                .build(),
            InternalConnection.builder()
                .fromPeer(mdu40EvenL.getPeer(18))
                .toPeer(tpd10gbe.getPeer(8))
                .build()

        );

        final PeerConnection peerConnection = PeerConnection.builder()
            .localPeer(mdu40EvenL.getPeer(81))
            .remotePeer(mdu40EvenL.getPeer(82))
            .build();

        final String NODE_A = "nodeA", NODE_Z = "nodeZ";
        final Peers FROM_NODEA_TO_NODEZ = Peers.of(NODE_A, NODE_Z), FROM_NODEZ_TO_NODEA = FROM_NODEA_TO_NODEZ.invert();

        this.nodeConfiguration = NodeConfiguration.builder()
//            .accessDataForNode(NODE_A, NodeAccessData.createDefault(nodeA.getIpAddress()))
//            .accessDataForNode(NODE_Z, NodeAccessData.createDefault(nodeZ.getIpAddress()))
            .accessDataForNode(NODE_A, NodeAccessData.createDefault("172.55.0.2"))
            .accessDataForNode(NODE_Z, NodeAccessData.createDefault("172.55.0.3"))
            .nodeEquipmentForNode(NODE_A, nodeEquipment)
            .nodeEquipmentForNode(NODE_Z, nodeEquipment)
            .internalConnectionForNode(NODE_A, internalConnections)
            .internalConnectionForNode(NODE_Z, internalConnections)
            .peerConnectionForPeers(FROM_NODEA_TO_NODEZ, Collections.singletonList(peerConnection))
            .peerConnectionForPeers(FROM_NODEZ_TO_NODEA, Collections.singletonList(peerConnection))
            .build();
    }

    @Test
    public void applyConfiguration() throws IOException {
//        nodeConfiguration.apply();
        nodeConfiguration.writeToFile("src/test/resources/killedboards/killed_boards_network.yaml");

    }

//    @After
    @Ignore
    @Test
    public void deleteConfiguration() {
        nodeConfiguration.delete();
    }

}
