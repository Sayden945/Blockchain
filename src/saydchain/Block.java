package saydchain;

import java.util.Date;

public class Block {


    public String hash;
    public String previousHash;
    private String data; //our data will be a simple message.
    private final long timeStamp; //Timestamp of block creation

    //Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StrUtil.appSha256(previousHash + Long.toString(timeStamp) + data);
    }
}