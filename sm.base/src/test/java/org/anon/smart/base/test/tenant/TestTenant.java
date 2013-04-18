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
 *
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                org.anon.smart.base.test.tenant.TestTenant
 * Author:              rsankar
 * Revision:            1.0
 * Date:                23-03-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * a tenant creation test
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.tenant;

import org.junit.Test;
import static org.junit.Assert.*;

import org.anon.smart.base.test.ServerUtilities;

public class TestTenant
{
    @Test
    public void testTestTenant()
        throws Exception
    {
        ServerUtilities sutils = new ServerUtilities(9080);
        sutils.runServer("org.anon.smart.base.test.tenant.TestTenantRunner");
        sutils.stopServer();
    }
}

