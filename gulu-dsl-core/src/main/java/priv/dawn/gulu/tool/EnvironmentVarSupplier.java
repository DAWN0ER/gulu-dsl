package priv.dawn.gulu.tool;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/03/19:54
 */
public interface EnvironmentVarSupplier {

    Set<String> supportRootPath();

    Object getVarByFullPath(String[] paths);

}
