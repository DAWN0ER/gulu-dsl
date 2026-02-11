package priv.dawn.gulu.api.impls.ctx;

import priv.dawn.gulu.api.EnvironmentVarSupplier;
import priv.dawn.gulu.api.GuluContext;
import priv.dawn.gulu.api.GuluContextOptions;
import priv.dawn.gulu.api.GuluReferableExpression;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.exception.GuluContextBuildExpression;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/11/20:08
 */
public abstract class BaseGuluContext implements GuluContext {

    // refer
    protected Map<String, GuluReferableExpression> referMap = new HashMap<>();
    protected Map<String, Boolean> resCache = new HashMap<>();
    protected Set<String> runningReferExp = new HashSet<>();
    // envVar
    protected Map<String, EnvironmentVarSupplier> environmentVarSupplierMap;
    // options
    protected GuluContextOptions options;

    @Override
    public Object getEnvVar(String path) {
        String[] paths = path.split("\\.");
        Object envVar = Optional.ofNullable(environmentVarSupplierMap.get(paths[0]))
                .map(supplier -> supplier.getVarByFullPath(paths))
                .orElse(null);
        if(envVar == null){
            throw new ExpressionEvaluateException("Environment variable [" + path + "] is not exists");
        }
        return envVar;
    }

    @Override
    public GuluAstNode getReferAstNode(String path) {
        GuluAstNode astNode = Optional.ofNullable(referMap.get(path))
                .map(GuluReferableExpression::getAstRootNode)
                .orElse(null);
        if(astNode == null){
            throw new ExpressionEvaluateException("refer expression is not exists");
        }
        return astNode;
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
    public GuluContextOptions getOptions() {
        return options;
    }

    public void registerReferExpression(GuluReferableExpression expression) {
        if (referMap.containsKey(expression.getReferPath())) {
            throw new IllegalArgumentException("refer path already exists");
        }
        referMap.put(expression.getReferPath(), expression);
    }

    public void registerEnvVarSupplier(EnvironmentVarSupplier supplier) {
        Set<String> roots = supplier.supportRootPath();
        for (String root : roots) {
            if (environmentVarSupplierMap.containsKey(root)) {
                throw new GuluContextBuildExpression("Duplicate supported root path:" + root);
            }
        }
        for (String root : roots) {
            environmentVarSupplierMap.put(root, supplier);
        }
    }



}
