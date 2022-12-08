package tools.sapcx.commerce.occ;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CxOccSwaggerFilter extends OncePerRequestFilter {
	private final ConfigurationService configurationService;

	public CxOccSwaggerFilter(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		boolean swaggerIsEnabled = configurationService.getConfiguration().getBoolean("sapcxocc.swagger.enabled", false);
		if (!swaggerIsEnabled) {
			String path = request.getRequestURI();
			if (path.endsWith("swagger-ui.html") || path.endsWith("api-docs")) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
			} else {
				filterChain.doFilter(request, response);
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}
}