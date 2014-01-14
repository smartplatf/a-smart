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
 * File:                org.anon.smart.base.flow.Link
 * Author:              rsankar
 * Revision:            1.0
 * Date:                16-05-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A link description for objects
 *
 * ************************************************************
 * */

package org.anon.smart.base.flow;

import org.anon.utilities.config.Constants;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public class Link implements Constants
{
    public class LinkObject
    {
        private String _flow;
        private String _object;
        private String _field;
        private String _cardinality;

        LinkObject(String unparsed, String curr)
            throws CtxException
        {
            String[] parsed = unparsed.split(OBJECT_SEPARATOR);
            int cnt = 0;
            if (parsed.length == 2)
            {
                _flow = curr;
            }
            else
            {
                _flow = parsed[cnt];
                cnt++;
            }
            _object = parsed[cnt];
            cnt++;
            _field = parsed[cnt];
            cnt++;
            if (_field.indexOf("(") > 0)
            {
                assertion().assertTrue(_field.endsWith(")"), "Format of link is not correct. Please close the paranthesis.");
                String[] split = _field.split("\\(");

                _field = split[0];
                _cardinality = split[1].substring(0, (split[1].length() - 1));
            }
            else
                _cardinality = "1";
        }

        LinkObject(LinkObject obj)
        {
            _flow = obj._flow;
            _object = obj._object;
            _field = obj._field;
            _cardinality = obj._cardinality;
        }

        public String getFlow() { return _flow; }
        public String getObject() { return _object; }
        public String getField() { return _field; }
        public String getCardinality() { return _cardinality; }
        public boolean linkFor(String flow, String obj) { return (_flow.equals(flow) && _object.equals(obj)); }

        public String toString()
        {
            return _flow + ":" + _object + ":"  + _field;
        }
    }

    private String name;
    private String from;
    private String to;
    private String via;
    private String viaflow;
    private boolean optional;

    private LinkObject _fromObject;
    private LinkObject _toObject;
    private LinkObject _via;
    private LinkObject _viaFlow;
    private boolean _internal;

    public Link()
    {
        _internal = false;
    }

    public Link(Link lnk)
    {
        _internal = lnk._internal;
        name = lnk.name;
        from = lnk.from;
        to = lnk.to;
        via = lnk.via;
        viaflow = lnk.viaflow;
        optional = lnk.optional;
        System.out.println("When copying link: " + optional + ":" + lnk.optional + ":" + name);

        if (lnk._fromObject != null)
            _fromObject = new LinkObject(lnk._fromObject);

        if (lnk._toObject != null)
            _toObject = new LinkObject(lnk._toObject);

        if (lnk._via != null)
            _via = new LinkObject(lnk._via);

        if (lnk._viaFlow != null)
            _viaFlow = new LinkObject(lnk._viaFlow);
    }

    public String getVia() { return via; }
    public boolean isInternal() { return _internal; }
    public String getName() { return name; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getViaFlow() { return viaflow; }

    void setup(String currFlow)
        throws CtxException
    {
        _internal = false;
        System.out.println("Optional for: " + name + " during setup is; " + optional + ":" + viaflow);
        if ((from != null) && (from.length() > 0))
            _fromObject = new LinkObject(from, currFlow);

        if ((to != null) && (to.length() > 0))
            _toObject = new LinkObject(to, currFlow);

        if ((via != null) && (via.length() > 0))
            _via = new LinkObject(via, currFlow);

        if ((viaflow != null) && (viaflow.length() > 0))
            _viaFlow = new LinkObject(viaflow, currFlow);

        //assertion().assertTrue(_fromObject.getFlow().equals(currFlow), "All links have to be from objects in the current flow to other flows or within current flow.");
    }

    public LinkObject getFromObject() { return _fromObject; }
    public LinkObject getToObject() { return _toObject; }
    public LinkObject getViaObject() { return _via; }
    public LinkObject getViaFlowObject() { return _viaFlow; }

    public boolean linkFor(String flow, String obj)
    {
        if (_toObject == null)
            return false;

        boolean ret = _fromObject.linkFor(flow, obj);
        ret = ret || _toObject.linkFor(flow, obj);
        return ret;
    }

    public String toString()
    {
        return "From: " + _fromObject + " To: " + _toObject + ":" + _viaFlow;
    }

    public String groupFor()
    {
        if ((_fromObject != null) && (_toObject != null))
            return _fromObject.getObject() + "|" + _toObject.getObject();

        return "";
    }

    public void setTo(String asto, String currFlow, boolean internal)
        throws CtxException
    {
        to = asto;
        _toObject = new LinkObject(asto, currFlow);
        _internal = internal;
    }

    public boolean isOptional()
    {
        //System.out.println("Optional for: " + name + ":" + optional);
        return optional;
    }
}

