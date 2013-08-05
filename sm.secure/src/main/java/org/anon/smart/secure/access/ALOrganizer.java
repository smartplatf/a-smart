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
 * File:                org.anon.smart.secure.access.ALOrganizer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                06-06-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An organizer for access levels
 *
 * ************************************************************
 * */

package org.anon.smart.secure.access;

import java.util.List;
import java.util.ArrayList;

public class ALOrganizer
{
    //this determines which access implies which access. The index of the access determines the level of the access 
    private static final Access[] _orderedAL = new Access[] { Access.none, Access.read, Access.write, Access.execute };
    private static final AccessScope[] _orderedScope = new AccessScope[] { AccessScope.klass, AccessScope.instance, AccessScope.attribute }; 

    private ALOrganizer()
    {
    }

    public static boolean implies(Access a1, Access a2)
    {
        int a1lvl = -1;
        int a2lvl = -1;

        for (int i = 0; (((a1lvl < 0) || (a2lvl < 0)) && (i < _orderedAL.length)); i++)
        {
            if (_orderedAL[i].equals(a1))
                a1lvl = i;
            if (_orderedAL[i].equals(a2))
                a2lvl = i;
        }

        //if any of the access levels is not present
        //in the list, then it does not imply anything
        if ((a1lvl < 0) || (a2lvl < 0))
            return false;

        return (a2lvl <= a1lvl);
    }

    public static Access[] impliedAccess(Access a)
    {
        List<Access> ret = new ArrayList<Access>();
        int alvl = -1;

        for (int i = 0; (alvl < 0) && (i < _orderedAL.length); i++)
        {
            ret.add(_orderedAL[i]);
            if (_orderedAL[i].equals(a))
                alvl = i;
        }

        if (alvl < 0) 
        {
            //if not present in list, implies nothing
            ret.clear();
            ret.add(a);
        }

        return ret.toArray(new Access[0]);
    }

    public static AccessScope[] orderedScope()
    {
        return _orderedScope;
    }

    public static Access highest()
    {
        return _orderedAL[_orderedAL.length - 1];
    }

    public static Access highest(List<Access> access)
    {
        Access ret = null;

        for (int i = 0; (i < _orderedAL.length); i++)
        {
            if (access.contains(_orderedAL[i]))
                ret = _orderedAL[i];
        }

        if (ret == null) //means none of the orderable access is present. so no idea, pick up the first
            ret = access.get(0);

        return ret;
    }

    public static Access highestOf(Access a1, Access a2)
    {
        int a1lvl = -1;
        int a2lvl = -1;

        for (int i = 0; (((a1lvl < 0) || (a2lvl < 0)) && (i < _orderedAL.length)); i++)
        {
            if (_orderedAL[i].equals(a1))
                a1lvl = i;
            if (_orderedAL[i].equals(a2))
                a2lvl = i;
        }

        //if any of the access levels is not present
        //in the list, then it does not imply anything
        if ((a1lvl < 0) || (a2lvl < 0))
            return a1; //just return one

        return (a1lvl < a2lvl) ? a2 : a1;
    }

    public static Access lowest(AccessLimits[] limits)
    {
        List<Access> a = new ArrayList<Access>();
        for (int i = 0; i < limits.length; i++)
            a.add(limits[i].getAccess());

        for (int i = 0; i < _orderedAL.length; i++)
        {
            if (a.contains(_orderedAL[i]))
                return _orderedAL[i];
        }

        return a.get(0);
    }
}

