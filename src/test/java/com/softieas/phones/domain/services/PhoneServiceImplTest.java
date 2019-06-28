package com.softieas.phones.domain.services;

import com.softieas.phones.domain.models.PhoneSheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PhoneServiceImpl.class})
class PhoneServiceImplTest {

    private final PhoneService phoneService;

    private final String dataSheet = "103343262,6478342944, 103426540,84528784843, 103278808,263716791426, 103426733,27736529279, 103426000,27718159078, 103218982,19855201547, 103427376,27717278645, 103243034,81667273413";

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

    @Test
    void importData() {
        var result = phoneService.importData(this.sheetStream);

    }

}