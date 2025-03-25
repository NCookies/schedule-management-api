package xyz.ncookie.sma.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

// Page 객체로 구성된 일정 전체 조회 응답 DTO
public record SchedulePageResponseDto(
        List<ScheduleResponseDto> contents,     // 조회 데이터

        long totalElements,             // 총 데이터 개수
        long totalPages,                // 총 페이지 수

        int pageNumber,                 // 현재 페이지
        int numberOfElements,           // 현재 페이지의 데이터 개수
        int sizeOfPage,                 // 한 페이지 당 최대 데이터 개수

        String sortProperty             // 정렬 기준 필드와 방향 ex) "createdAt: DESC, task: ASC"
) {

    public static SchedulePageResponseDto from(Page<ScheduleResponseDto> dto) {
        return new SchedulePageResponseDto(
                dto.getContent(),

                dto.getTotalElements(),
                dto.getTotalPages(),

                dto.getNumber(),
                dto.getContent().size(),
                dto.getSize(),

                dto.getSort().toString()
        );
    }

}
