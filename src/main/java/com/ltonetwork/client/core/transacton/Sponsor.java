package com.ltonetwork.client.core.transacton;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.ltonetwork.client.exceptions.BadMethodCallException;
import com.ltonetwork.client.types.Address;
import com.ltonetwork.client.types.JsonObject;
import com.ltonetwork.client.utils.Encoder;

import java.nio.charset.StandardCharsets;

public class Sponsor extends Transaction {
    private final static long MINIMUM_FEE = 500_000_000;
    private final static byte TYPE = 18;
    private final static byte VERSION = 1;
    private final Address recipient;

    public Sponsor(Address recipient) {
        super(TYPE, VERSION, MINIMUM_FEE);
        this.recipient = recipient;
    }

    public Sponsor(JsonObject json) {
        super(json);
        this.recipient = new Address(json.get("recipient").toString(), super.sender.getChainId());
    }

    public byte[] toBinary() {
        if (this.senderPublicKey == null) {
            throw new BadMethodCallException("Sender public key not set");
        }

        if (this.timestamp == 0) {
            throw new BadMethodCallException("Timestamp not set");
        }

        return Bytes.concat(
                Longs.toByteArray(this.type),
                Longs.toByteArray(this.version),
                new byte[this.getNetwork()],
                this.senderPublicKey.toBase58().getBytes(StandardCharsets.UTF_8),
                Encoder.base58Decode(this.recipient.getAddress()),
                Longs.toByteArray(this.timestamp),
                Longs.toByteArray(this.fee)
        );
    }
}
