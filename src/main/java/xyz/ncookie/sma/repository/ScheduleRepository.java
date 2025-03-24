package xyz.ncookie.sma.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;

import java.util.Optional;

public interface ScheduleRepository {

    Optional<ScheduleResponseDto> saveSchedule(ScheduleRequestDto dto);

    Page<ScheduleResponseDto> findAll(Pageable pageable, String modifiedDate, Long userId);

    Optional<ScheduleResponseDto> findById(Long id);

    int updateSchedule(Long id, String task, String password);

    void deleteSchedule(Long id, String password);

    boolean validPassword(Long scheduleId, String password);

}
