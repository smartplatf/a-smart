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
 * File:                org.anon.smart.secure.loader.SmartSecureLoader
 * Author:              rsankar
 * Revision:            1.0
 * Date:                07-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A loader that protects classes using the KlassProtector
 *
 * ************************************************************
 * */

package org.anon.smart.secure.loader;

import java.net.URL;
import java.security.CodeSource;
import java.security.CodeSigner;
import java.security.Permissions;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.security.AllPermission;

import org.anon.smart.base.loader.SmartLoader;
import org.anon.smart.base.loader.LoaderVars;
import org.anon.smart.base.flow.FlowConstants;
import org.anon.smart.secure.stt.Constants;
import org.anon.smart.secure.sdomain.CrossLinkKlassProtector;
import org.anon.smart.secure.sdomain.KlassProtector;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class SmartSecureLoader extends SmartLoader
{
    static
    {
        addForceLoadSuper("org.anon.smart.secure.stt.*");
        addForceLoadSuper("org.anon.smart.secure.loader.*");
    }

    private static final String[] _preventSecure =
                {
                    "org/anon/utilities/",
                    "org/anon/smart/secure/",
                    "org/anon/smart/base/",
                    "org/anon/smart/d2cache/",
                    "org/anon/smart/kernel/",
                    "org/anon/smart/channels/",
                    "org/anon/smart/atomicity/",
                    "org/anon/smart/deployment/",
                    "org/anon/smart/smcore/",
                    "org/anon/smart/secure/test/TestSecureConfig",
                    "org/anon/smart/secure/test/SecureServerRunner",
                    "org/jboss/netty",
                    "org/apache/jcs",
                    "org/apache/hadoop",
                    "java/"
                };

    private static final String[] _specificInclude = 
        {
            "org/anon/smart/base/tenant/TenantAdmin",
            "org/anon/smart/base/flow/FlowAdmin",
            "org/anon/smart/smcore/inbuilt/",
            "org/anon/smart/secure/inbuilt/",
            "org/anon/smart/secure/sdomain/SmartAccessController",
            "org/anon/smart/secure/sdomain/SmartPAction"
        };

    /*
    private static final String[] _secureTypes = 
                {
                    FlowConstants.PRIMETYPE,
                    FlowConstants.DATATYPE,
                    FlowConstants.EVENTTYPE,
                    FlowConstants.RESPONSETYPE,
                    FlowConstants.CONFIGTYPE,
                    FlowConstants.TRANSITIONTYPE,
                    FlowConstants.MESSAGETYPE
                };*/
 
    public SmartSecureLoader(URL[] urls, String name, String[] comps)
        throws CtxException
    {
        super(urls, name, comps);
    }

    public SmartSecureLoader(URL[] urls, String[] comps)
        throws CtxException
    {
        this(urls, "unreferenced:SmartSecureLoader", comps);
    }

    public SmartLoader repeatMe(LoaderVars lvars)
        throws CtxException
    {
        SmartSecureLoader ldr = new SmartSecureLoader(this.getURLs(), lvars.getName(), _initComps);
        return ldr;
    }

    private boolean passthrough(URL url)
    {
        if (url != null)
        {
            String p = url.getPath();
            for (int i = 0; i < _preventSecure.length; i++)
            {
                if (p.contains(_preventSecure[i]) && (!specificInclude(p)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean specificInclude(String p)
    {
        for (int i = 0; i < _specificInclude.length; i++)
        {
            if (p.contains(_specificInclude[i]))
                return true;
        }

        return false;
    }

    protected ProtectionDomain domainFor(URL url)
        throws CtxException
    {
        if (url == null)
            return null;

        if (passthrough(url))
        {
            //return a default all permission protection domain
            Permissions perms = new Permissions();
            perms.add(new AllPermission());
            ProtectionDomain domain = new ProtectionDomain(new CodeSource(url, new CodeSigner[0]), perms);
            return domain;
        }

        try
        {
            String nm = url.getPath();
            Permissions perms = new Permissions();
            CrossLinkKlassProtector protect = new CrossLinkKlassProtector(this, _name + ":" + nm, new CodeSource(url, new CodeSigner[0]), perms);
            return (ProtectionDomain)protect.link();
        }
        catch (Exception e)
        {
            except().rt(e, new CtxException.Context("DomainProtection", "Problem in creating."));
        }

        return null;
    }

    protected void initializeDomain(ProtectionDomain domain, Class cls)
        throws CtxException
    {
        if ((domain != null) && (domain.getClass() != null) && (domain.getClass().getName().equals(KlassProtector.class.getName())))
        {
            CrossLinkKlassProtector protect = new CrossLinkKlassProtector(domain);
            if (protect.isAssignable())
                protect.protectKlass(cls);
        }
    }

    /* instead of this, we do from the basetl. We add an extra basetl
    @Override
    public String[] addExtraSTT(String type)
    {
        String[] fromsuper = super.addExtraSTT(type);
        String[] ret = new String[fromsuper.length + 1];
        for (int i = 0; i < fromsuper.length; i++)
            ret[i] = fromsuper[i];

        ret[fromsuper.length] = { Constants.SECUREDATA };
        for (int i = 0; i < _secureTypes.length; i++)
        {
            if (type.equals(_secureTypes[i]))
                return ret;
        }

        return fromsuper;
    }
    */
}

