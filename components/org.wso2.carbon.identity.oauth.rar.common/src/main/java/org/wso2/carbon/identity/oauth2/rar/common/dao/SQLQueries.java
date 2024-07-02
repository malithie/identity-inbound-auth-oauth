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

package org.wso2.carbon.identity.oauth2.rar.common.dao;

/**
 * The {@code SQLQueries} class contains SQL query constants used for performing
 * database operations related to OAuth2 Rich Authorization Requests.
 */
public class SQLQueries {

    private SQLQueries() {
        // Private constructor to prevent instantiation
    }

    public static final String ADD_OAUTH2_CODE_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS " +
                    "(CODE_ID, TYPE_ID, AUTHORIZATION_DETAILS, TENANT_ID) VALUES " +
                    "(?, (SELECT ID FROM IDN_OAUTH2_AUTHORIZATION_DETAILS_TYPES WHERE TYPE=? AND TENANT_ID=?), " +
                    "? FORMAT JSON, ?)";

    public static final String GET_OAUTH2_CODE_AUTHORIZATION_DETAILS =
            "SELECT IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.AUTHORIZATION_DETAILS " +
                    "FROM IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS " +
                    "INNER JOIN IDN_OAUTH2_AUTHORIZATION_CODE " +
                    "ON IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.CODE_ID = IDN_OAUTH2_AUTHORIZATION_CODE.CODE_ID " +
                    "WHERE IDN_OAUTH2_AUTHORIZATION_CODE.AUTHORIZATION_CODE=? " +
                    "AND IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.TENANT_ID=?";

    public static final String ADD_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS " +
                    "(CONSENT_ID, TYPE_ID, AUTHORIZATION_DETAILS, CONSENT, TENANT_ID) VALUES " +
                    "(?,(SELECT ID FROM IDN_OAUTH2_AUTHORIZATION_DETAILS_TYPES WHERE TYPE=? AND TENANT_ID=?), " +
                    "? FORMAT JSON, ?, ?)";

    public static final String UPDATE_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS =
            "UPDATE IDN_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS " +
                    "SET AUTHORIZATION_DETAILS=? CONSENT=? WHERE CONSENT_ID=? AND TENANT_ID=?";

    public static final String GET_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS =
            "SELECT ID, TYPE_ID, AUTHORIZATION_DETAILS, CONSENT FROM IDN_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS " +
                    "WHERE CONSENT_ID=? AND TENANT_ID=?";

    public static final String DELETE_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS =
            "DELETE FROM IDN_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS WHERE CONSENT_ID=? AND TENANT_ID=?";

    public static final String CREATE_OAUTH2_ACCESS_TOKEN_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_ACCESS_TOKEN_AUTHORIZATION_DETAILS " +
                    "(AUTHORIZATION_DETAILS_TYPE, AUTHORIZATION_DETAILS, TOKEN_ID, TENANT_ID) VALUES (?, ?, ?, ?)";

    public static final String GET_IDN_OAUTH2_USER_CONSENT_CONSENT_ID =
            "SELECT CONSENT_ID FROM IDN_OAUTH2_USER_CONSENT WHERE USER_ID=? AND APP_ID=? AND TENANT_ID=?";
}
