package com.infinera.metro.dnam.acceptance.test.setup.basic;

import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeEquipment;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Slot;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Subrack;
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
import org.junit.Ignore;
import org.junit.Test;

/**
 * Run and configure nodes:
 * ./gradlew clean -Dtest.single=TwoXtmNodesWithTpd10gbeMdu40LInternalConnectionAndPeerConnectionUsingDslClasses test
 */
public class TwoXtmNodesWithTpd10gbeMdu40LInternalConnectionAndPeerConnectionUsingDslClasses extends AbstractTwoNodesSetup {

    @Ignore
    @Test
    public void applyNetworkUsingDslClasses() {

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

        final ClientPort clientPortTx41_Rx42 = ClientPort.builder()
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
            .clientPort(clientPortTx41_Rx42)
            .linePort(linePortTx81Rx82)
            .build();

        //Same equipment and configuration on both two nodes
        NodeEquipment nodeEquipment = NodeEquipment.builder()
            .board(tpd10gbe)
            .board(mdu40EvenL)
            .build();

        nodeEquipment.applyTo(getNodeA());
        nodeEquipment.applyTo(getNodeZ());

        InternalConnection tpd10gbeTx3ToMdu40EvenLRx42 = InternalConnection.builder()
            .fromPeer(tpd10gbe.getPeer(linePortTx3Rx4.getTransmitPort()))
            .toPeer(mdu40EvenL.getPeer(clientPortTx41_Rx42.getReceivePort()))
            .build();

        tpd10gbeTx3ToMdu40EvenLRx42.applyTo(getNodeA());
        tpd10gbeTx3ToMdu40EvenLRx42.applyTo(getNodeZ());

        InternalConnection mdu40EvenLTx41ToTpd10gbeRx4 = tpd10gbeTx3ToMdu40EvenLRx42.invert();
        mdu40EvenLTx41ToTpd10gbeRx4.applyTo(getNodeA());
        mdu40EvenLTx41ToTpd10gbeRx4.applyTo(getNodeZ());

        PeerConnection peerConnectionNodeANodeZ = PeerConnection.builder()
            .localPeer(mdu40EvenL.getPeer(linePortTx81Rx82.getTransmitPort()))
            .remotePeer(mdu40EvenL.getPeer(linePortTx81Rx82.getReceivePort()))
            .build();
        peerConnectionNodeANodeZ.applyTo(getNodeA(), getNodeZ());

        PeerConnection peerConnectionNodeZtoNodeA = peerConnectionNodeANodeZ.invert();
        peerConnectionNodeZtoNodeA.applyTo(getNodeZ(), getNodeA());
    }
}