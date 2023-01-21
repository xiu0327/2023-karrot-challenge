package numble.karrot;

import java.time.*;

public class TimeService {

    public static ZonedDateTime getPresentTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.atZone(ZoneId.of("Asia/Seoul"));

    }

    public static String replaceProductDate(LocalDateTime date){
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
        Duration duration = Duration.between(date, now);
        Period period = Period.between(date.toLocalDate(), now.toLocalDate());

        if(duration.toHours()<1){ return duration.toMinutes() + " 분 전";}
        if(duration.toDays()<1){ return duration.toHours() + " 시간 전";}

        if(period.getMonths()<1){ return period.getDays() + " 일 전";}
        if (period.getYears()<1) { return period.getMonths() + " 달 전";}

        return period.getYears() + " 년 전";
    }
}
