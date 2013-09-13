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
 * File:                org.anon.smart.secure.flow.CrossLinkSecureFlowDeployment
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-08-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A cross link for secure flow deployment
 *
 * ************************************************************
 * */

package org.anon.smart.secure.flow;

import java.util.List;
import java.util.ArrayList;

import org.anon.utilities.crosslink.CrossLinker;
import org.anon.utilities.exception.CtxException;

public class CrossLinkSecureFlowDeployment extends CrossLinker
{
    public CrossLinkSecureFlowDeployment(Object obj)
    {
        super(obj);
    }

    public List<CrossLinkSecureConfig> getSecurity()
        throws CtxException
    {
        List cfg = (List)linkMethod("getSecurity");
        List<CrossLinkSecureConfig> ret = new ArrayList<CrossLinkSecureConfig>();
        for (int i = 0; (cfg != null) && (i < cfg.size()); i++)
            ret.add(new CrossLinkSecureConfig(cfg.get(i)));
        return ret;
    }
}

