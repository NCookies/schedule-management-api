package xyz.ncookie.sma.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;

import java.util.Optional;

public interface ScheduleRepository {

    Schedule saveSchedule(Schedule schedule);

    Optional<Schedule> findById(Long id);

    Page<ScheduleResponseDto> findAll(Pageable pageable, String modifiedDate, Long userId);

    Optional<Schedule> updateSchedule(Long id, Long userId, String task);

    void deleteSchedule(Long id);

    String getPassword(Long scheduleId, String password);

}
