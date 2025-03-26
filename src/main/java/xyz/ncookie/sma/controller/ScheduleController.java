package xyz.ncookie.sma.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.ncookie.sma.common.ApiResponse;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.SchedulePageResponseDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.service.ScheduleService;
import xyz.ncookie.sma.valid.DateString;

@Validated  // @RequestParam, @PathVariable 등에도 valid 적용하기 위해서 사용
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<ScheduleResponseDto> createSchedule(@RequestBody @Valid ScheduleRequestDto dto) {
        return ApiResponse.success(HttpStatus.CREATED, scheduleService.saveSchedule(dto));
    }

    @GetMapping
    public ApiResponse<SchedulePageResponseDto> findAllSchedules(
            @PageableDefault(size = 10, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) @DateString String modified_date,    // 날짜 형식 yyyy-MM-dd
            @RequestParam(required = false) Long userId
    ) {
        return ApiResponse.ok(scheduleService.findAllSchedules(pageable, modified_date, userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<ScheduleResponseDto> findSchedule(@PathVariable Long id) {
        return ApiResponse.ok(scheduleService.findSchedule(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleUpdateRequestDto dto
    ) {
        return ApiResponse.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleDeleteRequestDto dto
    ) {
        scheduleService.deleteSchedule(id, dto);
        return ApiResponse.ok(null);
    }

}
