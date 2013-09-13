package org.anon.smart.d2cache.store.repository.hbase;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.anon.smart.d2cache.store.StoreRecord;
import org.anon.utilities.reflect.TVisitor;
import org.anon.utilities.reflect.DataContext;
import org.anon.utilities.exception.CtxException;

public class HbaseObjectTraversal implements TVisitor
{
    private Map<String, List<Object>> _traversed;
    private StoreRecord _rec;
    private boolean _update = false;

    public HbaseObjectTraversal(StoreRecord rec)
    {
        _traversed = new HashMap<String, List<Object>>();
        _rec = rec;
    }

    public void setUpdate() { _update = true; }

    public Object visit(DataContext ctx)
        throws CtxException
    {
        Object ret = null;
        List<Object> lst = null;
        if (ctx.field() != null)
        {
            lst = _traversed.get(ctx.field().getName());
            if (lst == null)
                lst = new ArrayList<Object>();
            lst.add(ctx.fieldVal());
            _traversed.put(ctx.field().getName(), lst);
            ret = ctx.fieldVal();
        }
        else
        {
            lst = _traversed.get(ctx.traversingClazz().getName());
            if (lst == null)
                lst = new ArrayList<Object>();
            lst.add(ctx.traversingObject());

            _traversed.put(ctx.traversingClazz().getName(), lst);
            ret = ctx.traversingObject();
        }
        //vinay
        _rec.append(ctx, _update);
        
        return ret;
    }

    public Map<String, List<Object>> getTraversed()
    {
        return _traversed;
    }
}
