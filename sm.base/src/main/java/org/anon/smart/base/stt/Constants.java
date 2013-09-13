/**
 * Utilities - Utilities used by anon
 *
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of Utilities.
 *
 * Utilities is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Utilities is distributed in the hope that it will be useful,
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
 * File:                org.anon.smart.base.stt.Constants
 * Author:              rsankar
 * Revision:            1.0
 * Date:                24-12-2012
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of constants used in stt package
 *
 * ************************************************************
 * */

package org.anon.smart.base.stt;

import org.anon.smart.base.annot.Synthetic;
import org.anon.smart.base.stt.annot.CustomVisit;
import org.anon.smart.base.stt.annot.IncludeSTT;
import org.anon.smart.base.stt.annot.MethodEnter;
import org.anon.smart.base.stt.annot.MethodExit;

public interface Constants
{
    public static final String CLASS_PARAMS = "(Ljava/lang/String;)V";
    public static final String CLASS_MTHD_PARAMS = "(Ljava/lang/String;Ljava/lang/String;)V";
    public static final String NO_PARAMS = "()V";
    public static final String ALL_VALUE = "all";
    public static final String CONSTRUCTOR_NAME = "<init>";
    public static final String CONSTRUCTOR_VAL = "constructor";

    public static final Class SYNTHETIC_CLASS = Synthetic.class;
    public static final Class CUSTOMVISIT_CLASS = CustomVisit.class;
    public static final Class INCLUDESTT_CLASS = IncludeSTT.class;
    public static final Class METHODENTER_CLASS = MethodEnter.class;
    public static final Class METHODEXIT_CLASS = MethodExit.class;

    public static final String TYPE_CONFIG = "type";
    public static final String ATTRIBUTE_CONFIG = "attributes";
    public static final String ACTION_CONFIG = "actions";
    public static final String SERVICE_CONFIG = "services";
}

