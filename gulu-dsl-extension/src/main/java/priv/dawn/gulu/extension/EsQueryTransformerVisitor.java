package priv.dawn.gulu.extension;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.ast.node.*;
import priv.dawn.gulu.extension.exceptions.EsQueryTransformException;
import priv.dawn.gulu.token.GuluToken;
import priv.dawn.gulu.api.GuluContext;
import priv.dawn.gulu.utils.GuluNodeValueUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/01/22:29
 */
public class EsQueryTransformerVisitor implements GuluNodeVisitor<QueryBuilder> {

    private final GuluContext context;
    private String currentNestedPrefix = "";

    public EsQueryTransformerVisitor(GuluContext context) {
        this.context = context;
    }

    @Override
    public QueryBuilder visitBoolLogicNode(GuluBoolLogicNode node) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (node.getOp() == GuluToken.Type.AND) {
            node.getConditions().forEach(n -> boolQuery.must(n.accept(this)));
        } else if (node.getOp() == GuluToken.Type.OR) {
            node.getConditions().forEach(n -> boolQuery.should(n.accept(this)));
        } else {
            throw new EsQueryTransformException("BoolLogicNode's op is not AND or OR");
        }
        return boolQuery;
    }

    @Override
    public QueryBuilder visitBoolNotNode(GuluBoolNotNode node) {
        QueryBuilder query = node.getWrappedNode().accept(this);
        return QueryBuilders.boolQuery().mustNot(query);
    }

    @Override
    public QueryBuilder visitBinaryCompareNode(GuluBinaryCompareNode node) {
        GuluAstNode left = node.getLeft();
        GuluAstNode right = node.getRight();
        if (left instanceof GuluIdentifierNode) {
            String pathName = ((GuluIdentifierNode) left).getPath();
            Object value = GuluNodeValueUtils.value(right, context);
            return assembleTermQuery(wrapPath(pathName), node.getCompareOp(), value);
        }
        if (right instanceof GuluIdentifierNode) {
            String pathName = ((GuluIdentifierNode) right).getPath();
            Object value = GuluNodeValueUtils.value(left, context);
            return assembleTermQuery(wrapPath(pathName), reverseOp(node.getCompareOp()), value);
        }
        throw new EsQueryTransformException("BinaryCompareNode's left or right is not IdentifierNode");
    }

    @Override
    public QueryBuilder visitExistedNode(GuluExistedNode node) {
        String path = node.getIdentifierNode().getPath();
        return QueryBuilders.existsQuery(wrapPath(path));
    }

    @Override
    public QueryBuilder visitInFuncNode(GuluInFuncNode node) {
        String path = node.getIdentifier().getPath();
        Collection<Object> list = node.getLiteralList().stream()
                .map(n -> GuluNodeValueUtils.value(n, context))
                .collect(Collectors.toList());
        return QueryBuilders.termsQuery(wrapPath(path), list);
    }

    @Override
    public QueryBuilder visitContainsNode(GuluContainsNode node) {
        String path = node.getIdentifier().getPath();
        Collection<Object> list = node.getContainsList().stream()
                .map(n -> GuluNodeValueUtils.value(n, context))
                .collect(Collectors.toList());
        return QueryBuilders.termsQuery(wrapPath(path), list);
    }

    @Override
    public QueryBuilder visitNestedNode(GuluNestedNode node) {
        String path = node.getPathIdentifier().getPath();
        // nested 嵌套逻辑
        String prefixStorage = currentNestedPrefix;
        currentNestedPrefix = wrapPath(path);
        QueryBuilder nestedQuery = node.getNestedExpression().accept(this);
        currentNestedPrefix = prefixStorage;
        return QueryBuilders.nestedQuery(wrapPath(path), nestedQuery, ScoreMode.None);
    }

    @Override
    public QueryBuilder visitReferNode(GuluReferNode node) {
        if (!currentNestedPrefix.isEmpty()) {
            throw new EsQueryTransformException("Nested query can not refer other Expression");
        }
        return context.getReferAstNode(node.getReferPath()).accept(this);
    }

    private QueryBuilder assembleTermQuery(String pathName, GuluToken.Type compareOp, Object value) {
        switch (compareOp) {
            case EQ:
                return QueryBuilders.termQuery(pathName, value);
            case NE:
                return QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(pathName, value));
            case GT:
                return QueryBuilders.rangeQuery(pathName).from(value, false);
            case GTE:
                return QueryBuilders.rangeQuery(pathName).from(value, true);
            case LT:
                return QueryBuilders.rangeQuery(pathName).to(value, false);
            case LTE:
                return QueryBuilders.rangeQuery(pathName).to(value, true);
        }
        throw new EsQueryTransformException("Unsupported compare operator:" + compareOp);
    }

    private GuluToken.Type reverseOp(GuluToken.Type op) {
        switch (op) {
            case GT:
                return GuluToken.Type.LT;
            case LT:
                return GuluToken.Type.GT;
            case GTE:
                return GuluToken.Type.LTE;
            case LTE:
                return GuluToken.Type.GTE;
            default:
                return op;
        }
    }

    private String wrapPath(String path) {
        return currentNestedPrefix.isEmpty()? path : currentNestedPrefix + "." + path;
    }

}
