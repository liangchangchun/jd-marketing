package com.mk.convention.utils.jd;

import com.lmax.disruptor.EventHandler;

public class JdEventHandler implements EventHandler<JdDataEvent>{

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(JdDataEvent jdDataEvent, long arg1, boolean arg2)
			throws Exception {
		/*if(BMDataContext.UserDataEventType.SAVE.toString().equals(jdDataEvent.getCommand())){
			if(jdDataEvent.getDbRes()!=null){
				jdDataEvent.getDbRes().save(jdDataEvent.getEvent()) ;
			}
			if(jdDataEvent.getEsRes()!=null){
				jdDataEvent.getEsRes().save(jdDataEvent.getEvent()) ;
			}
		}else if(BMDataContext.UserDataEventType.DELETE.toString().equals(jdDataEvent.getCommand())){
			if(jdDataEvent.getDbRes()!=null){
				jdDataEvent.getDbRes().delete(jdDataEvent.getEvent()) ;
			}
			if(jdDataEvent.getEsRes()!=null){
				jdDataEvent.getEsRes().delete(jdDataEvent.getEvent()) ;
			}
		}*/
		if (JdTransformTool.JdDataEventType.JDBC_SAVE.toString().equals(jdDataEvent.getCommand())) {
			jdbcSave(jdDataEvent);
		}
	}
	/**
	 * jdbc insert
	 * @param jdDataEvent
	 */
	private void jdbcSave(JdDataEvent jdDataEvent) {
		BaseDataEvent baseDataEvent = (BaseDataEvent)jdDataEvent.getEvent();
		JdbcSqlAdapter sqla = baseDataEvent.adapterSql(jdDataEvent);
        if (sqla!=null && sqla.getSql()!=null) {
        	jdDataEvent.getDataSource().executeUpdate(sqla.getSql(), sqla.getParameters());
        }
	}
}
