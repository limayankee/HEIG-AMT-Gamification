package ch.heigvd.filter;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by matthieu.villard on 21.01.2017.
 */

public class AuthenticationFilter extends BasicAuthenticationFilter {

	private ApplicationRepository applicationRepository;

	@Autowired
	public AuthenticationFilter(AuthenticationManager authenticationManager, ApplicationRepository applicationRepository) {
		super(authenticationManager);
		this.applicationRepository = applicationRepository;
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Application app = applicationRepository.findByName(authentication.getName());
		request.setAttribute("application", app);
	}
}