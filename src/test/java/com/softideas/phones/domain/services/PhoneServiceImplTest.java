package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.PhoneNumberStatus;
import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.models.RejectionReason;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(basePackages = {"com.softideas.phones.domain.repositories.*"})
@SpringBootTest(classes = {PhoneServiceImpl.class, NormalizedNumber.class})
class PhoneServiceImplTest {

    private final PhoneService phoneService;

    private final String dataSheet = "103343262,_D0736529279, 103426540,84528784843, 103278808,263716791426, 103426733,27736529279, 103426000,27718159078, 103218982,19855201547, 103427376,27717278645, 103243034,81667273413";

    private final Stream<PhoneSheet> sheetStream;

    public PhoneServiceImplTest(@Autowired PhoneService phoneService) {
        this.phoneService = phoneService;
        final int chunk = 2;
        AtomicInteger counter = new AtomicInteger();
        Map<String, String> pairs = Arrays.stream(dataSheet.split(","))
                .map(String::trim)
                .collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunk))
                .values()
                .stream()
                .collect(toMap(k -> k.get(0), v -> v.get(1)));

        this.sheetStream = pairs.entrySet().stream().map(PhoneSheet::fromEntry);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0736529279:true",
            "6478342944:false",
            "27826088289:true",
            "263716791426:false",
            "27718159078:true",
            "19855201547:false",
            "27717278645:true",
            "263774817994:false",
            "27724360860:true",
            "639565885094:false",
            "27827678672:true",
            "26771835182:false",
            "27713564440:true"}, delimiter = ':')
    void test_validateNumber(String number, boolean expected) {
        Assertions.assertEquals(phoneService.isValidCellNumber(number), expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "270736529279:true",
            "27736529000_o:true",
            "_D0736529279:true",
            "736529279:true",
            "_D0436529279:false",
            "0436529279:false",
            "6478342944:false"}, delimiter = ':')
    void test_FixPhoneNumber(String number, boolean expected) {
        Assertions.assertEquals(phoneService.tryToFixNumber(new PhoneSheet("0", number))
                .getPhoneNumberStatus() == PhoneNumberStatus.FIXED_NUMBER, expected);
    }

    @ParameterizedTest
    @MethodSource("provide_test_FixPhoneNumber_statusCase")
    void test_FixPhoneNumber_statusCase(String number, PhoneNumberStatus phoneNumberStatus, RejectionReason rejectionReason) {
        var actual = phoneService.tryToFixNumber(new PhoneSheet("0", number));
        Assertions.assertEquals(actual.getPhoneNumberStatus(), phoneNumberStatus);
        Assertions.assertEquals(actual.getRejectionReason(), rejectionReason);
    }

    private static Stream<Arguments> provide_test_FixPhoneNumber_statusCase() {
        return Stream.of(
                Arguments.of("27713564440", PhoneNumberStatus.VALID_NUMBER, RejectionReason.NOT_APPLICABLE),
                Arguments.of("27736529000_o", PhoneNumberStatus.FIXED_NUMBER, RejectionReason.NOT_APPLICABLE),
                Arguments.of("270736529000", PhoneNumberStatus.FIXED_NUMBER, RejectionReason.NOT_APPLICABLE),
                Arguments.of("_D0436529279", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.ILLEGAL_CHARS),
                Arguments.of("736529279", PhoneNumberStatus.FIXED_NUMBER, RejectionReason.NOT_APPLICABLE),
                Arguments.of("27713111564440", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.TOO_MANY_DIGITS),
                Arguments.of("2773652", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.MISSING_DIGITS),
                Arguments.of("639565885094", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.MALFORMED),
                Arguments.of("27713564440_DELL", PhoneNumberStatus.FIXED_NUMBER, RejectionReason.NOT_APPLICABLE),
                Arguments.of("6395EOEOE94", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.ILLEGAL_CHARS),
                Arguments.of("27913564440_DELL", PhoneNumberStatus.INVALID_NUMBER, RejectionReason.ILLEGAL_CHARS)
        );
    }

    @Test
    void test_importData_happyCase() {
        var result = phoneService.importData(this.sheetStream);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getValidNumbers(), this.sheetStream.count());
    }


}