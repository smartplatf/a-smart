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
 * File:                org.anon.smart.smcore.channel.distill.translation.CustomTranslationStage
 * Author:              rsankar
 * Revision:            1.0
 * Date:                29-12-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A custom translation of data
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.channel.distill.translation;

import java.util.Map;
import java.io.InputStream;

import org.anon.smart.channels.data.PData;
import org.anon.smart.channels.distill.Isotope;
import org.anon.smart.channels.distill.Distillate;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class CustomTranslationStage extends TranslationStage
{
    private CrossLinkAny _translator;

    public CustomTranslationStage(String translator)
        throws CtxException
    {
        super();
        _translator = new CrossLinkAny(translator, this.getClass().getClassLoader());
    }

    @Override
    public Distillate distill(Distillate prev)
        throws CtxException
    {
        PData data = (PData)prev.current();
        InputStream str = data.cdata().data();
        try
        {
        Object convert = _translator.invoke("readStream", new Class[] { InputStream.class }, new Object[] { str });
        System.out.println("Custom Converted: " + str + ":" + convert);
        Isotope translated = null;
        if (convert instanceof Map)
            translated = new MapData(data, (Map<String, Object>)convert);
        else
            except().te("Conversion has to return a Map object.", new CtxException.Context("CustomTranslation", "Returned: " + convert));

        return new Distillate(prev, translated);
        }
        catch (CtxException e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}

