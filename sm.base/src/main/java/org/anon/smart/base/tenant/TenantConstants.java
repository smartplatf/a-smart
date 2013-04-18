/**
 * SMART - State Machine ARchiTecture
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of SMART.
 *
 * SMART is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SMART is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * */
 
/**
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.base.tenant.TenantConstants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants used in tenant
 *
 * ************************************************************
 * */

package org.anon.smart.base.tenant;

import java.util.Map;
import java.util.HashMap;

public interface TenantConstants
{
    public static final String PLATFORMOWNER = "SmartOwner";
    public static final String TENANTGROUP = "SmartTenant";
    public static final String TENANTSPACENAME = "TenantsHosted";

    public static final String USERS_SPACE = "Users";
    public static final String SESSIONS_SPACE = "Sessions";
    public static final String ROLES_SPACE = "Roles";
    public static final String CONFIG_SPACE = "Config";
    public static final String WORKING_SPACE = "Working";
    public static final String MONITOR_SPACE = "Monitor";

    public static final String STANDARD_GROUP = "Standard";
    public static final String WORKING_GROUP = "Working";
    public static final String FLOW_GROUP = "Flow";
    public static final Map<String, String> GROUP_MAPPING = new HashMap<String, String>();
}

