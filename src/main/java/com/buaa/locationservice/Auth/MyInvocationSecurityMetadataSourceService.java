package com.buaa.locationservice.Auth;

import com.buaa.locationservice.dao.RoleDao;
import com.buaa.locationservice.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description: 加载资源与权限的对应关系,实现FilterInvocationSecurityMetadataSource接口也是必须的。
 *               这个类的工作是从数据库中获取授权信息。
 *               1.其中loadResourceDefine方法不是必须的，这个只是加载所有的资源与权限的对应关系并缓存起来，避免每次获取权限都访问数据库（提高性能），然后getAttributes根据参数（被拦截url）返回权限集合。
 *               这种缓存的实现其实有一个缺点，因为loadResourceDefine方法是放在构造器上调用的，而这个类的实例化只在web服务器启动时调用一次，那就是说loadResourceDefine方法只会调用一次，
 *               2.如果资源和权限的对应关系在启动后发生了改变，那么缓存起来的权限数据就和实际授权数据不一致，那就会授权错误了。但如果资源和权限对应关系是不会改变的，这种方法性能会好很多。
 *               要想解决权限数据的一致性,可以直接在getAttributes方法里面调用数据库操作获取权限数据，通过被拦截url获取数据库中的所有权限，封装成Collection<ConfigAttribute>返回就行了。（灵活、简单
 *               3.容器启动加载顺序：1：调用loadResourceDefine()方法  2：调用supports()方法   3：调用getAllConfigAttributes()方法
 */
@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
	private final static Logger LOGGER = LoggerFactory.getLogger(MyInvocationSecurityMetadataSourceService.class);
	//存放资源配置对象
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = null;

	@Autowired
	private RoleDao roleDao;

	public MyInvocationSecurityMetadataSourceService() {
	}

	public MyInvocationSecurityMetadataSourceService(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
		this.requestMap = requestMap;
		System.out.println(requestMap.size());
	}

	/**
	 * 参数是要访问的url，返回这个url对应的所有权限（或角色）
	 * 每次请求后台就会调用 得到请求所拥有的权限
	 * 这个方法在url请求时才会调用，服务器启动时不会执行这个方法
	 * getAttributes这个方法会根据你的请求路径去获取这个路径应该是有哪些权限才可以去访问。
	 *
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		LOGGER.info("进入自定义MyInvocationSecurityMetadataSourceService");
		//取消这段代码注释情况下,每次服务启动后请求后台只有到数据库中取一次权限;如果注释掉这段代码则每次请求都会到数据库中取权限
//		if (resourceMap == null){
//			// 每次请求 都会去数据库查询权限  貌似很耗性能
//			loadResourceDefine();
//		}
		// object 是一个URL，被用户请求的url。
		String uri = ((FilterInvocation) object).getRequest().getRequestURI();
		LOGGER.info("请求 uri ：" + uri);
		Collection<ConfigAttribute> atts = getRoles(uri);
		if (atts != null && atts.size() != 0) {
			return  atts;
		}

		return null ;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		//要返回true 不然要报异常 SecurityMetadataSource does not support secure object class: class
		return true;
	}

	public Collection<ConfigAttribute> getRoles(String uri) {
		Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
		List<Role> roles = roleDao.getRoleByResource(uri);
		String authorizedSigns;
		ConfigAttribute configAttributes;
		for (Role r:roles) {
			authorizedSigns = r.getName().trim();
			configAttributes = new SecurityConfig(authorizedSigns);
			atts.add(configAttributes);
		}

		return atts;
	}
}
