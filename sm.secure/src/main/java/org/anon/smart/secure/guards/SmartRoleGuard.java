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
 * File:                org.anon.smart.secure.guards.SmartRoleGuard
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A guard that guards by roles
 *
 * ************************************************************
 * */

package org.anon.smart.secure.guards;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.secure.access.Access;
import org.anon.smart.secure.access.Accessed;
import org.anon.smart.secure.access.Visitor;
import org.anon.smart.secure.access.ALOrganizer;
import org.anon.smart.secure.annot.GuardAnnotate;
import org.anon.smart.secure.inbuilt.data.SmartUser;
import org.anon.smart.secure.inbuilt.data.SmartRole;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.flow.CrossLinkFlowDeployment;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.utils.Repeatable;
import org.anon.utilities.utils.RepeaterVariants;
import org.anon.utilities.exception.CtxException;

public class SmartRoleGuard extends BaseSGuard
{
    private String[] _deployedURIs;

    public SmartRoleGuard(GuardAnnotate annot, Class cls)
        throws CtxException
    {
        super(annot, cls);
        //initURIs(cls);
    }

    public SmartRoleGuard(Class cls)
        throws CtxException
    {
        super(SGuardType.smartrole, cls);
        //initURIs(cls);
    }

    SmartRoleGuard()
    {
        super();
    }

    private void initURIs(Class cls)
        throws CtxException
    {
        String flow = flowFor(cls);
        CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
        CrossLinkDeploymentShell dshell = tenant.deploymentShell();
        String name = className(cls);
        //have to see how to handle multiple deployments
        List<CrossLinkFlowDeployment> fdeps = dshell.flowForType(name);
        assertion().assertTrue(fdeps.size() > 0, "Cannot Find deployment for: " + name);
        CrossLinkFlowDeployment fdep = fdeps.get(0);
        _deployedURIs = fdep.deployedURI(cls.getName());
    }

    @Override
    protected Access permitted(Accessed accessed, Visitor visitor, String parms)
        throws CtxException
    {
        if (_deployedURIs == null)
            initURIs(_guardForClass);

        if (visitor.associatedSession() == null)
            return Access.none;

        Session sess = visitor.associatedSession();
        SmartUser usr = sess.getUser();
        if (accessed.getAccessed() instanceof SmartUser)
        {
            usr = (SmartUser)accessed.getAccessed();
            if (sess.getUserId().equals(usr.getID()))
            {
                sess.setUser(usr);
                return Access.execute; //a logged in session with the user, hence has access
            }
        }
        else if (accessed.getAccessed() instanceof SmartRole)
        {
            SmartRole r = (SmartRole)accessed.getAccessed();
            if (usr.getRoles().contains(r.getName()))
                return Access.execute;
        }
        else if (usr == null)
        {
            usr = SmartUser.getUser(sess.getUserId());
        }

        System.out.println("URI is: " + _deployedURIs.length);
        List<SmartRole> roles = usr.lookupRoles();
        List<Access> ret = new ArrayList<Access>();
        for (SmartRole r : roles) //don't break, get the access as per all the roles
        {
            for (int i = 0; i < _deployedURIs.length; i++) //get the access as per all the URIs
            {
                Access a = r.allowedAccess(_deployedURIs[i]);
                System.out.println("Checked URI: " + _deployedURIs[i] + ":" + a);
                if ((a != null) && (a != Access.none))
                    ret.add(a);
            }
        }

        if (ret.size() <= 0)
            ret.add(Access.none);

        return ALOrganizer.highest(ret);
    }

    public Repeatable repeatMe(RepeaterVariants vars)
        throws CtxException
    {
        SGuardParms parm = (SGuardParms)vars;
        return new SmartRoleGuard(parm.getAnnotate(), parm.getKlass());
    }
}

