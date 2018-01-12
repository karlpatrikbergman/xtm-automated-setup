package com.infinera.metro.dnam.acceptance.test.setup.boards;

import com.infinera.metro.dnam.acceptance.test.node.AnswerObjects;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeEquipment;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Slot;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Subrack;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.board.BoardSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.client.ClientPortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.line.LinePortConfigAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.attribute.line.LinePortSetAttributes;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.*;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.ClientPort;
import com.infinera.metro.dnam.acceptance.test.node.configuration.port.LinePort;
import com.infinera.metro.dnam.acceptance.test.node.mib.Attribute;
import com.infinera.metro.dnam.acceptance.test.node.mib.Attributes;
import com.infinera.metro.dnam.acceptance.test.setup.AbstractTwoNodesSetup;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BoardsTest extends AbstractTwoNodesSetup {

    @Test
    public void CreateGxp2500SfpTest() {

        final LinePort linePortTx21Rx22 = LinePort.builder()
            .transmitPort(21)
            .receivePort(22)
            .linePortAttribute(LinePortSetAttributes.of("wdmIfExpectedTxLambda", "ch923"))
            .linePortAttribute(LinePortConfigAttributes.of("selectTrafficCombination", "fc2G yes"))
            .build();

        final Gxp2500Sfp gxp2500Sfp = Gxp2500Sfp.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot5)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .linePort(linePortTx21Rx22)
            .build();

        addAndVerifyBoardWasAdded(gxp2500Sfp);
    }

    @Test
    public void CreateMxp028Test() {

        final LinePort linePortTx21Rx22 = LinePort.builder()
            .transmitPort(21)
            .receivePort(22)
            .linePortAttribute(LinePortSetAttributes.of("expectedFrequency", "b1300"))
            .build();

        final ClientPort linePortTx1Rx2 = ClientPort.builder()
            .transmitPort(1)
            .receivePort(2)
            .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
            .build();


        final Mxp028 mxp028 = Mxp028.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot4)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .linePort(linePortTx21Rx22)
            .clientPort(linePortTx1Rx2)
            .build();

        addAndVerifyBoardWasAdded(mxp028);
    }

    @Test
    public void CreateMxp8Test() {
        final LinePort linePortTx17Rx18 = LinePort.builder()
            .transmitPort(17)
            .receivePort(18)
            .linePortAttribute(LinePortSetAttributes.of("expectedFrequency", "b1500"))
            .build();

        final ClientPort linePortTx1Rx2 = ClientPort.builder()
            .transmitPort(1)
            .receivePort(2)
            .clientPortAttribute(ClientPortSetAttributes.of("vc4", "0"))
            .build();

        final Mxp8 mxp8 = Mxp8.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot3)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .linePort(linePortTx17Rx18)
            .clientPort(linePortTx1Rx2)
            .build();

        addAndVerifyBoardWasAdded(mxp8);
    }

    @Test
    public void CreateTpddgbeTest() {

        final LinePort linePortTx13Rx14 = LinePort.builder()
            .transmitPort(13)
            .receivePort(14)
            .linePortAttribute(LinePortSetAttributes.of("wdmIfExpectedTxLambda", "w1550"))
            .build();

        final Tpddgbe tpddgbe = Tpddgbe.builder()
            .subrack(Subrack.subrack1)
            .slot(Slot.slot2)
            .boardAttribute(BoardSetAttributes.of("adminStatus", "up"))
            .linePort(linePortTx13Rx14)
            .build();

        addAndVerifyBoardWasAdded(tpddgbe);
    }

    private void addAndVerifyBoardWasAdded(Board board) {
        final NodeEquipment nodeEquipment = NodeEquipment.builder()
            .board(board)
            .build();

        nodeEquipment.applyTo(nodeA);

        Attributes getBoardAttributes = Attributes.of(
            Attribute.builder()
                .key("equipmentBoardName")
                .build()
        );

        AnswerObjects getBoardAnswerObjects = nodeA.getBoard(board.getBoardEntry(), getBoardAttributes);
        assertNotNull(getBoardAnswerObjects);
    }
}
