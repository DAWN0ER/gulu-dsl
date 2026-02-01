package priv.dawn.gulu.ast;

import priv.dawn.gulu.ast.node.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/23/23:04
 */
public interface GuluNodeVisitor<T> {

    default T visitBoolLogicNode(GuluBoolLogicNode node){
        return null;
    }
    default T visitBoolNotNode(GuluBoolNotNode node){
        return null;
    }
    default T visitBinaryCompareNode(GuluBinaryCompareNode node){
        return null;
    }
    default T visitIdentifierNode(GuluIdentifierNode node){
        return null;
    }
    default T visitExistedNode(GuluExistedNode node){
        return null;
    }
    default T visitInFuncNode(GuluInFuncNode node){
        return null;
    }
    default T visitContainsNode(GuluContainsNode node){
        return null;
    }
    default T visitNestedNode(GuluNestedNode node){
        return null;
    }
    default T visitNumberNode(GuluNumberNode node){
        return null;
    }
    default T visitStringNode(GuluStringNode node){
        return null;
    }
    default T visitBooleanNode(GuluBooleanNode node){
        return null;
    }
    default T visitEnvVarNode(GuluEnvVarNode node){
        return null;
    }
    default T visitReferNode(GuluReferNode node){
        return null;
    }

}
