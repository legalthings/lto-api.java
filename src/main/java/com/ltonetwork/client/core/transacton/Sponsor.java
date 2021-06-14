package com.ltonetwork.client.core.transacton;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.ltonetwork.client.exceptions.BadMethodCallException;
import com.ltonetwork.client.exceptions.InvalidArgumentException;
import com.ltonetwork.client.utils.CryptoUtil;
import com.ltonetwork.client.utils.Encoder;

public class Sponsor extends Transaction {
    private final static long MINIMUM_FEE = 500_000_000;
    private final static int TYPE = 18;
    private final static int VERSION = 1;
    private final String recipient;

    public Sponsor(String recipient) {
        super(TYPE, VERSION, MINIMUM_FEE);

        if (!CryptoUtil.isValidAddress(recipient, "base58")) {
            throw new InvalidArgumentException("Invalid recipient address; is it base58 encoded?");
        }

        this.recipient = recipient;
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
                Encoder.base58Decode(this.senderPublicKey),
                Encoder.base58Decode(this.recipient),
                Longs.toByteArray(this.timestamp),
                Longs.toByteArray(this.fee)
        );
    }
}