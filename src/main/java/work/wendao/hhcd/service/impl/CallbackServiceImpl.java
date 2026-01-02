package work.wendao.hhcd.service.impl;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.stereotype.Service;
import work.wendao.hhcd.dto.CallbackDTO;
import work.wendao.hhcd.service.CallbackService;

@Service
public class CallbackServiceImpl implements CallbackService {
    @Override
    public String info(CallbackDTO dto) {
        return verification(dto);
    }

    @Override
    public String directive(CallbackDTO dto) {
        return verification(dto);
    }

    public String verification(CallbackDTO dto) {
        String sToken = "BvLq2JbUmWcgA3nVWrqn8";
        String sCorpID = "ww7f44f2cc03995977";
        String sEncodingAESKey = "XbR2en20Q5qH4xNlv9LVGJfEK58rWXQA5RrkXr3cXEB";
        String echostr = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
            echostr = wxcpt.VerifyURL(dto.getMsg_signature(), dto.getTimestamp(), dto.getNonce(), dto.getEchostr());
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }

        return echostr;
    }
}
