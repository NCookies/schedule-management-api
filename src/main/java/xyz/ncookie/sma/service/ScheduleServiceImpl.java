package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.SchedulePageResponseDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.exception.InvalidPasswordException;
import xyz.ncookie.sma.exception.NoSuchIdException;
import xyz.ncookie.sma.repository.ScheduleRepository;
import xyz.ncookie.sma.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        return scheduleRepository.saveSchedule(dto).orElseThrow(() -> new NoSuchIdException("데이터 저장 중 문제가 발생했습니다."));
    }

    @Transactional(readOnly = true)
    @Override
    public ScheduleResponseDto findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(NoSuchIdException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public SchedulePageResponseDto findAllSchedules(Pageable pageable, String modifiedDate, Long userId) {
        // userId를 전달받았지만 유효하지 않은 유저 ID인 경우
        if (userId != -1 && userRepository.findById(userId).isEmpty()) {
            throw new NoSuchIdException("존재하지 않는 회원의 ID 입니다.");
        }

        Page<ScheduleResponseDto> schedules = scheduleRepository.findAll(pageable, modifiedDate, userId);

        return SchedulePageResponseDto.from(schedules);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto dto) {
        if (!scheduleRepository.validPassword(scheduleId, dto.password())) {
            throw new InvalidPasswordException();
        }

        int updatedUser = userRepository.updateUserName(dto.userId(), dto.author());
        if (updatedUser == 0) {
            throw new NoSuchIdException("존재하지 않는 회원의 ID 입니다.");
        }

        scheduleRepository.updateSchedule(scheduleId, dto.task(), dto.password());

        return findSchedule(scheduleId);
    }

    @Override
    public void deleteSchedule(Long scheduleId, ScheduleDeleteRequestDto dto) {
        if (!scheduleRepository.validPassword(scheduleId, dto.password())) {
            throw new InvalidPasswordException();
        }

        scheduleRepository.deleteSchedule(scheduleId, dto.password());
    }

}
