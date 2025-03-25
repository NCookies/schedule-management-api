package xyz.ncookie.sma.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;

import java.util.Optional;

public interface ScheduleRepository {

    Long saveSchedule(Long userId, String task, String password);

    Page<ScheduleResponseDto> findAll(Pageable pageable, String modifiedDate, Long userId);

    Optional<ScheduleResponseDto> findById(Long id);

    int updateSchedule(Long id, Long userId, String task);

    void deleteSchedule(Long id);

    String getPassword(Long scheduleId, String password);

}
