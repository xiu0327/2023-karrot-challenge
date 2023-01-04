package numble.karrot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {
    DIGITAL("디지털기기"),
    HOUSEHOLD_APPLIANCES("생활가전"),
    FURNITURE_INTERIOR("가구/인테리어"),
    CHILD("유아동"),
    LIVING_FOOD("생활/가공식품"),
    CHILD_BOOKS("유아도서"),
    SPORT_LEISURE("스포츠/레저"),
    WOMEN_ACCESSORIES("여성잡화"),
    MEN_FASHION_ACCESSORIES("남성패션/잡화"),
    GAME_HOBBIES("게임/취미"),
    BEAUTY("뷰티/미용"),
    PET_SUPPLIES("반려동물용품"),
    BOOKS_TICKETS_RECORDS("도서/티켓/음반"),
    PLANT("식물"),
    OTHER_USED("기타 중고물품"),
    USED_CAR("중고차");

    private String value;
}
