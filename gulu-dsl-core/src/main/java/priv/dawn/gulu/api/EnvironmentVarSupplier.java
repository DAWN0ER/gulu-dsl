package priv.dawn.gulu.api;

import java.util.Set;

/**
 * @author Dawn Yang
 * @since 2026/02/03/19:54
 */
public interface EnvironmentVarSupplier {

    Set<String> supportRootPath();

    Object getVarByFullPath(String[] paths);

}
