package lab1;

import lombok.Data;

import java.nio.ByteBuffer;

@Data
public class Message {
    private Integer cType;
    private Integer bUserId;
    private String payload;

    public Message(Integer cType, Integer bUserId, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.payload = message;
    }

    public byte[] toBytes() {
        byte[] payloadBytes = payload.getBytes();
        return ByteBuffer.allocate(8 + payloadBytes.length)
                .putInt(cType)
                .putInt(bUserId)
                .put(payloadBytes)
                .array();
    }

}
