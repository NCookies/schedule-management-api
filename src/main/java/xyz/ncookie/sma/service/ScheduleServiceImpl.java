package xyz.ncookie.sma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ncookie.sma.dto.ResponseCode;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.SchedulePageResponseDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.exception.InvalidPasswordException;
import xyz.ncookie.sma.exception.NotFoundException;
import xyz.ncookie.sma.repository.ScheduleRepository;
import xyz.ncookie.sma.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());
        Long savedScheduleId = scheduleRepository.saveSchedule(dto.userId(), dto.task(), encodedPassword);

        return scheduleRepository
                .findById(savedScheduleId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ERROR_WHILE_SAVE));
    }

    @Transactional(readOnly = true)
    @Override
    public ScheduleResponseDto findSchedule(Long id) {
        return scheduleRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_SCHEDULE_ID));
    }

    @Transactional(readOnly = true)
    @Override
    public SchedulePageResponseDto findAllSchedules(Pageable pageable, String modifiedDate, Long userId) {
        // userId를 전달받았지만 유효하지 않은 유저 ID인 경우
        if (userId != -1 && userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_USER_ID);
        }

        Page<ScheduleResponseDto> schedules = scheduleRepository.findAll(pageable, modifiedDate, userId);

        return SchedulePageResponseDto.from(schedules);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long scheduleId, ScheduleUpdateRequestDto dto) {
        validScheduleIdAndPassword(scheduleId, dto.password());

        // 유저 ID가 유효하지 않으면 예외 발생
        int updatedUser = userRepository.updateUserName(dto.userId(), dto.author());
        if (updatedUser == 0) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_USER_ID);
        }

        scheduleRepository.updateSchedule(scheduleId, dto.task());

        return findSchedule(scheduleId);
    }

    @Override
    public void deleteSchedule(Long scheduleId, ScheduleDeleteRequestDto dto) {
        validScheduleIdAndPassword(scheduleId, dto.password());

        scheduleRepository.deleteSchedule(scheduleId);
    }

    /**
     * 일정 ID 검증 + 입력받은 비밀번호와 DB에 저장되어 있는 비밀번호가 일치하는지 확인하는 메서드
     * @param scheduleId 일정 ID
     * @param rawPassword 클라이언트로부터 전달받은 비밀번호 (RAW)
     */
    private void validScheduleIdAndPassword(Long scheduleId, String rawPassword) {
        // 해당 ID의 일정이 존재하지 않으면 예외 발생
        findSchedule(scheduleId);
        
        String storedPassword = scheduleRepository.getPassword(scheduleId, rawPassword);
        if (!passwordEncoder.matches(rawPassword, storedPassword)) {
            throw new InvalidPasswordException();
        }
    }

}
