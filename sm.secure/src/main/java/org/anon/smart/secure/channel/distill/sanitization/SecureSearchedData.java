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
 * File:                org.anon.smart.secure.channel.distill.sanitization.SecureSearchedData
 * Author:              rsankar
 * Revision:            1.0
 * Date:                08-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A data whose accesses has been verified
 *
 * ************************************************************
 * */

package org.anon.smart.secure.channel.distill.sanitization;

import org.anon.smart.base.tenant.SmartTenant;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.smcore.channel.distill.ChannelConstants;
import org.anon.smart.smcore.channel.distill.sanitization.SearchedData;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class SecureSearchedData extends SearchedData implements ChannelConstants
{
    private Object _session;

    public SecureSearchedData(Isotope data)
    {
        super(data);
    }

    public void setSession(Object session)
    {
        _session = session;
    }

    public Object getSession()
    {
        return _session;
    }

    @Override
    public void setupSearchMap(SearchedData.PrimeFlow flow)
        throws CtxException
    {
        super.setupSearchMap(flow);
        if (_session != null)
            searchedMap().put(SESSION_FLD, _session);
    }

    public void setupSearchContext(String evtName)
        throws CtxException
    {
        SmartTenant tenant = tenant();
        ClassLoader ldr = tenant.getRelatedLoader();
        CrossLinkAny any = new CrossLinkAny(SecureSanitizeContext.class.getName(), ldr);
        any.create(evtName);
    }
}

