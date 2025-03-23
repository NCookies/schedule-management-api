package xyz.ncookie.sma.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;

import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    Page<ScheduleResponseDto> findAllSchedules(Pageable pageable, String modifiedDate, Long userId);

    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);

    int updateSchedule(Long id, String task, String password);

    int deleteSchedule(Long id, String password);

}
