package work.wendao.hhcd.service;

import work.wendao.hhcd.dto.CallbackDTO;

public interface CallbackService {
    String info(CallbackDTO dto);
    String directive(CallbackDTO dto);
}
