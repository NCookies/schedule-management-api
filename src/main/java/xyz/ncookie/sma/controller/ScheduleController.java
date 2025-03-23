package xyz.ncookie.sma.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.ncookie.sma.common.ApiResponse;
import xyz.ncookie.sma.dto.request.ScheduleDeleteRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.request.ScheduleUpdateRequestDto;
import xyz.ncookie.sma.dto.response.SchedulePageResponseDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        return ApiResponse.success(HttpStatus.CREATED, scheduleService.saveSchedule(dto));
    }

    @GetMapping
    public ApiResponse<SchedulePageResponseDto> findAllSchedules(
            @PageableDefault(size = 10, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String modified_date,
            @RequestParam(required = false, defaultValue = "-1") Long userId
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
            @RequestBody ScheduleUpdateRequestDto dto
    ) {
        return ApiResponse.ok(scheduleService.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleDeleteRequestDto dto
    ) {
        scheduleService.deleteSchedule(id, dto);
        return ApiResponse.ok(null);
    }

}
