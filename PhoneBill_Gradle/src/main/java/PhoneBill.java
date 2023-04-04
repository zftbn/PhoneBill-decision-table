import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class PhoneBill {
    private final BigDecimal RATE1 = new BigDecimal("0.05"); // 20分钟以下的费率
    private final BigDecimal RATE2 = new BigDecimal("1.00"); // 20分钟及以上的基础费率
    private final BigDecimal RATE3 = new BigDecimal("0.10"); // 超过20分钟的额外费率
    private static final int MINUTES_PER_HOUR = 60;
    private static final int MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR;
    private static final int MINUTES_PER_WEEK = 7 * MINUTES_PER_DAY;

    /**
     * 计算通话时长
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 通话时长（分钟）
     */
    public long calculateTimeSpan(LocalDateTime startTime, LocalDateTime endTime) {
        // 计算时间差并向上取整
        Duration duration = Duration.between(startTime, endTime);
        long timeLength = (duration.getSeconds() + 59) / 60;

        // 处理夏令时
        if (startTime.getYear() == endTime.getYear()) { // 同一年才可能有夏令时
            if (startTime.getMonthValue() == 3 && startTime.getDayOfWeek().getValue() == 7) {
                // 春季夏令时转换前
                if (startTime.getHour() <= 2 && endTime.getHour() >= 3) {
                    timeLength -= 60; // 减去一个小时
                }
            } else if (startTime.getMonthValue() == 11 && startTime.getDayOfWeek().getValue() == 7) {
                // 秋季夏令时转换后
                if (startTime.getHour() <= 3 && endTime.getHour() >= 2) {
                    timeLength += 60; // 加上一个小时
                }
            }
        }

        return timeLength;
    }

    /**
     * 计算通话费用
     *
     * @param timeLength 通话时长（分钟）
     * @return 通话费用（美元）
     */
    public BigDecimal calculateFee(long timeLength) {
        if (timeLength <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal fee;
        if (timeLength <= 20) {
            fee = RATE1.multiply(new BigDecimal(timeLength));
        } else {
            fee = RATE2.add(RATE3.multiply(new BigDecimal(timeLength - 20)));
        }

        //使用soptbugs扫描后显示多余
//        // 不到1分钟按1分钟计算
//        if (fee.compareTo(BigDecimal.ZERO) > 0 && timeLength < 1) {
//            timeLength = 1;
//        }

        return fee;
    }
}
