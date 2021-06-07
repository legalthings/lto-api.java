package legalthings.lto_api.lto.core.transacton;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import legalthings.lto_api.lto.exceptions.BadMethodCallException;
import legalthings.lto_api.lto.exceptions.InvalidArgumentException;
import legalthings.lto_api.utils.main.StringUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MassTransfer extends Transaction {
    private final static long BASE_FEE = 100_000_000;
    private final static long ITEM_FEE = 10_000_000;
    private final static int TYPE = 11;
    private final static int VERSION = 1;
    private final ArrayList<TransferShort> transfers;
    private String attachment = "";

    public MassTransfer(int amount, String recipient) {
        super(TYPE, VERSION, BASE_FEE);
        transfers = new ArrayList<>();
    }

    public void setAttachment(String message, String encoding) {
        this.attachment = switch (encoding) {
            case "base58" -> message;
            case "base64" -> StringUtil.base58Encode(new String(StringUtil.base64Decode(message)), "base58");
            case "raw" -> StringUtil.base58Encode(message, "base58");
//            TODO:
//            case "hex" ->
            default -> throw new InvalidArgumentException(String.format("Failed to encode to %s", encoding));
        };
    }

    public void setAttachment(String message) {
        setAttachment(message, "raw");
    }

    public void addTransfer(String recipient, int amount) {
        transfers.add(new TransferShort(recipient, amount));
        this.fee += ITEM_FEE;
    }

    public byte[] toBinary() {
        if (this.senderPublicKey == null) {
            throw new BadMethodCallException("Sender public key not set");
        }

        if (this.timestamp == 0) {
            throw new BadMethodCallException("Timestamp not set");
        }

        byte[] binaryAttachment = StringUtil.base58Decode(this.attachment);

        ArrayList<Byte> transfersBytes = new ArrayList<>();

        for (TransferShort transfer : transfers) {
            for (Byte rec : StringUtil.base58Decode(transfer.getRecipient())) {
                transfersBytes.add(rec);
            }
            for (Byte am : Ints.toByteArray(transfer.getAmount())) {
                transfersBytes.add(am);
            }
        }

        return Bytes.concat(
                Longs.toByteArray(this.type),
                Longs.toByteArray(this.version),
                StringUtil.base58Decode(this.senderPublicKey),
                Ints.toByteArray(transfers.size()),
                Bytes.toArray(transfersBytes),
                Longs.toByteArray(this.timestamp),
                Longs.toByteArray(this.fee),
                Ints.toByteArray(attachment.length()),
                binaryAttachment
        );
    }
}