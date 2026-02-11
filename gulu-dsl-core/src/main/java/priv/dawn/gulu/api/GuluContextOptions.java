package priv.dawn.gulu.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/11/20:45
 */
@Data
@Builder
public class GuluContextOptions {

    private List<String> supportDateFormats;

}
