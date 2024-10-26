package mover.bokji_mate.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/calendar")
@Slf4j
public class CalendarController {

    // 캘린더에 멤버가 스크랩한 정책 보내기
    @GetMapping()
    public void scrapPolicy() {

    }

    //
}
