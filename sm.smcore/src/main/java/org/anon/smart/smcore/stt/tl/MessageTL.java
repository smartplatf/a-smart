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
 * File:                org.anon.smart.smcore.stt.tl.MessageTL
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                May 3, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.stt.tl;

import java.util.ArrayList;
import java.util.List;

import org.anon.smart.base.annot.MessageAnnotate;
import org.anon.smart.base.stt.tl.BaseTL;

import org.anon.utilities.exception.CtxException;

public class MessageTL extends BaseTL {

	@Override
    public Class[] getAnnotations(String name)
        throws CtxException
    {
        List<Class> annons = new ArrayList<Class>();
        Class[] annots = super.getAnnotations(name);
        for (int i = 0; (annots != null) && (i < annots.length); i++)
            annons.add(annots[i]);

        annons.add(MessageAnnotate.class);
        return annons.toArray(new Class[0]);
    }
	
	public static MessageTL defaultFor(String clsname, String type, String flow, String[] parms)
    {
        MessageTL ret = new MessageTL();
        BaseTL.populateDefault(ret, clsname, type, flow);
        return ret;
    }

    @Override
    public boolean shouldAdd(String type)
    {
        return true;
    }

}
