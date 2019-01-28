package exceptions;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import resources.ErrorInformation;


public class CustomExceptionHandler extends DefaultResponseErrorHandler 
{
	private List<HttpMessageConverter<?>> converters;

	public CustomExceptionHandler(RestTemplate template)
	{
		converters = template.getMessageConverters();
	}
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException 
	{
		// take the body that was returned, and convert into a java class
		MediaType mediaType = response.getHeaders().getContentType();
		ErrorInformation error = null;
		for (HttpMessageConverter next : converters)
		{
			if (next.canRead(ErrorInformation.class, mediaType))
			{
				error = (ErrorInformation)next.read(ErrorInformation.class, response);
			}
		}
		
		if (response.getStatusCode() == HttpStatus.NOT_FOUND)
		{
			throw new ResourceNotFoundException(error);
		}
		
		if (response.getStatusCode() == HttpStatus.CONFLICT)
		{
			throw new EditingConflictException();
		}
		
		throw new UnknownHttpErrorException(response.getStatusCode().toString() + response.getStatusCode().getReasonPhrase());
	}

}
