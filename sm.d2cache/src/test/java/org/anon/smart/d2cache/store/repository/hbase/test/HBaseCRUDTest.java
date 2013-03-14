package org.anon.smart.d2cache.store.repository.hbase.test;

import java.util.UUID;

import org.anon.smart.d2cache.store.StoreConnection;
import org.anon.smart.d2cache.store.StoreTransaction;
import org.anon.smart.d2cache.store.repository.hbase.HBaseCRUD;
import org.anon.smart.d2cache.store.repository.hbase.HBaseConfig;
import org.anon.smart.d2cache.store.repository.hbase.HBaseConnection;
import org.anon.smart.d2cache.store.repository.hbase.TestHBaseConfig;
import org.anon.utilities.test.reflect.ComplexTestObject;
import org.anon.utilities.test.reflect.SimpleTestObject;
import org.junit.Test;

public class HBaseCRUDTest {

	@Test
	public void connectionTest()  
		throws Exception {
		HBaseConfig config = new TestHBaseConfig("hadoop", "2181", "hadoop:60000", false);
		StoreConnection connection = new HBaseConnection();
		connection.connect(config);
		connection.open("MyTenant", "global");
		System.out.println("Connected to Store..");
		connection.createMetadata("SimpleTestObject", null);
		connection.createMetadata("ComplexTestObject", null);
		UUID txn = UUID.randomUUID();
		long t1 = System.currentTimeMillis();
		StoreTransaction hTxn = connection.startTransaction(txn);
		for(int i = 0;i<1000;i++) 
		{
			SimpleTestObject obj = new SimpleTestObject();
			hTxn.addRecord("SimpleTestObject", "mystr"+i, obj);
		}
		
		hTxn.commit();
		
		long t2 = System.currentTimeMillis();
		System.out.println("Took "+ (t2-t1) + " ms to write simple 1000 objects");
		txn = UUID.randomUUID();
		t1 = System.currentTimeMillis();
		hTxn = connection.startTransaction(txn);
		for(int i= 0;i<1000;i++)
		{
			ComplexTestObject obj = new ComplexTestObject();
			hTxn.addRecord("ComplexTestObject", "mycompobj"+i, obj);
		
		}
		hTxn.commit();
		t2 = System.currentTimeMillis();
		System.out.println("Took "+ (t2-t1) + " ms to write 1000 Complex objects");
		
		//Object res = connection.find("SimpleTestObject", "mystr1");
		//	System.out.println("Got from store:"+res);
		connection.close();
		System.out.println("Closed the connection to Store..");
		
	}
}
