package com.example.camping.data.vo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "FavoriteItem")
data class Item(
    @PrimaryKey
    val contentId: Int, // 야영장 아이디

    val addr1: String?,  // 주소
    val addr2: String?,  // 주소상세

    val doNm: String?, // 도 - 경상남도
    val sigunguNm: String?, // 시군구

    val mapX: Double?,   // 경도
    val mapY: Double?,   // 위도

    val intro: String?, // 인트로 소개

    val facltNm: String?,    // 야영장명
    val firstImageUrl: String?, // 대표이미지

    val sbrsCl: String?, // 부대시설 - 전기,무선인터넷,장작판매,온수,놀이터,운동시설
    val sbrsEtc: String?, // 부대시설 기타 - 어린이 놀이기구 소량 에어바운스
    val swrmCo: Int?,    // 샤워실 개수
    val toiletCo: Int?,  // 화장실 개수
    val wtrplCo: Int?,   // 개수대 개수
    val eqpmnLendCl: String?, // 캠핑장비대여 - 텐트,화로대,난방기구,식기,침낭

    val trlerAcmpnyAt: String?,  // 개인 트레일러 동반여부 - Y/N
    val animalCmgCl: String?, // 애완동물 출입 - 가능/불가능
    val brazierCl: String?, // 화로대 - 개별/불가

    val tel: String?,    // 전번
    val homepage: String?, // 홍페이지
    val resveUrl: String?,
    val induty: String?, // 업종
    val prmisnDe: String?, // 등록 날짜
    val lctCl: String?, // 입지구분 - 산,숲,계곡,도심
    val lineIntro: String?, // 한줄소개 - 사계절 서로 다른 매력의 캠핑을 즐길 수 있는 곳
    val posblFcltyCl: String?, // 주변이용가능시설 - 계곡 물놀이,산책로,강/물놀이

    var distance: String?    // 거리
) : Parcelable