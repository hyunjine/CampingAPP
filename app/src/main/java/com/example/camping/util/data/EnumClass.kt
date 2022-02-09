package com.example.camping.util.data

/**
 *  1. AREA        - 큰 지역별 / 파라미터 값: ex) 강원도
 *  2. TYPE        - 야영지 타입별 / 파라미터 값: ex) 캠핑 - 추가 safeargs : 지역 or 숙소명
 *  3. PET         - 반려동물 여부 별 / 파라미터 값: ex) 강원도
 *  4. AROUND      - 내 위치 주변 / 파리미터 값: 위도, 경도
 *  5. LOAD_IMAGES - 해당 야영지 이미지 / 파라미터 값: contentID
 */
enum class ParameterType {
    AREA,
    TYPE,
    PET,
    INIT_PET,
    FAL_NAME,
    KEY_WORD,
    AROUND,
    IMAGE_LIST,
    RANDOM_LIST
}

enum class RecyclerViewType {
    LIST,
    AROUND,
    FAVORITE,
    PET
}