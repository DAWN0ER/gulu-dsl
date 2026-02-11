package priv.dawn.gulu.utils;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.api.GuluContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Dawn Yang
 * @since 2026/01/27/0:41
 */
public class GuluAstNodeComparator implements Comparator<GuluAstNode> {

    private final GuluContext ctx;
    private final Collection<SimpleDateFormat> supportFormats;

    public GuluAstNodeComparator(GuluContext ctx) {
        this.ctx = ctx;
        if (ctx.getOptions() != null && ctx.getOptions().getSupportDateFormats() != null && !ctx.getOptions().getSupportDateFormats().isEmpty()) {
            supportFormats = ctx.getOptions().getSupportDateFormats().stream().map(SimpleDateFormat::new).collect(Collectors.toList());
        } else {
            supportFormats = new ArrayList<>();
            supportFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Override
    public int compare(GuluAstNode o1, GuluAstNode o2) {
        // 完善工具类，比较相关的只用这个作比较
        Object val1 = GuluNodeValueUtils.value(o1, ctx);
        Object val2 = GuluNodeValueUtils.value(o2, ctx);
        if (val1 instanceof String && val2 instanceof String) {
            return ((String) val1).compareTo((String) val2);
        }
        if (val1 instanceof Number && val2 instanceof Number) {
            return Double.compare(((Number) val1).doubleValue(), ((Number) val2).doubleValue());
        }
        Long epochMills1 = GuluNodeValueUtils.tryConvertEpochMills(val1, supportFormats);
        Long epochMills2 = GuluNodeValueUtils.tryConvertEpochMills(val2, supportFormats);
        if (epochMills1 != null && epochMills2 != null) {
            return epochMills1.compareTo(epochMills2);
        }
        throw new ExpressionEvaluateException(String.format("Unsupported compare %s (of node %s) and %s (of node %s)",
                val1, o1.getClass(), val2, o2.getClass()));
    }

}
