package com.hackathon.backend.locationsservice.Domain.JSONB_POJOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {
    private DayHours monday;
    private DayHours tuesday;
    private DayHours wednesday;
    private DayHours thursday;
    private DayHours friday;
    private DayHours saturday;
    private DayHours sunday;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayHours {
        private String open;
        private String close;
    }
}