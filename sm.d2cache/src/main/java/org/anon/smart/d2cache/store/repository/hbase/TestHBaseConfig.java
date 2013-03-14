package org.anon.smart.d2cache.store.repository.hbase;
 
public class TestHBaseConfig implements HBaseConfig {

	private String _zoo;
	private String _zooPort;
	private String _hmaster;
	private boolean _local;
	
	public TestHBaseConfig(String zoo, String port, String hmaster, boolean local) {
		_zoo = zoo;
		_zooPort = port;
		_hmaster = hmaster;
		_local = local;
				
	}
	@Override
	public String zookeeperQuorum() {
		return _zoo;
	}

	@Override
	public String zookeeperPort() {
		return _zooPort;
	}

	@Override
	public String hbaseMaster() {
		return _hmaster;
	}

	@Override
	public boolean isLocal() {
		return _local;
	}

}
