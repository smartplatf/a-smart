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
 * File:                org.anon.smart.smcore.inbuilt.events.SearchEvent
 * Author:              vjaasti
 * Revision:            1.0
 * Date:                Apr 3, 2013
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
import java.util.Map;

public class SearchEvent implements Serializable {
	private String group;
    private long size = -1; //default returns all
    private long pageNum = -1;
    private long pageSize = -1; //default returns all
    private String sortBy = null;
    private boolean ascending = true;

	private Map<String, String> queryMap;
	public SearchEvent(){}
	
	public Map<String, String> getQueryMap() { return queryMap; }
	public String getGroup() { return group; }
    public long getSize() { if (size == 0) return -1; else return size; } //return all

    public long getPageNum() { return pageNum; }
    public long getPageSize() { if (pageSize == 0) return -1; else return pageSize; }

    public String getSortBy() { return sortBy; }
    public boolean sortAscending() { return ascending; }
}
