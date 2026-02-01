package priv.dawn.gulu.tool.impls;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.tool.GuluReferableExpression;
import priv.dawn.gulu.utils.ReflectUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/23:13
 */
public class GuluReflectionContext implements GuluContext {

    private final Object object;
    private final Map<String, GuluReferableExpression> referMap = new HashMap<>();
    private final Map<String, Boolean> resCache = new HashMap<>();
    private final Set<String> runningReferExp = new HashSet<>();

    public GuluReflectionContext(Object object) {
        this.object = object;
    }

    @Override
    public Object getIdentifier(String path) {
        return ReflectUtils.getFieldByPath(object, path);
    }

    @Override
    public Object getEnvVar(String path) {
        return null;
    }

    @Override
    public GuluAstNode getReferAstNode(String path) {
        return Optional.of(referMap.get(path)).map(GuluReferableExpression::getAstRootNode).orElse(null);
    }

    @Override
    public Boolean getReferExpressionResult(String path) {
        if (!referMap.containsKey(path)) {
            throw new ExpressionEvaluateException("refer expression is not exists");
        }
        if (resCache.containsKey(path)) {
            return resCache.get(path);
        }
        GuluReferableExpression exp = referMap.get(path);
        if (runningReferExp.contains(exp.getReferPath())) {
            throw new ExpressionEvaluateException("refer expression is running, there might be circular dependency:" + runningReferExp);
        }
        runningReferExp.add(exp.getReferPath());
        boolean evaluate = exp.evaluate(this);
        resCache.put(exp.getReferPath(), evaluate);
        return evaluate;
    }

    @Override
    public void registerReferExpression(GuluReferableExpression expression) {
        if (referMap.containsKey(expression.getReferPath())) {
            throw new IllegalArgumentException("refer path already exists");
        }
        referMap.put(expression.getReferPath(), expression);
    }
}
