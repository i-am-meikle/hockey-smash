package no.meikle.hockey_smash.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

public class SimpleAuthorizationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4234569487326806733L;

	private String protectedNamespace;
	private String unauthorizedResult;
	private String authorizationSessionKey;

	public void setProtectedNamespace(String protectedNamespace) {
		this.protectedNamespace = protectedNamespace;
	}

	public void setUnauthorizedResult(String failResult) {
		this.unauthorizedResult = failResult;
	}
	
	public void setAuthorizationSessionKey(String authorizationSessionKey) {
		this.authorizationSessionKey = authorizationSessionKey;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		String namespace = ServletActionContext.getActionMapping().getNamespace();

		if (namespace.equals(protectedNamespace)) {

			Object username = ServletActionContext.getRequest().getSession().getAttribute(authorizationSessionKey);

			if (username != null) {

				return invocation.invoke();
			}

			return unauthorizedResult;

		} else {
			return invocation.invoke();
		}

	}

}
