package xyz.ncookie.sma.service;

import org.springframework.data.domain.Pageable;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.SchedulePageResponseDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto findSchedule(Long id);

    SchedulePageResponseDto findAllSchedules(Pageable pageable, String modifiedDate, Long userId);

    ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto);

    void deleteSchedule(Long id, ScheduleDeleteRequestDto dto);

}
