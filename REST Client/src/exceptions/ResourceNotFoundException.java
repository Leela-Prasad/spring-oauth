package exceptions;
import resources.ErrorInformation;

public class ResourceNotFoundException extends RuntimeException 
{
	private ErrorInformation error;
	
	public ResourceNotFoundException(ErrorInformation error) 
	{
		this.error = error;
	}
	
	public ErrorInformation getErrorObject()
	{
		return this.error;
	}

}
