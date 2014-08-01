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
 * File:                org.anon.smart.secure.inbuilt.transition.ManageUsers
 * Author:              rsankar
 * Revision:            1.0
 * Date:                01-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of transitions to create and manage users
 *
 * ************************************************************
 * */

package org.anon.smart.secure.inbuilt.transition;

import java.util.List;
import java.util.ArrayList;

import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.secure.inbuilt.data.Session;
import org.anon.smart.secure.inbuilt.data.SmartUser;
import org.anon.smart.secure.inbuilt.data.SmartRole;
import org.anon.smart.secure.inbuilt.events.CreateUser;
import org.anon.smart.secure.inbuilt.events.AddIdentity;
import org.anon.smart.secure.inbuilt.events.AddRolesToUser;
import org.anon.smart.secure.inbuilt.events.ChangePassword;
import org.anon.smart.secure.inbuilt.data.iden.Identity;
import org.anon.smart.secure.inbuilt.data.iden.SCredential;
import org.anon.smart.secure.inbuilt.data.iden.IdentityType;
import org.anon.smart.secure.inbuilt.responses.SecurityResponse;
import org.anon.smart.secure.inbuilt.data.iden.Password;
import org.anon.smart.secure.sdomain.SmartSecureData;

import static org.anon.smart.base.utils.AnnotationUtils.*;
import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.crosslink.CrossLinkAny;
import org.anon.utilities.exception.CtxException;

public class ManageUsers
{
    private static final String DEFAULT_USER_ID = "admin";
    private static final String DEFAULT_USER_NAME = "Admin";
    private static final String DEFAULT_IDENTITY = "admin";
    private static final String SMART_OWNER = "smart";

    public ManageUsers()
    {
    }

    public void changePassword(SmartUser user, ChangePassword pwd)
        throws CtxException
    {
        Object obj = pwd;
        SmartSecureData data = (SmartSecureData)obj;
        Session sess = data.smart___session();
        assertion().assertNotNull(sess, "Cannot change password without logging into the user first.");
        String usr = sess.getUserId();
        assertion().assertTrue((usr.equals(user.getID())), "Cannot change password for a different user. " + sess.getUserId() + ":" + user.getID());
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(Identity.class);
        String flow = flowFor(Identity.class);
        Object iden = rshell.lookupFor(flow, group, pwd.getIdentity());

        assertion().assertNotNull(iden, "Cannot find identity for which password is changed.");
        Identity identity = (Identity)iden;
        assertion().assertTrue((identity.getCredential() instanceof Password), "Cannot change password for identities other than the ones in SMART.");
        Password currpwd = (Password)identity.getCredential();
        Password confirm = new Password(pwd.getOldCredential());
        assertion().assertTrue(currpwd.equals(confirm), "The old password provided is not correct.");
        currpwd.change(pwd.getCredential());
        SecurityResponse resp = new SecurityResponse("Changed the password for: " + pwd.getIdentity());
    }

    public void setupResetPassword(Identity identity, String randomCode)
        throws CtxException
    {
        assertion().assertTrue((identity.getCredential() instanceof Password), "Cannot change password for identities other than the ones in SMART.");
        identity.setRandomCode(randomCode);
    }

    public void resetPassword(Identity identity, String randomCode, String credential)
        throws CtxException
    {
        System.out.println("Random Code is: " + identity.getRandomCode() + ":" + randomCode);
        assertion().assertTrue((identity.getCredential() instanceof Password), "Cannot change password for identities other than the ones in SMART.");
        assertion().assertTrue(((identity.getRandomCode() != null) && (identity.getRandomCode().equals(randomCode))), "Random Code does not match for reset.");
        assertion().assertFalse(((identity.getRandomCode() != null) && identity.getRandomCode().equals("Nothing")), "Random Code does not match for reset.");
        identity.resetCode();
        Password currpwd = (Password)identity.getCredential();
        currpwd.change(credential);
        SecurityResponse resp = new SecurityResponse("Changed the password for: " + identity.getIdentity() + ":" + credential);
    }

    public void createUserService(String userid, String name, List<String> roles)
        throws CtxException
    {
        CreateUser cuser = new CreateUser(userid, name, roles);
        createNewUser(cuser);
    }

    public void createNewUser(CreateUser user)
        throws CtxException
    {
        createNewUserInternal(user, false);
    }

    private SmartUser createNewUserInternal(CreateUser user, boolean adddefault)
        throws CtxException
    {
        SecurityResponse resp = null;
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(SmartUser.class);
        String flow = flowFor(SmartUser.class);
        System.out.println("Searching for: " + flow + ":" + group + ":" + user.getID());
        Object suser = rshell.lookupFor(flow, group, user.getID());
        if (suser != null)
        {
            resp = new SecurityResponse("A user already exists for: " + user.getID());
            return null;
        }

        System.out.println("Creating user: " + user.getID());
        SmartUser usr = new SmartUser(user.getID(), user.getName());
        List<String> roles = user.getRoles();
        addRoles(usr, roles, rshell, adddefault);
        resp = new SecurityResponse("Created a user for: " + user.getID() + ":" + user.getName());
        return usr;
    }

    public Object clCreateDefaultUser(ClassLoader tenantldr, String tenant)
        throws CtxException
    {
        CrossLinkAny any = new CrossLinkAny(this.getClass().getName(), tenantldr);
        any.create();
        Object usr = any.invoke("createDefaultUser", tenant);
        return usr;
    }

    public Object clAddIdentity(ClassLoader tenantldr, Object usr, String tenant)
        throws CtxException
    {
        CrossLinkAny any = new CrossLinkAny(this.getClass().getName(), tenantldr);
        any.create();
        Object ident = any.invoke("clAddDefaultIdent", new Class[] { Object.class, String.class }, new Object[] { usr, tenant });
        return ident;
    }

    public SmartUser createDefaultUser()
        throws CtxException
    {
        return createDefaultUser(SMART_OWNER);
    }

    public SmartUser createDefaultUser(String tenant)
        throws CtxException
    {
        CreateUser user = new CreateUser(tenant + DEFAULT_USER_ID, tenant + DEFAULT_USER_NAME);
        SmartUser usr = createNewUserInternal(user, true);
        return usr;
    }

    public Object clAddDefaultIdent(Object usr, String tenant)
        throws CtxException
    {
        return addDefaultIdentity((SmartUser)usr, tenant);
    }

    public Identity addDefaultIdentity(SmartUser usr)
        throws CtxException
    {
        return addDefaultIdentity(usr, SMART_OWNER);
    }

    public Identity addDefaultIdentity(SmartUser usr, String tenant)
        throws CtxException
    {
        AddIdentity ident = new AddIdentity(tenant + DEFAULT_IDENTITY, tenant + DEFAULT_IDENTITY, "custom");
        return addIdentity(usr, ident);
    }

    private void addRoles(SmartUser usr, List<String> roles, RuntimeShell rshell, boolean adddefault)
        throws CtxException
    {
        if (roles != null)
        {
            String flow = flowFor(SmartRole.class);
            String group = className(SmartRole.class);
            for (String role : roles)
            {
                Object robject = rshell.lookupFor(flow, group, role);
                assertion().assertNotNull(robject, "Cannot find role for: " + role);
                usr.addRole(role);
            }
        }
        else
        {
            //add default role
            SmartRole robject = ManageRoles.getDefaultRole();
            if (robject != null)
                usr.addRole(robject.getName());
            else if (adddefault)
                usr.addRole(ManageRoles.getDefaultRoleName());
        }
    }

    public void addRolesToUser(SmartUser usr, AddRolesToUser evt)
        throws CtxException
    {
        SecurityResponse resp = null;
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        List<String> roles = evt.getRoles();
        addRoles(usr, roles, rshell, false);
        resp = new SecurityResponse("Added roles: " + roles + " to " + usr.getID());
    }

    public void addRolesToUserService(String userid, List<String> roles)
        throws CtxException
    {
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(SmartUser.class);
        String flow = flowFor(SmartUser.class);
        SmartUser suser = (SmartUser)rshell.lookupFor(flow, group, userid);
        assertion().assertNotNull(suser, "Cannot find user for: " + userid);
        AddRolesToUser add = new AddRolesToUser(roles);
        addRolesToUser(suser, add);
    }

    public Identity addIdentity(SmartUser user, AddIdentity identity)
        throws CtxException
    {
        SecurityResponse resp = null;
        RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
        String group = className(Identity.class);
        String flow = flowFor(Identity.class);
        Object iden = rshell.lookupFor(flow, group, identity.getIdentity());
        if (iden != null)
        {
            //I know this is any identity. Should we limit by type, I wonder!! Same identity id for two users
            //is not possible, but is it possible that the same identity is used with different types?
            resp = new SecurityResponse("An identity with: " + identity.getIdentity() + " already exists. Cannot create.");
            return null;
        }

        SCredential cred = IdentityType.getCredential(identity.getType(), identity.getCredentialKey());
        if (cred != null)
        {
            Identity niden = new Identity(user.getID(), identity.getIdentity(), cred);
            resp = new SecurityResponse("Added identity: " + identity.getIdentity());
            return niden;
        }
        else
        {
            resp = new SecurityResponse("Could not create a credential for type: " + identity.getType());
        }
        return null;
    }

    public Identity addIdentityService(SmartUser usr, String userid, String identity, String cred, String type)
        throws CtxException
    {
        SmartUser suser = usr;
        if ((suser == null) && (userid != null))
        {
            RuntimeShell rshell = RuntimeShell.currentRuntimeShell();
            String group = className(SmartUser.class);
            String flow = flowFor(SmartUser.class);
            suser = (SmartUser)rshell.lookupFor(flow, group, userid);
        }
        assertion().assertNotNull(suser, "Cannot find user for: " + userid);
        AddIdentity ident = new AddIdentity(identity, cred, type);
        return addIdentity(suser, ident);
    }
}

