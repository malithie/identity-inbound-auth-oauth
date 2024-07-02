/*
 * Copyright (c) 2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.rar.core;

import org.wso2.carbon.identity.oauth2.rar.internal.AuthorizationDetailsDataHolder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A factory class to manage and provide instances of {@link AuthorizationDetailsProvider} Service Provider Interface.
 * This class follows the Singleton pattern to ensure only one instance is created.
 * It uses {@link ServiceLoader} to dynamically load and manage {@link AuthorizationDetailsProvider} implementations.
 * <p> Example usage:
 * <pre> {@code
 * // Get a specific provider by type
 * AuthorizationDetailsProviderFactory.getInstance()
 *     .getProviderByType("customer_information")
 *     .ifPresentOrElse(
 *         p -> log.debug("Provider for type " + type + ": " + p.getClass().getName()),
 *         () -> log.debug("No provider found for type " + type)
 *     );
 * } </pre> </p>
 *
 * @see AuthorizationDetailsProvider AuthorizationDetailsService
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc9396#name-request-parameter-authoriza">
 * Request Parameter "authorization_details"</a>
 * @since 7.0.26.9
 */
public class AuthorizationDetailsProviderFactory {

    private static volatile AuthorizationDetailsProviderFactory instance;
    private final Map<String, AuthorizationDetailsProvider> supportedAuthorizationDetailsTypes;

    /**
     * Private constructor to initialize the factory.
     * <p> This constructor is intentionally private to prevent direct instantiation of the
     * {@code AuthorizationDetailsProviderFactory} class.
     * Instead, use the {@link #getInstance()} method to obtain the singleton instance. </p>
     */
    private AuthorizationDetailsProviderFactory() {

        this.supportedAuthorizationDetailsTypes = this.loadSupportedAuthorizationDetailsTypes();
    }

    /**
     * Loads supported authorization details types from the provided {@link ServiceLoader}.
     *
     * @return Map of authorization details types with their corresponding services.
     */
    private Map<String, AuthorizationDetailsProvider> loadSupportedAuthorizationDetailsTypes() {

        return AuthorizationDetailsDataHolder.getInstance()
                .getAuthorizationDetailsProviders()
                .stream()
                .collect(Collectors.toMap(AuthorizationDetailsProvider::getType, Function.identity()));
    }

    /**
     * Provides the singleton instance of {@code AuthorizationDetailsProviderFactory}.
     *
     * @return Singleton instance of {@code AuthorizationDetailsProviderFactory}.
     */
    public static AuthorizationDetailsProviderFactory getInstance() {

        if (instance == null) {
            synchronized (AuthorizationDetailsProviderFactory.class) {
                if (instance == null) {
                    instance = new AuthorizationDetailsProviderFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the {@link AuthorizationDetailsProvider} provider for the given type.
     *
     * @param type A supported authorization details type.
     * @return {@link Optional} containing the {@link AuthorizationDetailsProvider} if present, otherwise empty.
     * @see AuthorizationDetailsProvider#getType() getAuthorizationDetailsType
     */
    public Optional<AuthorizationDetailsProvider> getProviderByType(final String type) {

        return Optional.ofNullable(this.supportedAuthorizationDetailsTypes.get(type));
    }

    /**
     * Checks if a given type has a valid service provider implementation.
     *
     * @param type The type to check.
     * @return {@code true} if the type is supported, {@code false} otherwise.
     * @see AuthorizationDetailsProvider AuthorizationDetailsService
     */
    public boolean isSupportedAuthorizationDetailsType(final String type) {

        return this.supportedAuthorizationDetailsTypes.containsKey(type);
    }

    /**
     * Returns a {@link Collections#unmodifiableSet} of all supported authorization details types.
     * <p> To be included as a supported authorization details type, there must be a custom implementation
     * of the {@link AuthorizationDetailsProvider} Service Provider Interface (SPI) available in the classpath
     * for the specified type. </p>
     *
     * @return A set of supported authorization details types.
     */
    public Set<String> getSupportedAuthorizationDetailTypes() {

        return Collections.unmodifiableSet(this.supportedAuthorizationDetailsTypes.keySet());
    }
}
