package tools.sapcx.commerce.occ;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import tools.sapcx.commerce.toolkit.testing.testdoubles.config.ConfigurationServiceFake;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@UnitTest
public class CxOccSwaggerFilterTest {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;

	private ConfigurationServiceFake configurationServiceFake;

	private CxOccSwaggerFilter cxOccSwaggerFilterUnderTest;
	private static final String SWAGGER_PROPERTY = "mynetzsch.occ.swagger.enabled";

	@Before
	public void setUp() {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.spy(HttpServletResponse.class);
		filterChain = Mockito.spy(FilterChain.class);
		configurationServiceFake = new ConfigurationServiceFake();
		cxOccSwaggerFilterUnderTest = new CxOccSwaggerFilter(configurationServiceFake);
	}

	@Test
	public void swaggerDisabled_nonSwaggerRequest() throws Exception {
		// Setup
		when(request.getRequestURI()).thenReturn("occ/v2/test?name=swagger");
		configurationServiceFake.setProperty(SWAGGER_PROPERTY, false);
		// Run the test
		cxOccSwaggerFilterUnderTest.doFilterInternal(request, response, filterChain);

		// Verify the results
		verify(filterChain).doFilter(request, response);

	}

	@Test
	public void swaggerDisabled_swaggerUiRequest() throws Exception {
		// Setup
		when(request.getRequestURI()).thenReturn("occ/v2/swagger-ui.html");
		configurationServiceFake.setProperty(SWAGGER_PROPERTY, false);

		// Run the test
		cxOccSwaggerFilterUnderTest.doFilterInternal(request, response, filterChain);

		// Verify the results
		verifyZeroInteractions(filterChain);
		verify(response).setStatus(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void swaggerDisabled_swaggerApiDocsRequest() throws Exception {
		// Setup
		when(request.getRequestURI()).thenReturn("occ/v2/api-docs");
		configurationServiceFake.setProperty(SWAGGER_PROPERTY, false);

		// Run the test
		cxOccSwaggerFilterUnderTest.doFilterInternal(request, response, filterChain);

		// Verify the results
		verifyZeroInteractions(filterChain);
		verify(response).setStatus(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void swaggerEnabled_swaggerRequest() throws Exception {
		// Setup
		when(request.getRequestURI()).thenReturn("occ/v2/swagger-ui.html");
		configurationServiceFake.setProperty(SWAGGER_PROPERTY, true);

		// Run the test
		cxOccSwaggerFilterUnderTest.doFilterInternal(request, response, filterChain);

		// Verify the results
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void swaggerEnabled_nonSwaggerRequest() throws Exception {
		// Setup
		when(request.getRequestURI()).thenReturn("occ/v2/test?param=name");
		configurationServiceFake.setProperty(SWAGGER_PROPERTY, true);

		// Run the test
		cxOccSwaggerFilterUnderTest.doFilterInternal(request, response, filterChain);

		// Verify the results
		verify(filterChain).doFilter(request, response);
	}
}
