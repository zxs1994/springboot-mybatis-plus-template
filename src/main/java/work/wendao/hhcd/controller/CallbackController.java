package work.wendao.hhcd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.wendao.hhcd.common.NoApiWrap;
import work.wendao.hhcd.dto.CallbackDTO;
import work.wendao.hhcd.service.CallbackService;

@Tag(name = "企业微信回调")
@RestController
@RequestMapping("/callback")
public class CallbackController {
    @Autowired
    CallbackService service;

    @NoApiWrap
    @Operation(summary = "数据回调 URL 校验")
    @GetMapping(value = "/info", produces = "text/plain;charset=UTF-8")
    public String info(CallbackDTO dto) {
        return service.info(dto);
    }

    @NoApiWrap
    @Operation(summary = "指令回调 URL 校验")
    @GetMapping(value = "/directive", produces = "text/plain;charset=UTF-8")
    public String directive(CallbackDTO dto) {
        return service.directive(dto);
    }
}