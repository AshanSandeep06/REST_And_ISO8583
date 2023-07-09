package lk.epic.springBoot_middleware.controller;

import lk.epic.springBoot_middleware.ISO8583Message.ISO8583Message;
import lk.epic.springBoot_middleware.dto.ISO8583FieldsDTO;
import lk.epic.springBoot_middleware.entity.ISO8583Fields;
import lk.epic.springBoot_middleware.util.ResponseUtil;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    public ResponseUtil workWithJsonAndISO8583(@RequestBody ISO8583FieldsDTO allFields) {
        try {
            System.out.println(allFields);

            remoteSocket = new Socket("localhost", PORT);
            dataInputStream = new DataInputStream(remoteSocket.getInputStream());
            dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());

            ISO87APackager packager = new ISO87APackager();

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
            message.set(42, allFields.getF42());
            message.set(55, allFields.getF55());
            message.set(62, allFields.getF62());

            message.setPackager(packager);
            byte[] packedData = message.pack();

            dataOutputStream.writeInt(packedData.length);
            dataOutputStream.write(packedData);
            dataOutputStream.flush();

            int responseLength = dataInputStream.readInt();
            byte[] responseData = new byte[responseLength];
            dataInputStream.readFully(responseData);

            //Unpack the response message
            ISOMsg iso8583Response = ISO8583Message.getInstance().getIsoMessage();
            iso8583Response.setPackager(packager);
            iso8583Response.unpack(responseData);

            ISO8583Fields iso8583Entity = new ISO8583Fields();

            iso8583Entity.setF0(iso8583Response.getString(0));
            iso8583Entity.setF3(iso8583Response.getString(3));
            iso8583Entity.setF4(iso8583Response.getString(4));
            iso8583Entity.setF11(iso8583Response.getString(11));
            iso8583Entity.setF22(iso8583Response.getString(22));
            iso8583Entity.setF24(iso8583Response.getString(24));
            iso8583Entity.setF25(iso8583Response.getString(25));
            iso8583Entity.setF35(iso8583Response.getString(35));
            iso8583Entity.setF41(iso8583Response.getString(41));
            iso8583Entity.setF42(iso8583Response.getString(42));
            iso8583Entity.setF55(iso8583Response.getString(55));
            iso8583Entity.setF62(iso8583Response.getString(62));

            return new ResponseUtil("OK", ISOUtil.hexString(iso8583Response.pack()), iso8583Entity);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
