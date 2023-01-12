package numble.karrot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PresentTime {

    public static ZonedDateTime getPresentTime(){
        LocalDateTime now = LocalDateTime.now();
        return now.atZone(ZoneId.of("Asia/Seoul"));
    }
}
