package com.infinera.metro.dnam.acceptance.test.setup.basic;

import com.infinera.metro.dnam.acceptance.test.node.Node;
import com.infinera.metro.dnam.acceptance.test.node.mib.Attribute;
import com.infinera.metro.dnam.acceptance.test.node.mib.Attributes;
import com.infinera.metro.dnam.acceptance.test.node.mib.MpoIdentifier;
import com.infinera.metro.dnam.acceptance.test.node.mib.entry.*;
import com.infinera.metro.dnam.acceptance.test.node.mib.type.*;
import com.infinera.metro.dnam.acceptance.test.setup.AbstractTwoNodesSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO: Figure out if running as a junit test is the best option?
 * TODO: Do we want a way to tear things down?
 *
 * * Run and configure nodes:
 * ./gradlew clean -Dtest.single=TwoXtmNodesWithTpd10gbeMdu40LAndPeerConnectionUsingNodeImplClass test
 *
 * Configure nodes that are already up and running:
 * ./gradlew clean -Dtest.single=TwoXtmNodesWithTpd10gbeMdu40LAndPeerConnectionUsingNodeImplClass -PipAddressNodeA=172.17.0.2 -PipAddressNodeZ=172.17.0.3 test
 */
@Slf4j
public class TwoXtmNodesWithTpd10gbeMdu40LAndPeerConnectionUsingNodeImplClass extends AbstractTwoNodesSetup {

    private final BoardEntry boardEntryTpd10gbeSubrack1Slot2 = BoardEntry.builder()
        .boardType(BoardType.TPD10GBE)
        .subrack(1)
        .slot(2)
        .build();

    private final ClientPortEntry clientPortSubrack1Slot2Tx1Rx2 = ClientPortEntry.builder()
        .moduleType(ModuleType.CLIENT)
        .groupOrTableType(GroupOrTableType.IF)
        .clientPortType(ClientPortType.CLIENT)
        .subrack(1)
        .slot(2)
        .transmitPort(1)
        .receivePort(2)
        .build();

    private final Attributes clientPortExpectedFrequency = Attributes.of(
        Attribute.builder()
            .key("clientIfExpectedTxFrequency")
            .value("w1530")
            .build()
    );

    private final Attributes clientPortSignalFormat = Attributes.of(
        Attribute.builder()
            .key("clientIfConfigurationCommand")
            .value("wan10GbE yes")
            .build()
    );

    private final LinePortEntry linePortEntrySubrack1Slot2Tx3Rx4 = LinePortEntry.builder()
        .moduleType(ModuleType.WDM)
        .groupOrTableType(GroupOrTableType.IF)
        .linePortType(LinePortType.WDM)
        .subrack(1)
        .slot(2)
        .transmitPort(3)
        .receivePort(4)
        .build();

    private final Attributes linePortExpectedFrequency = Attributes.of(
        Attribute.builder()
            .key("expectedFrequency")
            .value("ch939")
            .build()
    );

    private final BoardEntry boardEntryMdu40EvenLSubrack1Slot3 = BoardEntry.builder()
        .boardType(BoardType.MDU40EVENL)
        .subrack(1)
        .slot(3)
        .build();

    private final InternalConnectionEntry internConnSubrack1Slot2Tx3_To_Subrack1Slot3Rx42 = InternalConnectionEntry.builder()
        .fromSubrack(1)
        .fromSlot(2)
        .fromMpoIdentifier(MpoIdentifier.NotPresent())
        .fromPort(3)
        .toSubrack(1)
        .toSlot(3)
        .toMpoIdentifier(MpoIdentifier.NotPresent())
        .toPort(42)
        .build();

    @Ignore
    @Test
    public void applyNetworkUsingNodeImpl() {
        configureNode(getNodeA());
        configureNode(getNodeZ());
        createSymmetricalPeerConnectionBetweenNodes(getNodeA(), getNodeZ());
    }

    private void configureNode(Node node) {
        node.createBoard(boardEntryTpd10gbeSubrack1Slot2);
        node.setClientPortAttributes(clientPortSubrack1Slot2Tx1Rx2, clientPortExpectedFrequency);
        node.configureClientPortAttributes(clientPortSubrack1Slot2Tx1Rx2, clientPortSignalFormat);
        node.setLinePortAttributes(linePortEntrySubrack1Slot2Tx3Rx4, linePortExpectedFrequency);
        node.createBoard(boardEntryMdu40EvenLSubrack1Slot3);
        node.createInternalConnection(internConnSubrack1Slot2Tx3_To_Subrack1Slot3Rx42);
        node.createInternalConnection(internConnSubrack1Slot2Tx3_To_Subrack1Slot3Rx42.reverse());
    }

    private void createSymmetricalPeerConnectionBetweenNodes(Node nodeA, Node nodeZ) {
        createlPeerEntryForMdu40EvenL(nodeA, nodeZ);
        createlPeerEntryForMdu40EvenL(nodeZ, nodeA);
    }
    @SuppressWarnings("Duplicates")
    private void createlPeerEntryForMdu40EvenL(Node transmitNode, Node recieveNode) {
        final PeerEntry transmitPeerEntry = PeerEntry.builder()
            .subrack(1)
            .slot(3)
            .port(81)
            .mpoIdentifier(MpoIdentifier.NotPresent())
            .build();

        final PeerEntry receivePeerEntry = PeerEntry.builder()
            .subrack(1)
            .slot(3)
            .port(82)
            .mpoIdentifier(MpoIdentifier.NotPresent())
            .build();

        final Attributes transmitPeerConfig = Attributes.builder()
            .attribute(Attribute.builder()
                    .key("topoPeerLocalLabel")
                    .value(transmitPeerEntry.getLocalLabel())
                    .build()
            )
            .attribute(Attribute.builder()
                    .key("topoPeerRemoteIpAddress")
                    .value(recieveNode.getIpAddress())
                    .build()
            )
            .attribute(Attribute.builder()
                    .key("topoPeerRemoteLabel")
                    .value(receivePeerEntry.getLocalLabel())
                    .build()
            )
            .build();

        final Attributes receivePeerConfig = Attributes.builder()
            .attribute(Attribute.builder()
                    .key("topoPeerLocalLabel")
                    .value(receivePeerEntry.getLocalLabel())
                    .build()
            )
            .attribute(Attribute.builder()
                    .key("topoPeerRemoteIpAddress")
                    .value(transmitNode.getIpAddress())
                    .build()
            )
            .attribute(Attribute.builder()
                    .key("topoPeerRemoteLabel")
                    .value(transmitPeerEntry.getLocalLabel())
                    .build()
            )
            .build();

        createPeerEntry(transmitNode, transmitPeerEntry, transmitPeerConfig);
        createPeerEntry(recieveNode, receivePeerEntry, receivePeerConfig);
    }

    private void createPeerEntry(Node node, PeerEntry peerEntry, Attributes peerConfig) {
        node.createPeer(peerEntry);
        node.setPeerAttributes(peerEntry, peerConfig);
    }
}
