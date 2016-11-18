package de.hddesign.androidutils.androidutils.network.utils;

/**
 * Fluent Builder interface for creating immutable objects.
 *
 * @param <T> The type handled by the builder.
 * @author Rui Vilao (rui@gymondo.de)
 */
public interface FluentBuilder<T> {
    /**
     * Builds the final object.
     *
     * @return The final object.
     */
    T build();
}
