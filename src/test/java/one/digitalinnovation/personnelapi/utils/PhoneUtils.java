package one.digitalinnovation.personnelapi.utils;

import one.digitalinnovation.personnelapi.dto.PhoneDTO;
import one.digitalinnovation.personnelapi.entity.Phone;
import one.digitalinnovation.personnelapi.enums.PhoneType;

public class PhoneUtils {

    private static final String PHONE_NUMBER = "1199999-9999";
    private static final PhoneType PHONE_TYPE = PhoneType. HOME;
    private static final long PHONE_ID = 1L;

    public static PhoneDTO createPhoneDTO() {
        return PhoneDTO.builder()
                .number(PHONE_NUMBER)
                .type(PHONE_TYPE)
                .build();
    }

    public static Phone createPhoneEntity() {
        return Phone.builder()
                .id(PHONE_ID)
                .number(PHONE_NUMBER)
                .type(PHONE_TYPE)
                .build();
    }
}
