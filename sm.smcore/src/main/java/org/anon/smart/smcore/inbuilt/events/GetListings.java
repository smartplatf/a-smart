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
 * File:                org.anon.smart.smcore.inbuilt.events.GetListings
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Aug 5, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * <Purpose>
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.events;

import java.io.Serializable;

public class GetListings implements Serializable
{
    int listingsPerPage;
    int pageNum;
    String sortBy;
    
    String group;
    public GetListings() 
    {
    }
    public String getGroup()
    {
        return group;
    }
    public void setGroup(String group)
    {
        this.group = group;
    }
    public int getListingsPerPage()
    {
        return listingsPerPage;
    }
    public void setListingsPerPage(int listingsPerPage)
    {
        this.listingsPerPage = listingsPerPage;
    }
    public int getPageNum()
    {
        return pageNum;
    }
    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }
    public String getSortBy()
    {
        return sortBy;
    }
    public void setSortBy(String sortBy)
    {
        this.sortBy = sortBy;
    }
    

}
