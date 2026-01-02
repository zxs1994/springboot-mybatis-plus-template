package work.wendao.hhcd.dto;

import lombok.Data;

@Data
public class CallbackDTO {
    private String msg_signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}