package org.jepria.test.server.environment;

/**
 * TODO: rename with more specifics.
 */
public interface Environment<T> extends
    EnvironmentPlain, EnvironmentWithDao<T>, EnvironmentWithAuth {
}
