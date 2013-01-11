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
 * File:                org.anon.smart.base.test.loader.TagObjectStereoType
 * Author:              rsankar
 * Revision:            1.0
 * Date:                31-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A tagging stereotype
 *
 * ************************************************************
 * */

package org.anon.smart.base.test.loader;

import org.anon.smart.base.stt.annot.MethodExit;

public class TagObjectStereoType
{
    private String __tag__name__;

    public TagObjectStereoType()
    {
    }

    @MethodExit("constructor")
    private void initializeTag()
    {
        __tag__name__ = this.getClass().getName();
        System.out.println("TagName: " + __tag__name__);
    }

    public String[] getTagName()
    {
        return new String[] { __tag__name__ };
    }
}

