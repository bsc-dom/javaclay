package es.bsc.dataclay.paraver.aspects;

import es.bsc.dataclay.paraver.Paraver;
import es.bsc.dataclay.paraver.ParaverEventType;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ParaverAspects {
	
	private static final String EXTRAE_POINTCUT = "(execution(* es.bsc.dataclay.commonruntime.ClientRuntime.*(..)) "
			+ "|| execution(* es.bsc.dataclay.commonruntime.DataClayRuntime.*(..)) "
			+ "|| 	execution(* es.bsc.dataclay.commonruntime.DataServiceRuntime.*(..)) " 
			+ "|| 	execution(* es.bsc.dataclay.api.DataClay.*(..)) " //WARNING: this class must be always intervened
			+ "|| 	execution(* es.bsc.dataclay.dataservice.DataService.*(..)) "
			+ "|| 	execution(* es.bsc.dataclay.logic.LogicModule.*(..)) "
			+ "|| 	execution(* es.bsc.dataclay.storagelocation..*(..)) "
			+ "|| 	execution(* es.bsc.dataclay.logic.accountmgr..*(..)) "
			+ "|| 	execution(* storage.StorageItf.*(..)) " 
			+ ") "
			+ "&& !execution(* es.bsc.dataclay.paraver..*(..)) "
			+ "&& !execution(* *.activateTracing(..)) "
			+ "&& !execution(* *.deactivateTracing(..)) "
			+ "&& !execution(* *.activateTracingInDataClayServices(..)) "
			+ "&& !execution(* *.deactivateTracingInDataClayServices(..)) "
			+ "&& !execution(* *.getTraces(..)) "
			+ "&& !execution(* *.init(..)) "
			+ "&& !execution(* *.finish(..)) "
			+ "" 
			+ "&& !execution(* es.bsc.dataclay.paraver..*(..)) "
			+ "&& !execution(* *.activateTracing(..)) "
			+ "&& !execution(* *.deactivateTracing(..)) "
			+ "&& !execution(* *.activateTracingInDataClayServices(..)) "
			+ "&& !execution(* *.deactivateTracingInDataClayServices(..)) "
			+ "&& !execution(* *.getTraces(..)) "
			+ "&& !execution(* *.init(..)) "
			+ "&& !execution(* *.finish(..)) "
			+ "&& !execution(* es.bsc.dataclay.DataClayObject.*(..)) "
			+ "&& !execution(* es.bsc.dataclay.serialization.DataClaySerializable.*(..)) "
			+ "&& !execution(* storage.StubItf.*(..)) "
			+ "&& !execution(* storage.StorageObject.*(..)) ";
	

    @Pointcut(EXTRAE_POINTCUT)
	public void extraePointcut(){}
					
	@Before("extraePointcut()")
	public void emitExtraeEventBefore(JoinPoint thisJoinPoint) {
		Paraver.emitEvent(true,
				thisJoinPoint.getSignature().getDeclaringTypeName() + "." + thisJoinPoint.getSignature().getName());
	}

	@After("extraePointcut()")
	public void emitExtraeEventAfter(JoinPoint thisJoinPoint) {
		Paraver.emitEvent(false,
				thisJoinPoint.getSignature().getDeclaringTypeName() + "." + thisJoinPoint.getSignature().getName());
	}

    @AfterThrowing(value = "execution(* *(..))", throwing = "e")
    public void emitExtraeException(JoinPoint thisJoinPoint, Throwable e) {
		Paraver.emitEvent(true, e.toString());
		Paraver.emitEvent(false, e.toString());
	}

}
