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
 * File:                org.anon.smart.smcore.deployment.DeploymentService
 * Author:              rsankar
 * Revision:            1.0
 * Date:                14-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A service that registers and recognizes artefacts and provides other services
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.deployment;

import java.lang.annotation.Annotation;

import org.anon.smart.deployment.ArtefactType;
import org.anon.smart.base.annot.SmartDataAnnotate;
import org.anon.smart.base.annot.EventAnnotate;
import org.anon.smart.base.annot.TransitionAnnotate;
import org.anon.smart.base.annot.ResponseAnnotate;
import org.anon.smart.base.annot.MessageAnnotate;

public class DeploymentService
{
    static
    {
        try
        {
            ArtefactType.registerArtefactType(dataRecognizer(), "name", "name");
            ArtefactType.registerArtefactType(eventRecognizer(), "name", "name");
            ArtefactType.registerArtefactType(transitionRecognizer(), "name", "data", "event");
            ArtefactType.registerArtefactType(responseRecognizer(), "name", "name");
            ArtefactType.registerArtefactType(messageRecognizer(), "name", "name");
        }
        catch (Exception e)
        {
            //TODO: logger
            e.printStackTrace();
        }
    }

    private DeploymentService()
    {
    }

    public static Class<? extends Annotation> dataRecognizer()
    {
        return SmartDataAnnotate.class;
    }

    public static Class<? extends Annotation> eventRecognizer()
    {
        return EventAnnotate.class;
    }

    public static Class<? extends Annotation> transitionRecognizer()
    {
        return TransitionAnnotate.class;
    }

    public static Class<? extends Annotation> responseRecognizer()
    {
        return ResponseAnnotate.class;
    }

    public static Class<? extends Annotation> messageRecognizer()
    {
        return MessageAnnotate.class;
    }

    public static boolean isSmartData(ArtefactType type)
    {
        return type.isType(SmartDataAnnotate.class);
    }

    public static boolean isEvent(ArtefactType type)
    {
        return type.isType(EventAnnotate.class);
    }

    public static boolean isTransition(ArtefactType type)
    {
        return type.isType(TransitionAnnotate.class);
    }
}

