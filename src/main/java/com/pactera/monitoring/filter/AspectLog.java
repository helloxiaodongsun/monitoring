/*
 * 文件名：WebLogAspect.java 描述： 修改人：ll 修改时间：2017年8月11日 跟踪单号： 修改单号： 修改内容：
 */

package com.pactera.monitoring.filter;


import com.google.gson.Gson;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.utils.IpAddressUtil;
import com.pactera.monitoring.utils.IpUtils;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * 实现Web层的日志切面
 * 
 * @author ll
 * @version v.0.1
 */
@Aspect
@Component
@Order(1)
public class AspectLog
{
    /**
     * log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectLog.class);
    
    
    private static String[] types = {"java.lang.Integer", "java.lang.Double",
        "java.lang.Float", "java.lang.Long", "java.lang.Short",
        "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
        "java.lang.String", "int", "double", "long", "short", "byte",
        "boolean", "char", "float"};
    
    /**
     * gson
     */
    private Gson gson = new Gson();

    /**
     * startTime
     */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * Description: <br> 1、申明一个切点 里面是 execution表达式<br>
     * 
     * @see
     */
    @Pointcut("execution(public * com.pactera.monitoring.controller..*.*(..))")
    private void controllerAspect()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Description: <br> 1、请求method前打印内容<br>
     * @param joinPoint joinPoint
     * @see
     */
    @Before(value = "controllerAspect()")
    public void methodBefore(JoinPoint joinPoint)
    {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        LOGGER.info("===============请求内容===============");
        LOGGER.info("请求地址:{}" , request.getRequestURL());
        LOGGER.info("请求方式:{}" , request.getMethod());
        LOGGER.info("IP:{};浏览器:{}" , IpAddressUtil.getIpAddress(request),IpAddressUtil.getOsAndBrowserInfo(request));

        LOGGER.info("请求类方法:{}" , joinPoint.getSignature());
        String str = "请求类方法参数:" + Arrays.toString(joinPoint.getArgs());
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        StringBuilder sb = null;
        if (parameterNames!=null) {
            sb = new StringBuilder();
            for (int i = 0; i < parameterNames.length; i++) {
                // 对参数解析(参数有可能为基础数据类型，也可能为一个对象，若为对象则需要解析对象中变量名以及值)
                String value = "";
                if (joinPoint.getArgs()[i] == null) {
                    value = "null";
                } else {
                    // 获取对象类型
                    String typeName = joinPoint.getArgs()[i].getClass().getName();
                    boolean flag = false;
                    for (String t : types) {
                        //1 判断是否是基础类型
                        if (t.equals(typeName)) {
                            value = joinPoint.getArgs()[i].toString();
                            flag = true;
                        }
                        if (flag) {
                            break;
                        }
                    }
                    if (!flag) {
                        //2 通过反射获取实体类属性
                        value = getFieldsValue(joinPoint.getArgs()[i]);
                    }
                }
                sb.append(parameterNames[i] + ":" + value + "; ");
            }
        }
        sb = sb == null ? new StringBuilder() : sb;
        
        LOGGER.info("请求方法参数:{}",sb);
        LOGGER.info(str);
        LOGGER.info("===============请求内容===============");
    }

    /**
     * Description: <br> 1、在方法执行完结后打印返回内容<br>
     * @param object object
     * @see
     */
    @AfterReturning(returning = "object", pointcut = "controllerAspect()")
    public void methodAfterReturing(JoinPoint joinPoint,Object object)
    {
	    ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        LOGGER.info("--------------返回内容----------------");
        String json = gson.toJson(object);
        LOGGER.info(json);
        LOGGER.info("--------------返回内容----------------");
        String time = "请求处理时间为:" + (System.currentTimeMillis() - startTime.get());
        LOGGER.info(time);
      //添加到日志表
        try {
	        Method method = getMethod(joinPoint);
			MethodExplain annotation = method.getAnnotation(MethodExplain.class);
	        if(annotation!=null){
	        	String logTableName = null;
	        	String logMenuId = null;
	        	boolean isSave = true;
	        	String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
	        	if(parameterNames!=null&&parameterNames.length>0){
	        		for (int i = 0; i < parameterNames.length; i++) {
	        			if(parameterNames[i].equals("pageNum")){
	        				String value = "";
	        				String typeName = joinPoint.getArgs()[i].getClass().getName();
	        				boolean flag = false;
	        				for (String t : types) {
	        					//1 判断是否是基础类型
	        					if (t.equals(typeName)) {
	        						value = joinPoint.getArgs()[i].toString();
	        						flag = true;
	        					}
	        					if (flag) {
	        						break;
	        					}
	        				}
	        				if (!flag) {
	        					//2 通过反射获取实体类属性
	        					value = getFieldsValue(joinPoint.getArgs()[i]);
	        				}
	        				if(!value.equals("1")){
	        					isSave = false;
	        				}
	        			}
	        			if(parameterNames[i].equals("logTableName")){
	        				String typeName = joinPoint.getArgs()[i].getClass().getName();
	        				boolean flag = false;
	        				for (String t : types) {
	        					//1 判断是否是基础类型
	        					if (t.equals(typeName)) {
	        						logTableName = joinPoint.getArgs()[i].toString();
	        						flag = true;
	        					}
	        					if (flag) {
	        						break;
	        					}
	        				}
	        				if (!flag) {
	        					//2 通过反射获取实体类属性
	        					logTableName = getFieldsValue(joinPoint.getArgs()[i]);
	        				}
	        			}
	        			if(parameterNames[i].equals("logMenuId")){
	        				String typeName = joinPoint.getArgs()[i].getClass().getName();
	        				boolean flag = false;
	        				for (String t : types) {
	        					//1 判断是否是基础类型
	        					if (t.equals(typeName)) {
	        						logMenuId = joinPoint.getArgs()[i].toString();
	        						flag = true;
	        					}
	        					if (flag) {
	        						break;
	        					}
	        				}
	        				if (!flag) {
	        					//2 通过反射获取实体类属性
	        					logMenuId = getFieldsValue(joinPoint.getArgs()[i]);
	        				}
	        			}
	        		}
	        	}
	        	if(isSave){
	        		LOGGER.info("LOGGING BEGIN");
	        		JSONObject jsonobj = JSONObject.fromObject(json);
	        		String code = (String) jsonobj.get("code");
	        		MethodType methodType = annotation.methodType();
	        		String methodName = annotation.methodName();
	        		String opeIp = IpAddressUtil.getIpAddress(request);
	        		String url = request.getServletPath();
	        		String errDesc = (String) jsonobj.get("message");
	        		//LogUtil.saveLog(opeIp, url, methodType.getType(), methodName, code, errDesc,logTableName,logMenuId);
	        		LOGGER.info("LOGGING END");
	        	}
	        }
        } catch (Exception e) {
			LOGGER.error("LOGGING ERROR");
			LOGGER.error(e.getMessage());
		}
    }
    
    /**
     *  解析实体类，获取实体类中的属性
     */
    public static String getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = "short";
        for (String t : types) {
            if (t.equals(typeName)) {
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : types) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //其实，我们可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " : " + f.get(obj) + ", ");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }
    private Method getMethod(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        try {
            return targetClass.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}