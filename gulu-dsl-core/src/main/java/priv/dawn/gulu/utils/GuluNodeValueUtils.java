package priv.dawn.gulu.utils;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.node.*;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.api.GuluContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/0:44
 */
public class GuluNodeValueUtils {

    public static Object value(GuluAstNode node, GuluContext ctx){
        if (node instanceof GuluIdentifierNode) {
            return ctx.getIdentifier(((GuluIdentifierNode) node).getPath());
        }
        if (node instanceof GuluStringNode) {
            return ((GuluStringNode) node).getValue();
        }
        if (node instanceof GuluBooleanNode){
            return ((GuluBooleanNode) node).getValue();
        }
        if (node instanceof GuluNumberNode) {
            return ((GuluNumberNode) node).getValue();
        }
        if (node instanceof GuluEnvVarNode) {
            return ctx.getEnvVar(((GuluEnvVarNode) node).getEnvVarPath());
        }
        throw new ExpressionEvaluateException("Unsupported AstNode in BinaryCompareNode");
    }

    public static Long tryConvertEpochMills(Object val, List<String> dateFormats) {
        Collection<SimpleDateFormat> supportFormats;
        if (dateFormats != null && !dateFormats.isEmpty()) {
            supportFormats = dateFormats.stream().map(SimpleDateFormat::new).collect(Collectors.toList());
        } else {
            supportFormats = new ArrayList<>();
            supportFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        return tryConvertEpochMills(val, supportFormats);
    }

    public static Long tryConvertEpochMills(Object val, Collection<SimpleDateFormat> supportFormats) {
        if (val instanceof Date) {
            return ((Date) val).getTime();
        }
        if (val instanceof LocalDate) {
            return ((LocalDate) val).toEpochDay();
        }
        if (val instanceof LocalDateTime) {
            return ((LocalDateTime) val).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        }
        if (val instanceof Integer || val instanceof Long) {
            return ((Number) val).longValue();
        }
        if (val instanceof String) {
            for (SimpleDateFormat format : supportFormats) {
                try {
                    return format.parse((String) val).getTime();
                } catch (ParseException e) {
                    // do nothing
                }
            }
        }
        return null;
    }

}
