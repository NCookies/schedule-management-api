package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;
import xyz.ncookie.sma.repository.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        return scheduleRepository.saveSchedule(Schedule.from(dto));
    }

    @Override
    public ScheduleResponseDto findSchedule(Long id) {
        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String author) {
        return scheduleRepository.findAllSchedules(modifiedDate, author);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto) {
        int updatedSchedule = scheduleRepository.updateSchedule(id, dto.task(), dto.author(), dto.password());
        
        // TODO: @Valid 사용해서 dto 객체 유효성 검사 해야함
        
        // id와 password 모두 일치하는 데이터가 없음
        if (updatedSchedule == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID 또는 비밀번호가 유효하지 않습니다.");
        }

        return findSchedule(id);
    }

    @Override
    public void deleteSchedule(Long id, ScheduleDeleteRequestDto dto) {
        int deletedSchedule = scheduleRepository.deleteSchedule(id, dto.password());

        if (deletedSchedule == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID 또는 비밀번호가 유효하지 않습니다.");
        }
    }

}
