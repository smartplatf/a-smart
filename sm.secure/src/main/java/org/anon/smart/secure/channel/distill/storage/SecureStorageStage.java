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
 * File:                org.anon.smart.secure.channel.distill.storage.SecureStorageStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-09-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A storage stage that removes session from the threadlocal
 *
 * ************************************************************
 * */

package org.anon.smart.secure.channel.distill.storage;

import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.smcore.channel.distill.storage.StorageStage;
import org.anon.smart.secure.session.SessionDirector;

import org.anon.utilities.exception.CtxException;

public class SecureStorageStage extends StorageStage
{
    public SecureStorageStage()
    {
        super();
    }

    protected void cleanup(CrossLinkSmartTenant tenant)
        throws CtxException
    {
        SessionDirector.removeSessionFrom(tenant);
    }
}

