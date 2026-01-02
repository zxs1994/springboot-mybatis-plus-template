package work.wendao.hhcd.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.wendao.hhcd.util.EnumUtils;

import java.util.Map;

@RestController
@RequestMapping("/enums")
@Tag(name = "枚举", description = "枚举控制器")
public class EnumController {

    @GetMapping("/all")
    public Map<String, Object> allEnums() {
        return EnumUtils.loadAllEnums("work.wendao.hhcd.enums");
    }
}
