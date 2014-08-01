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
 * File:                org.anon.smart.smcore.inbuilt.responses.ErrorResponse
 * Author:              rsankar
 * Revision:            1.0
 * Date:                22-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * An error response sent when there are errors
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.responses;

import java.util.List;
import java.util.ArrayList;

public class ErrorResponse implements java.io.Serializable
{
    public enum servererrors
    {
        exception(0);

        private int code;
        private servererrors(int cde)
        {
            code = cde;
        }

        public int getCode() { return code; }
    }

    class Error implements java.io.Serializable
    {
        private int code;
        private String context;

        Error(int cde, String ctx)
        {
            code = cde;
            context = ctx;
        }

        Error(servererrors err, Throwable t)
        {
            code = err.getCode();
            if (t != null)
            {
                t.printStackTrace();
                context = t.getMessage();
            }
            else
                context = "A non-descript error has occurred.";
        }

        Error(int cde, Throwable t)
        {
            code = cde;
            if (t != null)
            {
                context = t.getMessage();
                t.printStackTrace();
            }
            else
                context = "A non-descript error has occurred.";
        }
    }

    private List<Error> errors;
    private transient Object _pdata;

    public ErrorResponse(servererrors err, Throwable t)
    {
        errors = new ArrayList<Error>();
        addError(err, t);
        //errors.add(new Error(err, t));
    }

    public void addError(servererrors err, Throwable t)
    {
        boolean added = false;
        if ((t != null) && (t.getLocalizedMessage() != null) && (!t.getLocalizedMessage().equals(t.getMessage())))
        {
            try
            {
                System.out.println("Got the error code as: " + t.getLocalizedMessage());
                int code = Integer.parseInt(t.getLocalizedMessage());
                System.out.println("Got the error code as: " + code);
                added = true;
                errors.add(new Error(code, t));
            }
            catch (Exception e)
            {
            }
        }

        if (!added)
            errors.add(new Error(err, t));
    }
}

