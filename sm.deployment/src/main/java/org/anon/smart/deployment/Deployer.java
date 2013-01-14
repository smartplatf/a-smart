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
 * File:                org.anon.smart.deployment.Deployer
 * Author:              rsankar
 * Revision:            1.0
 * Date:                13-01-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A deployer for different types of files
 *
 * ************************************************************
 * */

package org.anon.smart.deployment;

import java.util.Map;

import static org.anon.utilities.services.ServiceLocator.*;

import org.anon.utilities.exception.CtxException;

public interface Deployer
{
    public static enum deployers
    {
        ear(new EARDeployer(), "ear"),
        jar(new JARDeployer(), "jar");

        private Deployer _deployer;
        private String _extension;
        private deployers(Deployer dep, String ext)
        {
            _deployer = dep;
            _extension = ext;
        }

        public static Deployer deployerFor(String file)
            throws CtxException
        {
            int li = file.lastIndexOf(".");
            if (li <= 0)
                except().te("Cannot recognize the type of file to be deployed");
            String ext = file.substring(li + 1);

            for (deployers d : deployers.values())
            {
                if (d._extension.equals(ext))
                    return d._deployer;
            }

            except().te("Cannot recognize the type of file to be deployed. " + ext);
            return null;
        }
    }

    public Map<String, String> deploy(String file, DeploymentSuite service)
        throws CtxException;
}

