package lk.epic.springBoot_middleware.controller;

import lk.epic.springBoot_middleware.ISO8583Message.ISO8583Message;
import lk.epic.springBoot_middleware.dto.ISO8583FieldsDTO;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.web.bind.annotation.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
@RequestMapping("/api/v1/springBoot_middleware")
@CrossOrigin
public class SpringBoot_Middleware {
    private final static int PORT = 5000;
    private static Socket remoteSocket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    @PostMapping
    public void workWithJsonAndISO8583(@RequestBody ISO8583FieldsDTO allFields) {
        try {
            System.out.println(allFields);

            remoteSocket = new Socket("localhost", PORT);
            dataInputStream = new DataInputStream(remoteSocket.getInputStream());
            dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());

            ISOMsg message = ISO8583Message.getInstance().getIsoMessage();
            message.setMTI(allFields.getF0());
            message.set("3", allFields.getF3());
            message.set("4", allFields.getF4());
            message.set("11", allFields.getF11());
            message.set("22", allFields.getF22());
            message.set("24", allFields.getF24());
            message.set("25", allFields.getF25());

            message.set(35, allFields.getF35());
            message.set(41, allFields.getF41());
            message.set(42, allFields.getF41());
            message.set(55, allFields.getF55());
            message.set(62, allFields.getF62());

            message.setPackager(new ISO87APackager());
            byte[] packedData = message.pack();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
