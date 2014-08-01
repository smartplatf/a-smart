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
 * File:                org.anon.smart.smcore.inbuilt.transition.GoogleMapServices
 * Author:              rsankar
 * Revision:            1.0
 * Date:                15-11-2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * A set of services that links to google site
 *
 * ************************************************************
 * */

package org.anon.smart.smcore.inbuilt.transition;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.anon.smart.smcore.data.ConfigData;
import org.anon.smart.smcore.config.ConfigService;
import org.anon.smart.smcore.inbuilt.config.GoogleAppConfig;
import org.anon.smart.smcore.channel.client.pool.ClientConfig;
import org.anon.smart.smcore.channel.client.pool.ClientObjectCreator;
import org.anon.smart.smcore.channel.client.pool.HTTPClientObject;
import org.anon.smart.smcore.channel.client.JSONResponse;

import org.anon.utilities.pool.Pool;
import org.anon.utilities.exception.CtxException;

import static org.anon.utilities.services.ServiceLocator.*;

public class GoogleMapServices
{
    private static final String GOOGLEAPIS = "maps.googleapis.com";
    private static final String PLACEURI = "/maps/api/place/";
    private static final String FORMAT = "json";
    private static final String AUTOCOMPLETE = "autocomplete/";
    private static final String DETAILS = "details/";
    private static final String NEARBYSEARCH = "nearbysearch/";
    private static final String ADD = "add/";
    private static final String DELETE = "delete/";

    private static final String GEOCODEURI = "/maps/api/geocode/";

    public GoogleMapServices()
    {
    }

    private Pool getMapsAPIPool()
        throws CtxException
    {
        ClientConfig ccfg = new ClientConfig("MapsAPI", GOOGLEAPIS, 443, 1, FORMAT);
        Pool p = ClientObjectCreator.getPool(ccfg);
        return p;
    }

    private GoogleAppConfig getConfig()
        throws CtxException
    {
        Class cls = GoogleAppConfig.class;
        Class<? extends ConfigData> ccls = (Class<? extends ConfigData>)cls;
        Object cfg = ConfigService.configFor("MAPS", ccls);
        GoogleAppConfig scfg = (GoogleAppConfig)cfg;
        assertion().assertNotNull(scfg, "Please setup an GoogleMap config for key MAPS before calling this service");
        return scfg;
    }

    private Map intrungoogleapi(String uripath, String api, Map data)
        throws CtxException
    {
        Pool p = getMapsAPIPool();
        HTTPClientObject hclient = (HTTPClientObject)p.lockone();
        String post = hclient.getFormatted(data, "form");
        String uri = uripath + api + FORMAT + "?" + post;
        Object resp = hclient.getData(uri, true);
        p.unlockone(hclient);
        assertion().assertNotNull(resp, "Cannot call the " + api + " uri.");
        JSONResponse r = (JSONResponse)resp;
        Map m = r.getResponse();
        return m;
    }

    private Map rungoogleapi(String api, Map data)
        throws CtxException
    {
        return intrungoogleapi(PLACEURI, api, data);
    }

    private Map rungooglegeocodeapi(Map data)
        throws CtxException
    {
        return intrungoogleapi(GEOCODEURI, "", data);
    }

    private Map postgoogleapi(String api, Map data, Object post)
        throws CtxException
    {
        Pool p = getMapsAPIPool();
        HTTPClientObject hclient = (HTTPClientObject)p.lockone();

        String uridata = hclient.getFormatted(data, "form");
        String pdata = hclient.getObjectFormatted(post, "json");

        String uri = PLACEURI + api + FORMAT + "?" + uridata;
        System.out.println("Posting data: " + pdata + ": to : " + uridata);
        Object resp = hclient.postData(uri, pdata, true);
        p.unlockone(hclient);
        assertion().assertNotNull(resp, "Cannot call the " + api + " uri.");
        JSONResponse r = (JSONResponse)resp;
        Map m = r.getResponse();
        return m;
    }

    private Map getStandard()
        throws CtxException
    {
        GoogleAppConfig cfg = getConfig();
        Map in = new HashMap();
        in.put("key", cfg.getAppKey());
        in.put("sensor", "false");

        return in;
    }

    public void pinLatLng(String pincde, String state, String country, Map output)
        throws CtxException
    {
        Map data = new HashMap();
        data.put("address", pincde + "," + state + "," + country);
        Map m = rungooglegeocodeapi(data);
        String stat = (String)m.get("status");
        assertion().assertNotNull(stat, "Cannot find lat lng for " + pincde);
        assertion().assertTrue(stat.equals("OK"), "Cannot find lat lng for " + pincde);
        List l = (List)m.get("results");
        assertion().assertNotNull(l, "Cannot find lat lng for " + pincde);
        //for (int i = 0; i < l.size(); i++)
        {
            Map m1 = (Map)l.get(0);
            Map m2 = (Map)m1.get("geometry");
            Map m3 = (Map)m2.get("location");
            output.put("lat", m3.get("lat"));
            output.put("lng", m3.get("lng"));
        }
    }

    public void autocomplete(String input, Map output)
        throws CtxException
    {
        Map in = getStandard();
        in.put("input", input);
        Map m = rungoogleapi(AUTOCOMPLETE, in);
        List l = (List)m.get("predictions");
        assertion().assertNotNull(l, "Cannot find autocomplete values for: " + input);
        List ret = new ArrayList();
        for (int i = 0; i < l.size(); i++)
        {
            Map m1 = (Map)l.get(i);
            Map oneloc = new HashMap();
            oneloc.put("description", m1.get("description"));
            oneloc.put("reference", m1.get("reference"));
            ret.add(oneloc);
        }
        output.put("locations", ret);
    }

    public void details(String reference, Map output)
        throws CtxException
    {
        Map in = getStandard();
        in.put("reference", reference);
        Map m = rungoogleapi(DETAILS, in);
        Map det = (Map)m.get("result");
        assertion().assertNotNull(det, "Cannot find the details for: " + reference);
        Map location = new HashMap();
        location.put("url", det.get("url"));
        location.put("name", det.get("name"));
        location.put("address", det.get("formatted_address"));
        Map geom = (Map)det.get("geometry");
        Map loc = (Map)geom.get("location");
        location.put("latitude", loc.get("lat"));
        location.put("longitude", loc.get("lng"));
        List ret = new ArrayList();
        ret.add(location);
        output.put("locations", ret);
    }

    public void nearbysearch(double lat, double lon, int radius, Map output)
        throws CtxException
    {
        Map det = getStandard();
        GoogleAppConfig cfg = getConfig();
        String type = cfg.getPlaceType();
        det.put("types", type);
        String add = lat + "," + lon;
        det.put("location", add);
        det.put("radius", radius);

        Map m = rungoogleapi(NEARBYSEARCH, det);
        List ret = new ArrayList();
        List locs = (List)m.get("results");
        for (int i = 0; (locs != null) && (i < locs.size()); i++)
        {
            Map one = (Map)locs.get(i);
            String nm = (String)one.get("name");
            if (nm != null)
                ret.add(nm);
        }

        output.put("locations", ret);
    }

    public void removePlace(String reference)
        throws CtxException
    {
        Map in = getStandard();
        GoogleDeletePlace dplace = new GoogleDeletePlace(reference);
        postgoogleapi(DELETE, in, dplace);
    }

    public void addPlace(String reference, String name, Map output)
        throws CtxException
    {
        Map det = new HashMap();
        details(reference, det);
        List lst = (List)det.get("locations");
        assertion().assertTrue(lst.size() > 0, "Cannot find the location details for: " + reference);
        Map latloc = (Map)lst.get(0);
        System.out.println("Got: " + latloc + ":" + latloc.get("latitude"));
        Double lat = (Double)latloc.get("latitude");
        Double lon = (Double)latloc.get("longitude");
        GoogleAppConfig cfg = getConfig();
        Map in = getStandard();
        System.out.println("Creating: " + lat + ":" + lon + ":" + name + ":" + cfg.getAccuracy());
        GoogleAddPlace post = new GoogleAddPlace(lat, lon, name, cfg.getAccuracy(), cfg.getPlaceType());

        Map ret = postgoogleapi(ADD, in, post);
        String ref = (String)ret.get("reference");
        output.put("reference", ref);
    }
}

