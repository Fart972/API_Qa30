package SchedulerDto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DateDto {
    int dayOfMonth;
    String dayOfWeek;
    int month;
    int year;
}
