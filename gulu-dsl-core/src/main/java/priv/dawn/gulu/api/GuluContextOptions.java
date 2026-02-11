package priv.dawn.gulu.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Dawn Yang
 * @since 2026/02/11/20:45
 */
@Data
@Builder
public class GuluContextOptions {

    /**
     * 支持的日期格式列表，用于日期或时间类型的比较
     */
    private List<String> supportDateFormats;

}
