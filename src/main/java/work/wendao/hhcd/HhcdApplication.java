package work.wendao.hhcd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("work.wendao.hhcd.mapper") // 扫描 Mapper 包
public class HhcdApplication {
    public static void main(String[] args) {
        SpringApplication.run(HhcdApplication.class, args);
    }
}