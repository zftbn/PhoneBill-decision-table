import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneBillTest {

    @Test
    void testCalculateFee() {
        PhoneBill phoneBill = new PhoneBill();

//        // C1:通话时长 < 0, 动作:不可能情况, 规则:M1
//        assertThrows(IllegalArgumentException.class, () -> phoneBill.calculateFee(-1));
//
//        // C1:通话时长 > 1800, 动作:不可能情况, 规则:M5
//        assertThrows(IllegalArgumentException.class, () -> phoneBill.calculateFee(1801));

        // C1:0 <= 通话时长 < 1, C2:春夏时刻转换, 动作:money=0.05, 规则:M2,T2
        LocalDateTime startTime1 = LocalDateTime.of(2023, Month.MARCH, 26, 1, 59, 30);
        LocalDateTime endTime1 = LocalDateTime.of(2023, Month.MARCH, 26, 3, 0, 0);
        assertEquals(new BigDecimal("0.05"), phoneBill.calculateFee(phoneBill.calculateTimeSpan(startTime1, endTime1)));


        // C1:1 <= 通话时长 < 20, C2:夏秋时刻转换, 动作:money=0.05t, 规则:M3,T3
        LocalDateTime startTime2 = LocalDateTime.of(2023, Month.NOVEMBER, 5, 2, 59, 0);
        LocalDateTime endTime2 = LocalDateTime.of(2023, Month.NOVEMBER, 5, 2, 8, 0);
        assertEquals(new BigDecimal("0.50"), phoneBill.calculateFee(phoneBill.calculateTimeSpan(startTime2, endTime2)));

        // C1:20 <= 通话时长 <= 1800, C2:春夏时刻转换, 动作:money=0.1t-1, 规则:M4,T2
        LocalDateTime startTime3 = LocalDateTime.of(2023, Month.MARCH, 26, 1, 59, 0);
        LocalDateTime endTime3 = LocalDateTime.of(2023, Month.MARCH, 26, 3, 29, 0);
        assertEquals(new BigDecimal("2.00"), phoneBill.calculateFee(phoneBill.calculateTimeSpan(startTime3, endTime3)));

    }
}