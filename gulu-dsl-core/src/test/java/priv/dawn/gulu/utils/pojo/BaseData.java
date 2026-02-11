package priv.dawn.gulu.utils.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Dawn Yang
 * @since 2026/01/28/21:28
 */
@Data
public class BaseData {

    private String aStr;
    private Long aLong;
    private Integer aInt;
    private Float aFloat;
    private Double aDouble;
    private Boolean aBool;
    private Date aDate;
    private LocalDate aLocalDate;
    private LocalDateTime aLocalDateTime;
    private BaseData aSubObj;

    private List<String> aStrList;
    private List<Long> aLongList;
    private List<Integer> aIntList;
    private List<Float> aFloatList;
    private List<Double> aDoubleList;
    private List<Boolean> aBoolList;
    private List<Date> aDateList;
    private List<LocalDate> aLocalDateList;
    private List<LocalDateTime> aLocalDateTimeList;
    private List<BaseData> aSubObjList;
}
