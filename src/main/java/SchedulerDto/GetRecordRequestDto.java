package SchedulerDto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class GetRecordRequestDto {
    int monthFrom;
    int monthTo;
    int yearFrom;
    int yearTo;
}
